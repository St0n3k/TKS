package pl.lodz.p.it.tks.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("Room")
@NamedQueries({
    @NamedQuery(name = "Room.getAll",
        query = "SELECT r FROM RoomEntity r"),
    @NamedQuery(name = "Room.getByRoomNumber",
        query = "SELECT r FROM RoomEntity r WHERE r.roomNumber = :roomNumber")
})
@Data

@NoArgsConstructor
public class RoomEntity extends AbstractEntity {

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
        super(room.getId(), room.getVersion());
        this.roomNumber = room.getRoomNumber();
        this.price = room.getPrice();
        this.size = room.getSize();
    }

    public Room mapToRoom() {
        return new Room(this.getId(), this.getRoomNumber(), this.getPrice(), this.getSize(), this.getVersion());
    }
}
