package pl.lodz.p.it.tks.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Room {

    private Long id;

    @NotNull
    @Min(value = 1)
    private int roomNumber;

    @NotNull
    @Min(value = 1)
    private double price;

    @NotNull
    @Min(value = 1)
    private int size;

    public Room(int roomNumber, double price, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }

    public Room(Long id, int roomNumber, double price, int size) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
