package pl.lodz.p.it.tks.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateApartmentDTO {

    @NotNull
    @Min(value = 1)
    private int roomNumber;

    @NotNull
    @Min(value = 1)
    private double price;

    @NotNull
    @Min(value = 1)
    private int size;

    @NotNull
    @Min(value = 0)
    private Double balconyArea;
}
