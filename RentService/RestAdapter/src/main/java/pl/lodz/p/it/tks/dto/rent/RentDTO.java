package pl.lodz.p.it.tks.dto.rent;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.dto.AbstractDTO;
import pl.lodz.p.it.tks.dto.room.RoomDTO;
import pl.lodz.p.it.tks.dto.user.ClientDTO;
import pl.lodz.p.it.tks.model.Rent;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class RentDTO extends AbstractDTO {

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private boolean board;

    private double finalCost;

    private ClientDTO client;

    private RoomDTO room;

    public RentDTO(Rent rent) {
        super(rent.getId(), rent.getVersion());
        beginTime = rent.getBeginTime();
        endTime = rent.getEndTime();
        board = rent.isBoard();
        finalCost = rent.getFinalCost();
        client = new ClientDTO(rent.getClient());
        room = new RoomDTO(rent.getRoom());
    }
}
