package pl.lodz.p.it.tks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.lodz.p.it.tks.model.user.ClientEntity;

import java.time.LocalDateTime;

// TODO if possible, refactor to use single query with addidtional parameter

@Entity
@NamedQueries({
    @NamedQuery(name = "Rent.getAll",
                query = "SELECT r FROM RentEntity r"),
    @NamedQuery(name = "Rent.getByRoomId",
                query = "SELECT r FROM RentEntity r WHERE r.roomEntity.id = :roomId"),
    @NamedQuery(name = "Rent.getByClientPersonalId",
                query = "SELECT r FROM RentEntity r WHERE r.client.personalId = :personalId"),
    @NamedQuery(name = "Rent.getRentsColliding",
                query = """
                    SELECT r FROM RentEntity r
                    WHERE r.roomEntity.roomNumber = :roomNumber
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
                query = "SELECT r from RentEntity r WHERE (r.endTime < CURRENT_TIMESTAMP) AND r.roomEntity.id = :id"),
    @NamedQuery(name = "Rent.getActiveRentsByRoom",
                query = "SELECT r from RentEntity r WHERE (r.endTime > CURRENT_TIMESTAMP) AND r.roomEntity.id = :id"),
    @NamedQuery(name = "Rent.getPastRentsByClient",
                query = "SELECT r from RentEntity r WHERE (r.endTime < CURRENT_TIMESTAMP) AND r.client.id = :id"),
    @NamedQuery(name = "Rent.getActiveRentsByClient",
                query = "SELECT r from RentEntity r WHERE (r.endTime > CURRENT_TIMESTAMP) AND r.client.id = :id")
})
@Data
@NoArgsConstructor
public class RentEntity extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "rentId", strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Long id;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    @AssertTrue
    private boolean isEndDateAfterBeginDate() {
        return endTime.isAfter(beginTime);
    }
}
