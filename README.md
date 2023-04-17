# NAG-P-ASSIGNMENT-Consumer
Res4J, Jaeger, RMQ, Docker, Security, Swagger


Docker Compose File (docker-compose.yml) :

version: "3"

services:
    jaeger-allinone:
        image: jaegertracing/all-in-one:1.7
        ports:
                - 5775:5775/udp
                - 6831:6831/udp
                - 6832:6832/udp
                - 5778:5778
                - 16686:16686
                - 14268:14268
                - 9411:9411
    rabbitmq:
        container_name: rabbitmq
        image: rabbitmq:management
        ports:
                - "5672:5672"
                - "15672:15672"
    discovery-server:
        container_name: eureka-server-nishant
        image: eureka-server-nishant-nagp
        ports:
                - "8761:8761"
        volumes:
                - /tmp:/workspace/logs
        healthcheck:
                test: ["CMD-SHELL", "curl --fail http://localhost:8761/actuator/health || exit 1"]
                interval: 30s
                timeout: 10s
                retries: 3
    consumer:
        container_name: consumer-service-nishant
        image: consumer-service-nishant-nagp
        ports:
                - "8083:8083"
        links:
                - discovery-server:discovery-server
        environment:
                - discovery_server_url=http://eureka-server-nishant:8761/eureka
                - AMQP_URI=amqp://guest:guest@rabbitmq:5672
                - JAEGER_AGENT_HOST=jaeger-allinone
                - JAEGER_AGENT_PORT=6831
                - JAEGER_SAMPLER_TYPE=const
                - JAEGER_SAMPLER_PARAM=1
        volumes:
                - /tmp:/workspace/logs
        depends_on:
                - discovery-server
    flight-supplier:
        container_name: flight-supplier-service-nishant
        image: flight-supplier-service-nishant-nagp
        ports:
                - "8084:8084"
        links:
            - discovery-server:discovery-server
        environment:
                - discovery_server_url=http://eureka-server-nishant:8761/eureka
                - AMQP_URI=amqp://guest:guest@rabbitmq:5672
                - JAEGER_AGENT_HOST=jaeger-allinone
                - JAEGER_AGENT_PORT=6831
                - JAEGER_SAMPLER_TYPE=const
                - JAEGER_SAMPLER_PARAM=1
        volumes:
                - /tmp:/workspace/logs
        depends_on:
                - discovery-server
    hotel-supplier:
        container_name: hotel-supplier-service-nishant
        image: hotel-supplier-service-nishant-nagp
        ports:
                - "8085:8085"
        links:
            - discovery-server:discovery-server
        environment:
                - discovery_server_url=http://eureka-server-nishant:8761/eureka
                - AMQP_URI=amqp://guest:guest@rabbitmq:5672
                - JAEGER_AGENT_HOST=jaeger-allinone
                - JAEGER_AGENT_PORT=6831
                - JAEGER_SAMPLER_TYPE=const
                - JAEGER_SAMPLER_PARAM=1
        volumes:
                - /tmp:/workspace/logs
        depends_on:
                - discovery-server
    api-gateway:
        container_name: api-gateway-nishant
        image: api-gateway-nishant-nagp
        ports:
                - "8080:8080"
        links:
                - discovery-server:discovery-server
        environment:
                - discovery_server_url=http://eureka-server-nishant:8761/eureka
                - JAEGER_AGENT_HOST=jaeger-allinone
                - JAEGER_AGENT_PORT=6831
                - JAEGER_SAMPLER_TYPE=const
                - JAEGER_SAMPLER_PARAM=1
        volumes:
                - /tmp:/workspace/logs
        depends_on:
                - discovery-server
