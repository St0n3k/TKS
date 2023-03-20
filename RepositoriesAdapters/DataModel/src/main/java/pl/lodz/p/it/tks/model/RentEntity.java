package pl.lodz.p.it.tks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import pl.lodz.p.it.tks.model.user.ClientEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "rent")
@NamedQueries({
    @NamedQuery(name = "Rent.getAll",
                query = "SELECT r FROM RentEntity r"),
    @NamedQuery(name = "Rent.getByRoomId",
                query = "SELECT r FROM RentEntity r WHERE r.room.id = :roomId"),
    @NamedQuery(name = "Rent.getByClientPersonalId",
                query = "SELECT r FROM RentEntity r WHERE r.client.personalId = :personalId"),
    @NamedQuery(name = "Rent.getRentsColliding",
                query = """
                    SELECT r FROM RentEntity r
                    WHERE r.room.roomNumber = :roomNumber
                          AND ((:beginDate BETWEEN r.beginTime AND r.endTime)
                          OR (:endDate BETWEEN r.beginTime AND r.endTime)
                          OR (r.beginTime between :beginDate and :endDate)
                          OR (r.endTime BETWEEN :beginDate AND :endDate))"""),
    @NamedQuery(name = "Rent.removeById",
                query = "DELETE FROM RentEntity r WHERE r.id = :id"),
    @NamedQuery(name = "Rent.getByClientUsername",
                query = "SELECT r FROM RentEntity r WHERE r.client.username = :username"),
    @NamedQuery(name = "Rent.getByClientId",
                query = "SELECT r FROM RentEntity r WHERE r.client.id = :id"),
    @NamedQuery(name = "Rent.getPastRentsByRoom",
                query = "SELECT r from RentEntity r WHERE (r.endTime < CURRENT_TIMESTAMP) AND r.room.id = :id"),
    @NamedQuery(name = "Rent.getActiveRentsByRoom",
                query = "SELECT r from RentEntity r WHERE (r.endTime > CURRENT_TIMESTAMP) AND r.room.id = :id"),
    @NamedQuery(name = "Rent.getPastRentsByClient",
                query = "SELECT r from RentEntity r WHERE (r.endTime < CURRENT_TIMESTAMP) AND r.client.id = :id"),
    @NamedQuery(name = "Rent.getActiveRentsByClient",
                query = "SELECT r from RentEntity r WHERE (r.endTime > CURRENT_TIMESTAMP) AND r.client.id = :id")
})
@Data
@NoArgsConstructor
public class RentEntity extends AbstractEntity {

    @NotNull
    @Column(name = "begin_time")
    @Future
    private LocalDateTime beginTime;

    @NotNull
    @Column(name = "end_time")
    @Future
    private LocalDateTime endTime;

    @NotNull
    @Column
    private boolean board;

    @NotNull
    @Column(name = "final_cost")
    private double finalCost;

    @NotNull
    @ManyToOne
    @CascadeOnDelete
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @NotNull
    @ManyToOne
    @CascadeOnDelete
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    public RentEntity(LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, ClientEntity client,
                      RoomEntity roomEntity) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = roomEntity;
    }

    public RentEntity(Rent rent) {
        super(rent.getId(), rent.getVersion());
        this.beginTime = rent.getBeginTime();
        this.endTime = rent.getEndTime();
        this.board = rent.isBoard();
        this.finalCost = rent.getFinalCost();
        this.client = new ClientEntity(rent.getClient());
        this.room = new RoomEntity(rent.getRoom());
    }

    public Rent mapToRent(){
        return new Rent(this.getId(),
                this.getBeginTime(),
                this.getEndTime(),
                this.isBoard(),
                this.getFinalCost(),
                this.getClient().mapToClient(),
                this.getRoom().mapToRoom(),
                this.getVersion());
    }

    @AssertTrue
    private boolean isEndDateAfterBeginDate() {
        return endTime.isAfter(beginTime);
    }
}
