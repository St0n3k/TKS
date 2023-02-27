package pl.lodz.p.it.tks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    String jwt;
}
