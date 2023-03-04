package pl.lodz.p.it.tks.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.ConstructorArgumentException;

@Data
@NoArgsConstructor
public class Address {
    private String city;

    private String street;

    private int houseNumber;

    public Address(String city, String street, int houseNumber) {
        if (city == null || street == null || houseNumber < 1) {
            throw new ConstructorArgumentException();
        }
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
