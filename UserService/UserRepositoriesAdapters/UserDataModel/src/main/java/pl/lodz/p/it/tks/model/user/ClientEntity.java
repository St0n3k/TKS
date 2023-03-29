package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.AddressEntity;

import java.util.UUID;

@Entity
@DiscriminatorValue("Client")
@Data
@NoArgsConstructor
public class ClientEntity extends UserEntity {

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "personal_id", unique = true)
    private String personalId;

    @NotNull
    @Embedded
    private AddressEntity address;

    public ClientEntity(Client client) {
        this(client.getVersion(),
            client.getId(),
            client.getUsername(),
            client.isActive(),
            client.getPassword(),
            client.getFirstName(),
            client.getLastName(),
            client.getPersonalId(),
            new AddressEntity(client.getAddress()));
    }

    public ClientEntity(long version,
                        UUID id,
                        String username,
                        boolean active,
                        String password,
                        String firstName,
                        String lastName,
                        String personalId,
                        AddressEntity address) {
        super(version, id, username, active, "CLIENT", password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public ClientEntity(UUID id,
                        String username,
                        boolean active,
                        String password,
                        String firstName,
                        String lastName,
                        String personalId,
                        AddressEntity address) {
        super(id, username, active, "CLIENT", password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    @Override
    public User mapToUser() {
        return new Client(this.getId(),
            this.getVersion(),
            this.getUsername(),
            this.isActive(),
            this.getPassword(),
            this.firstName,
            this.lastName,
            this.personalId,
            this.address.mapToAddress());
    }

    public Client mapToClient() {
        return (Client) this.mapToUser();
    }
}
