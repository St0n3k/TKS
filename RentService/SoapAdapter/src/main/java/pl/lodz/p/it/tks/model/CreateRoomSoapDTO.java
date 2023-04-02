package pl.lodz.p.it.tks.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlType
public class CreateRoomSoapDTO {
    @NotNull
    @Min(value = 1)
    private int roomNumber;

    @NotNull
    @Min(value = 1)
    private double price;

    @NotNull
    @Min(value = 1)
    private int size;
}
