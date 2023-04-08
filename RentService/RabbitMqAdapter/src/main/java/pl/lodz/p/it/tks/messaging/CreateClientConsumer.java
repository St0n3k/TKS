package pl.lodz.p.it.tks.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class CreateClientConsumer {

    @Inject
    private Channel channel;

    private void consume(@Observes Startup event) {
        if (channel == null) {
            System.out.println("Error during initializing consumer, connection is not established");
            return;
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };

        String queueName = "CREATE_USER_QUEUE";

        try {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException ignored) {
            System.out.println("Error during connecting to queue");
        }
    }
}
