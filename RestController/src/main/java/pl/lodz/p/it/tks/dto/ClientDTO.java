package pl.lodz.p.it.tks.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.user.Client;

@Data
@NoArgsConstructor
public class ClientDTO extends UserDTO {

    private String firstName;

    private String lastName;

    private String personalId;

    private String city;

    private String street;

    private int houseNumber;

    public ClientDTO(Client client) {
        super(client);
        firstName = client.getFirstName();
        lastName = client.getLastName();
        personalId = client.getPersonalId();
        city = client.getAddress().getCity();
        street = client.getAddress().getStreet();
        houseNumber = client.getAddress().getHouseNumber();
    }
}
