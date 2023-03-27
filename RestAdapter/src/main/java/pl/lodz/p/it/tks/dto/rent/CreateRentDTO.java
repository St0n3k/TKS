package pl.lodz.p.it.tks.dto.rent;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentDTO {
    @NotNull
    @Future
    private LocalDateTime beginTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    private boolean board;

    @NotNull
    private UUID clientId;

    @NotNull
    private UUID roomId;

    @AssertTrue
    private boolean isEndDateAfterBeginDate() {
        return endTime.isAfter(beginTime);
    }
}
