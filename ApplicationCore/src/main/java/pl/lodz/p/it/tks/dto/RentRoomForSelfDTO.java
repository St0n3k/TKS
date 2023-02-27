package pl.lodz.p.it.tks.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RentRoomForSelfDTO {
    @NotNull
    @Future
    private LocalDateTime beginTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    private boolean board;

    @AssertTrue
    private boolean isEndDateAfterBeginDate() {
        return endTime.isAfter(beginTime);
    }
}
