package com.programacion.distribuida.authors.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConsulServiceDiscovery {

    private static final Logger LOGGER = Logger.getLogger(ConsulServiceDiscovery.class.getName());

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "localhost")
    private String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    private int consulPort;

    @Inject
    @ConfigProperty(name = "consul.register", defaultValue = "true")
    private boolean consulRegister;

    @Inject
    @ConfigProperty(name = "service.name", defaultValue = "app-authors")
    private String serviceName;

    @Inject
    @ConfigProperty(name = "server.port", defaultValue = "8090")
    private int servicePort;

    @Inject
    @ConfigProperty(name = "service.health-check.path", defaultValue = "/health/ready")
    private String healthCheckPath;

    @Inject
    @ConfigProperty(name = "service.health-check.interval", defaultValue = "10s")
    private String healthCheckInterval;

    @Inject
    @ConfigProperty(name = "service.health-check.deregister-after", defaultValue = "1m")
    private String deregisterAfter;

    private ConsulClient consulClient;
    private String serviceId;
    private String serviceAddress;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        try {
            consulClient = new ConsulClient(consulHost, consulPort);

            if (consulRegister) {
                registerService();
            }

            LOGGER.info("Consul client initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Error initializing Consul: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerService() {
        try {
            serviceAddress = InetAddress.getLocalHost().getHostAddress();

            String hostname = System.getenv().getOrDefault("HOSTNAME", serviceAddress);
            serviceId = serviceName + "-" + hostname + "-" + servicePort;

            NewService newService = new NewService();
            newService.setId(serviceId);
            newService.setName(serviceName);
            newService.setPort(servicePort);
            newService.setAddress(serviceAddress);

            String healthCheckUrl = "http://" + hostname + ":" + servicePort + healthCheckPath;
            NewService.Check check = new NewService.Check();
            check.setHttp(healthCheckUrl);
            check.setInterval(healthCheckInterval);
            check.setDeregisterCriticalServiceAfter(deregisterAfter);
            newService.setCheck(check);

            List<String> tags = List.of(
                    "helidon",
                    "microprofile",
                    "v1.0",
                    "traefik.enable=true",
                    "traefik.http.routers.authors.rule=PathPrefix(`/app-authors`)",
                    "traefik.http.routers.authors.priority=10",
                    "traefik.http.middlewares.authors-stripprefix.stripPrefix.prefixes=/app-authors",
                    "traefik.http.routers.authors.middlewares=authors-stripprefix");
            newService.setTags(tags);

            consulClient.agentServiceRegister(newService);
            LOGGER.info("Service registered in Consul: " + serviceId + " at " + serviceAddress + ":" + servicePort);
        } catch (Exception e) {
            LOGGER.severe("Error registering service in Consul: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void deregister() {
        if (consulClient != null && consulRegister && serviceId != null) {
            try {
                consulClient.agentServiceDeregister(serviceId);
                LOGGER.info("Service deregistered from Consul: " + serviceId);
            } catch (Exception e) {
                LOGGER.warning("Error deregistering service: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}