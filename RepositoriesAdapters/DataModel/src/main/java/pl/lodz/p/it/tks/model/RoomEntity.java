package pl.lodz.p.it.tks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
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

    @Column(name = "room_number", unique = true)
    private int roomNumber;

    @Column
    private double price;

    @Column
    private int size;

    public RoomEntity(int roomNumber, double price, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }

    public RoomEntity(Room room) {
        this.id = room.getId();
        this.roomNumber = room.getRoomNumber();
        this.price = room.getPrice();
        this.size = room.getSize();
        this.setVersion(room.getVersion());
    }

    public Room mapToRoom() {
        return new Room(this.getId(), this.getRoomNumber(), this.getPrice(), this.getSize(), this.getVersion());
    }
}
