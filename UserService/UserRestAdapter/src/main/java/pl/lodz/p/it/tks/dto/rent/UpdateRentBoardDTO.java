package pl.lodz.p.it.tks.dto.rent;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRentBoardDTO {

    @NotNull
    Boolean board;
}
