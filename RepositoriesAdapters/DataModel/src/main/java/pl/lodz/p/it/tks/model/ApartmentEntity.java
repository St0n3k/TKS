package pl.lodz.p.it.tks.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Apartment")
@Data
@NoArgsConstructor
public class ApartmentEntity extends RoomEntity {


    @Column
    private double balconyArea;

    public ApartmentEntity(int roomNumber, double price, int size, double balconyArea) {
        super(roomNumber, price, size);
        this.balconyArea = balconyArea;
    }

    public ApartmentEntity(Apartment apartment) {
        super(apartment);
        this.balconyArea = apartment.getBalconyArea();
    }

    @Override
    public Room mapToRoom() {
        return new Apartment(this.getId(), this.getRoomNumber(), this.getPrice(), this.getSize(), this.getBalconyArea(), this.getVersion());
    }
}
