package pl.lodz.p.it.tks.model;

import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlType(name = "Apartment")
public class ApartmentSoapDTO extends RoomSoapDTO {
    private Double balconyArea;

    public ApartmentSoapDTO(Apartment apartment) {
        super(apartment);
        balconyArea = apartment.getBalconyArea();
    }
}

