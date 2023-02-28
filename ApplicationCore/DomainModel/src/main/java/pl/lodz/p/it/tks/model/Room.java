package pl.lodz.p.it.tks.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Room {

    private Long id;

    private Integer roomNumber;

    private Double price;

    private Integer size;

    public Room(Integer roomNumber, Double price, Integer size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }

    public Room(Long id, Integer roomNumber, Double price, Integer size) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
