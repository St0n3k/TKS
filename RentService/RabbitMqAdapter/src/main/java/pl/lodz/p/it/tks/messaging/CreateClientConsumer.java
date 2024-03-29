package pl.lodz.p.it.tks.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.tks.event.ClientCreatedEvent;
import pl.lodz.p.it.tks.event.ClientRollbackEvent;
import pl.lodz.p.it.tks.exception.user.CreateUserException;
import pl.lodz.p.it.tks.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.ui.command.UserCommandUseCase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class CreateClientConsumer {

    @Inject
    private Channel channel;

    @Inject
    private UserCommandUseCase userCommandUseCase;

    @Inject
    private ClientProducer clientProducer;

    @Inject
    @ConfigProperty(name = "user_queue_name", defaultValue = "USER_QUEUE")
    private String queueName;

    private void initConsumer(@Observes Startup event) {
        if (channel == null) {
            System.out.println("Error during initializing consumer, connection is not established");
            return;
        }

        try {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicConsume(queueName, true, this::deliverCallback, consumerTag -> { });
        } catch (IOException ignored) {
            System.out.println("Error during connecting to queue");
        }
    }

    void deliverCallback(String consumerTag, Delivery delivery) throws IOException {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        ClientCreatedEvent clientCreatedEvent;

        try (Jsonb jsonb = JsonbBuilder.create()) {
            clientCreatedEvent = jsonb.fromJson(message, ClientCreatedEvent.class);
        } catch (Exception e) {
            System.out.println("Invalid message format");
            return;
        }

        if (clientCreatedEvent.isEditEvent()) {
            try {
                userCommandUseCase.updateUser(clientCreatedEvent.getId(), clientCreatedEvent.toClient());
            } catch (UpdateUserException | UserNotFoundException e) {
                System.out.println("Error during updating client");
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true);
            }

        } else {
            try {
                userCommandUseCase.registerClient(clientCreatedEvent.toClient());
            } catch (CreateUserException e) {
                System.out.println("Error during saving client to database");
                clientProducer.produce(new ClientRollbackEvent(clientCreatedEvent.getId()));
            }
        }
    }
}
