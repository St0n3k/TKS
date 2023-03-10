package pl.lodz.p.it.tks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment extends Room {

    private Double balconyArea;

    public Apartment(UUID id, Integer roomNumber, Double price, Integer size, Double balconyArea, long version) {
        super(id, roomNumber, price, size, version);
        if (balconyArea < 0) {
            throw new ConstructorArgumentException();
        }
        this.balconyArea = balconyArea;
    }

    public Apartment(Integer roomNumber, Double price, Integer size, Double balconyArea) {
        super(roomNumber, price, size);
        this.balconyArea = balconyArea;
    }
}
