package pl.lodz.p.it.tks.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class AbstractSoapDTO {
    private UUID id;
    private long version;

    public AbstractSoapDTO(UUID id, long version) {
        this.id = id;
        this.version = version;
    }
}
