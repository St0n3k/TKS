package pl.lodz.p.it.tks.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;
import pl.lodz.p.it.tks.model.user.Client;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class Rent extends AbstractModel{

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private boolean board;

    private double finalCost;

    private Client client;

    private Room room;

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, Client client,
                Room room) {
        validateDates(beginTime, endTime);
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
        validateDates(beginTime, endTime);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }

    private void validateDates(LocalDateTime beginTime, LocalDateTime endTime) {
        if (endTime.isBefore(beginTime) || finalCost < 0) {
            throw new ConstructorArgumentException();
        }
    }
}
