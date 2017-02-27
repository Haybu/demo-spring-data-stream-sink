package com.homedepot;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.SubscribableChannel;

import java.util.logging.Logger;

@SpringBootApplication
@EnableBinding(ProducerChannels.class)
public class CatalogCloudDataflowSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogCloudDataflowSinkApplication.class, args);
	}

	@Bean
	@Scope("prototype")
	public Logger logger(InjectionPoint ip) {
		return Logger.getLogger(ip.getDeclaredType().getName());
	}

	@Bean
    IntegrationFlow integrationFlow(ProducerChannels producerChannels, Logger logger) {
        return IntegrationFlows.from(producerChannels.producer())
                .handle(String.class, (payload, headers) -> {
                    logger.info("new message: " + payload);
                    return null;
                })
                .get();
    }
}

interface ProducerChannels {

    @Input
    SubscribableChannel producer();
}
