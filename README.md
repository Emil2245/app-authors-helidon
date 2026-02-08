# app-authors-helidon

Helidon MP application that uses JPA with an in-memory H2 database.

## Build and run


With JDK21
```bash
mvn package
java -jar target/app-authors-helidon.jar
```

## Exercise the application

Basic:
```
curl -X GET http://localhost:8080/simple-greet
Hello World!
```


JSON:
```
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
```



## Using CORS

### Sending "simple" CORS requests

The following requests illustrate the CORS protocol with the example app.

By setting `Origin` and `Host` headers that do not indicate the same system we trigger CORS processing in the
 server:

```bash
# Follow the CORS protocol for GET
curl -i -X GET -H "Origin: http://foo.com" -H "Host: here.com" http://localhost:8080/cors

HTTP/1.1 200 OK
Access-Control-Allow-Origin: *
Content-Type: application/json
Date: Thu, 30 Apr 2020 17:25:51 -0500
Vary: Origin
connection: keep-alive
content-length: 27

{"greeting":"Hello World!"}
```
Note the new headers `Access-Control-Allow-Origin` and `Vary` in the response.

These are what CORS calls "simple" requests; the CORS protocol for these adds headers to the request and response which
the client and server exchange anyway.

### "Non-simple" CORS requests

The CORS protocol requires the client to send a _pre-flight_ request before sending a request
that changes state on the server, such as `PUT` or `DELETE`, and to check the returned status
and headers to make sure the server is willing to accept the actual request. CORS refers to such `PUT` and `DELETE`
requests as "non-simple" ones.

This command sends a pre-flight `OPTIONS` request to see if the server will accept a subsequent `PUT` request from the
specified origin to change the greeting:
```bash
curl -i -X OPTIONS \
    -H "Access-Control-Request-Method: PUT" \
    -H "Origin: http://foo.com" \
    -H "Host: here.com" \
    http://localhost:8080/cors/greeting

HTTP/1.1 200 OK
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Origin: http://foo.com
Date: Thu, 30 Apr 2020 17:30:59 -0500
transfer-encoding: chunked
connection: keep-alive
```
The successful status and the returned `Access-Control-Allow-xxx` headers indicate that the
 server accepted the pre-flight request. That means it is OK for us to send `PUT` request to perform the actual change
 of greeting. (See below for how the server rejects a pre-flight request.)
```bash
curl -i -X PUT \
    -H "Origin: http://foo.com" \
    -H "Host: here.com" \
    -H "Access-Control-Allow-Methods: PUT" \
    -H "Access-Control-Allow-Origin: http://foo.com" \
    http://localhost:8080/greet/Hola

HTTP/1.1 200 OK
Access-Control-Allow-Origin: http://foo.com
Date: Thu, 30 Apr 2020 17:32:55 -0500
Vary: Origin
connection: keep-alive

Hola World!
```

Note that the tests in the example `MainTest` class follow these same steps.


## Try health

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...

```


## Building a Native Image

The generation of native binaries requires an installation of GraalVM 22.1.0+.

You can build a native binary using Maven as follows:

```
mvn -Pnative-image install -DskipTests
```

The generation of the executable binary may take a few minutes to complete depending on
your hardware and operating system. When completed, the executable file will be available
under the `target` directory and be named after the artifact ID you have chosen during the
project generation phase.



### Database Setup

Start your database before running this example.

Example docker commands to start databases in temporary containers:

H2:
```
docker run --rm --name h2 -p 9092:9082 -p 8082:8082 nemerosa/h2
```
For details, see https://www.h2database.com/html/cheatSheet.html



## Building the Docker Image

```
docker build -t app-authors-helidon .
```

## Running the Docker Image

```
docker run --rm -p 8080:8080 app-authors-helidon:latest
```

Exercise the application as described above.
                                

## Run the application in Kubernetes

If you don’t have access to a Kubernetes cluster, you can [install one](https://helidon.io/docs/latest/#/about/kubernetes) on your desktop.

### Verify connectivity to cluster

```
kubectl cluster-info                        # Verify which cluster
kubectl get pods                            # Verify connectivity to cluster
```

### Deploy the application to Kubernetes

```
kubectl create -f app.yaml                              # Deploy application
kubectl get pods                                        # Wait for quickstart pod to be RUNNING
kubectl get service  app-authors-helidon                     # Get service info
kubectl port-forward service/app-authors-helidon 8081:8080   # Forward service port to 8081
```

You can now exercise the application as you did before but use the port number 8081.

After you’re done, cleanup.

```
kubectl delete -f app.yaml
```

