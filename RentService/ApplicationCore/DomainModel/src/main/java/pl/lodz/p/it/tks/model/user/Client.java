package pl.lodz.p.it.tks.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.Address;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Client extends User {

    private String firstName;

    private String lastName;

    private String personalId;

    private Address address;

    public Client(String username,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address) {
        super(username);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(UUID id,
                  String username,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address) {
        super(id, username);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(UUID id,
                  long version,
                  String username,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address) {
        super(id, version, username);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(String firstName, String lastName, String personalId, Address address) {
        this(null, firstName, lastName, personalId, address);
    }
}
