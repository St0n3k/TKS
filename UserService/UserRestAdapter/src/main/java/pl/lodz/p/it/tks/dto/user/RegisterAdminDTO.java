package pl.lodz.p.it.tks.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAdminDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
