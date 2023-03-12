package pl.lodz.p.it.tks.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class AbstractDTO {
    private UUID id;
    private long version;

    public AbstractDTO(UUID id, long version) {
        this.id = id;
        this.version = version;
    }
}
