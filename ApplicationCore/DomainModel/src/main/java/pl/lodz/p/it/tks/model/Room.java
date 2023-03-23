package pl.lodz.p.it.tks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room extends AbstractModel {

    private Integer roomNumber;

    private Double price;

    private Integer size;


    public Room(UUID id, Integer roomNumber, Double price, Integer size, long version) {
        super(id, version);
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
