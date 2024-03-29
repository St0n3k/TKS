package pl.lodz.p.it.tks.user.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    @NotNull
    private String newPassword;

    @NotNull
    private String oldPassword;

}
