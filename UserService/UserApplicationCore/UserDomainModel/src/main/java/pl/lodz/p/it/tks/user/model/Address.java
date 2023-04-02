package pl.lodz.p.it.tks.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    private String city;

    private String street;

    private int houseNumber;

    public Address(String city, String street, int houseNumber) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
