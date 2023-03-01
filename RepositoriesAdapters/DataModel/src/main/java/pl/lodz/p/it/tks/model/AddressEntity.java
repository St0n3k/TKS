package pl.lodz.p.it.tks.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Access(AccessType.FIELD)
@Data
@NoArgsConstructor
public class AddressEntity {
    @NotNull
    @Column
    private String city;

    @NotNull
    @Column
    private String street;

    @NotNull
    @Column(name = "house_number")
    private int houseNumber;

    public AddressEntity(String city, String street, int houseNumber) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public AddressEntity(Address address) {
        this.city = address.getCity();
        this.street = address.getStreet();
        this.houseNumber = address.getHouseNumber();
    }

    public Address mapToAddress(){
        return new Address(this.getCity(), this.getStreet(), this.getHouseNumber());
    }
}
