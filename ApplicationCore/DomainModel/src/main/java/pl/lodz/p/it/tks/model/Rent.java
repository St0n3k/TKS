package pl.lodz.p.it.tks.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.user.Client;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class Rent extends AbstractModel{

    @NotNull
    @Future
    private LocalDateTime beginTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    @NotNull
    private boolean board;

    @NotNull
    private double finalCost;

    @NotNull
    private Client client;

    @NotNull
    private Room room;

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, Client client,
                Room room) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }

    public Rent(Long id, LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, Client client,
                Room room, long version) {
        super(id, version);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }

    @AssertTrue
    private boolean isEndDateAfterBeginDate() {
        return endTime.isAfter(beginTime);
    }
}
