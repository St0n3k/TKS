package pl.lodz.p.it.tks.model;

import jakarta.validation.constraints.Min;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlType
public class UpdateApartmentSoapDTO {
    @Min(1)
    private Integer roomNumber;

    @Min(1)
    private Integer size;

    @Min(1)
    private Double price;

    @Min(1)
    private Double balconyArea;
}
