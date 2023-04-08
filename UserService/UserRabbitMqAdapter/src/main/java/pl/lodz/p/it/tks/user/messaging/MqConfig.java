package pl.lodz.p.it.tks.user.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@ApplicationScoped
public class MqConfig {

    private Connection connection;

    @Produces
    public Channel getChannel() throws IOException {
        if (connection == null) {
            System.out.println("Cannot get channel. Connection with RabbitMQ is not established");
            return null;
        }
        return connection.createChannel();
    }

    @PostConstruct
    void afterCreate() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //TODO get connection properties from microprofile config
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("password");

        try {
            connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException ignored) {
            System.out.println("Error during establishing connection with RabbitMQ");
        }
    }

    @PreDestroy
    void beforeDestroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException ignored) {
                System.out.println("Error during closing connection with RabbitMQ");
            }
        }
    }
}
