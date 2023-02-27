package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.Address;

@Entity
@Data
@NoArgsConstructor
public class Client extends User {

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
    private Address address;


    public Client(String username, String firstName, String lastName, String personalId, Address address,
                  String password) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.setRole("CLIENT");
    }
}
