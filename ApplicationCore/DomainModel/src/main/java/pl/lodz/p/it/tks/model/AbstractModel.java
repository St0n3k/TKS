package pl.lodz.p.it.tks.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;

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
        if (version < 0) {
            throw new ConstructorArgumentException();
        }
        this.id = id;
        this.version = version;
    }
}
