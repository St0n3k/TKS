package pl.lodz.p.it.tks.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class AbstractModel {
    private UUID id;
    private long version;

    public AbstractModel(UUID id) {
        this.id = id;
    }

    public AbstractModel(UUID id, long version) {
        this.id = id;
        this.version = version;
    }
}
