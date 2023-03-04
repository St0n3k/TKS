package pl.lodz.p.it.tks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room extends AbstractModel {

    private Integer roomNumber;

    private Double price;

    private Integer size;


    public Room(Long id, Integer roomNumber, Double price, Integer size, long version) {
        super(id, version);
        if (roomNumber < 1 || price < 1 || size < 1) {
            throw new ConstructorArgumentException();
        }
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
