package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.AddressEntity;

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


    public ClientEntity(String username, String firstName, String lastName, String personalId, AddressEntity address,
                        String password) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.setRole("CLIENT");
    }

    public ClientEntity(Client client) {
        super(client.getUsername(), client.getPassword());
        this.setId(client.getId());
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.personalId = client.getPersonalId();
        this.address = new AddressEntity(client.getAddress());
        this.setRole("CLIENT");
    }

    public Client mapToClient(){
        return new Client(this.getId(), this.getUsername(),
                this.getFirstName(),
                this.getLastName(),
                this.getPersonalId(),
                this.getAddress().mapToAddress(),
                this.getPassword());
    }

    public User mapToUser() {
        return new Client(this.getId(),
                          this.getUsername(),
                          this.firstName,
                          this.lastName,
                          this.personalId,
                          this.address.mapToAddress(),
                          this.getPassword());
    }
}
