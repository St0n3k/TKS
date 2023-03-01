package pl.lodz.p.it.tks.model.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.Address;

@Data
@NoArgsConstructor
public class Client extends User {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String personalId;


    @NotNull
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
                  String password) {
        super(id, username,  "CLIENT", password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }
}
