package pl.lodz.p.it.tks.user.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDTO {

    @Pattern(regexp = "[^\\d\\s!?_#$%^&*()@=+,.|/~`'\"\\\\]+")
    private String firstName;

    @Pattern(regexp = "[^\\d\\s!?_#$%^&*()@=+,.|/~`'\"\\\\]+")
    private String lastName;
}
