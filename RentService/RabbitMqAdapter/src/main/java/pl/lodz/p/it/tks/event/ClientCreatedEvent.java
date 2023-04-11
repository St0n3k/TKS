package pl.lodz.p.it.tks.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.Address;
import pl.lodz.p.it.tks.model.user.Client;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreatedEvent {

    @NotNull
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    @Pattern(regexp = "[^\\d\\s!?_#$%^&*()@=+,.|/~`'\"\\\\]+")
    private String firstName;

    @NotNull
    @Pattern(regexp = "[^\\d\\s!?_#$%^&*()@=+,.|/~`'\"\\\\]+")
    private String lastName;

    @NotNull
    @Pattern(regexp = "[0-9]+")
    private String personalID;

    @NotNull
    private Address address;

    public ClientCreatedEvent(Client client) {
        this.id = client.getId();
        this.username = client.getUsername();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.address = client.getAddress();
        this.personalID = client.getPersonalId();
    }

    public Client toClient() {
        return new Client(id, username, firstName, lastName, personalID, address);
    }
}
