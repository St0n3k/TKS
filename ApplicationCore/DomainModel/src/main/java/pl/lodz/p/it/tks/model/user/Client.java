package pl.lodz.p.it.tks.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.Address;

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
        super(username, null, "CLIENT");
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(String username,
                  String password,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address) {
        super(username, password, "CLIENT");
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(Long id,
                  String username,
                  boolean active,
                  String password,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address) {
        super(id, username, active, "CLIENT", password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(Long id,
                  long version,
                  String username,
                  boolean active,
                  String password,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address) {
        super(id, version, username, active, "CLIENT", password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(String firstName, String lastName, String personalId, Address address) {
        this(null, firstName, lastName, personalId, address);
    }
}
