package pl.lodz.p.it.tks.dto.room;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApartmentDTO {

    @Min(1)
    private Integer roomNumber;

    @Min(1)
    private Integer size;

    @Min(1)
    private Double price;

    @Min(1)
    private Double balconyArea;
}
