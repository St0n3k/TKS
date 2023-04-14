package pl.lodz.p.it.tks.user.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.tks.user.event.ClientRollbackEvent;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.ui.command.UserCommandUseCase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class ClientResponseConsumer {

    @Inject
    private Channel channel;

    @Inject
    private UserCommandUseCase userCommandUseCase;

    @Inject
    @ConfigProperty(name = "user_response_queue_name", defaultValue = "USER_RESPONSE_QUEUE")
    private String responseQueueName;

    private void initConsumer(@Observes Startup event) {
        if (channel == null) {
            System.out.println("Error during initializing consumer, connection is not established");
            return;
        }

        try {
            channel.queueDeclare(responseQueueName, true, false, false, null);
            channel.basicConsume(responseQueueName, true, this::deliverCallback, consumerTag -> { });
        } catch (IOException ignored) {
            System.out.println("Error during connecting to queue");
        }
    }

    void deliverCallback(String consumerTag, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        ClientRollbackEvent clientRollbackEvent;

        try (Jsonb jsonb = JsonbBuilder.create()) {
            clientRollbackEvent = jsonb.fromJson(message, ClientRollbackEvent.class);
        } catch (Exception e) {
            System.out.println("Invalid message format");
            return;
        }

        try {
            userCommandUseCase.deleteUser(clientRollbackEvent.getId());
        } catch (UserNotFoundException e) {
            System.out.println("Error deleting client");
        }
    }
}
