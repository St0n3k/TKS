package pl.lodz.p.it.tks.user.messaging;

import com.rabbitmq.client.Channel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.tks.user.event.ClientCreatedEvent;

import java.io.IOException;

@ApplicationScoped
public class ClientProducer {

    @Inject
    private Channel channel;

    @Inject
    @ConfigProperty(name = "user_queue_name", defaultValue = "USER_QUEUE")
    private String queueName;

    public void produce(ClientCreatedEvent event) {
        if (channel == null) {
            System.out.println("Error during initializing consumer, connection is not established");
            return;
        }

        String message;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            message = jsonb.toJson(event);
        } catch (Exception e) {
            System.out.println("Invalid message format");
            return;
        }

        try {
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            System.out.println("Error during producing message, connection is not established");
        }
    }

    private void initProducer(@Observes Startup event) {
        if (channel == null) {
            System.out.println("Error during initializing producer, connection is not established");
            return;
        }

        try {
            channel.queueDeclare(queueName, true, false, false, null);
        } catch (IOException ignored) {
            System.out.println("Error during connecting to queue");
        }
    }
}
