package pl.lodz.p.it.tks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NamedQueries({
    @NamedQuery(name = "Room.getAll",
                query = "SELECT r FROM RoomEntity r"),
    @NamedQuery(name = "Room.getByRoomNumber",
                query = "SELECT r FROM RoomEntity r WHERE r.roomNumber = :roomNumber"),
    @NamedQuery(name = "Room.existsById",
                query = "select (count(r) > 0) from RoomEntity r where r.id = :id")
})
@Data
@NoArgsConstructor
public class RoomEntity extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "roomId", strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @NotNull
    @Column(name = "room_number", unique = true)
    @Min(value = 1)
    private int roomNumber;

    @NotNull
    @Column
    @Min(value = 1)
    private double price;

    @NotNull
    @Column
    @Min(value = 1)
    private int size;

    public RoomEntity(int roomNumber, double price, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }

    public RoomEntity(Room room) {
        this.roomNumber = room.getRoomNumber();
        this.price = room.getPrice();
        this.size = room.getSize();
    }

    public Room mapToRoom(){
        return new Room(this.getId(), this.getRoomNumber(), this.getPrice(), this.getSize());
    }
}
