package cl.duoc.EFT_S9_GRUPO11.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.queue.normal}")
    private String queueNormal;

    @Value("${app.queue.error}")
    private String queueError;

    @Bean
    public Queue normalQueue() {
        // Cola 1: Procesamiento normal
        return new Queue(queueNormal, true);
    }

    @Bean
    public Queue errorQueue() {
        // Cola 2: Almacenamiento de mensajes con errores
        return new Queue(queueError, true);
    }
}