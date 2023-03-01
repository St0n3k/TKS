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


    public Client(String username, String firstName, String lastName, String personalId, Address address,
                  String password) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }

    public Client(Long id, String username, String firstName, String lastName, String personalId, Address address,
                  String password, boolean active, long version) {
        super(id, version, username, "CLIENT", password, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }
}
