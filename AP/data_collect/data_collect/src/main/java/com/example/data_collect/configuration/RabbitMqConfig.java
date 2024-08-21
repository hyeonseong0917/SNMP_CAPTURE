package com.example.data_collect.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String LOG_QUEUE_NAME = "log_queue";

    @Bean
    public Queue logQueue() {
        return QueueBuilder.durable(LOG_QUEUE_NAME)
                .autoDelete()
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(ConnectionFactory connectionFactory,
                                                          SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(1); // 프리페치 수를 1로 설정
        factory.setConcurrentConsumers(3); // 동시에 실행할 소비자 수를 설정
        factory.setMaxConcurrentConsumers(5); // 최대 소비자 수를 설정
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}

