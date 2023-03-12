package pl.lodz.p.it.tks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.Apartment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDTO extends RoomDTO {

    private Double balconyArea;

    public ApartmentDTO(Apartment apartment) {
        super(apartment);
        balconyArea = apartment.getBalconyArea();
    }
}
