package pl.lodz.p.it.tks.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;

@Data
@NoArgsConstructor
public abstract class AbstractModel {
    private Long id;
    private long version;

    public AbstractModel(Long id) {
        if (id < 1) {
            throw new ConstructorArgumentException();
        }
        this.id = id;
    }

    public AbstractModel(Long id, long version) {
        if (id < 1 || version < 0) {
            throw new ConstructorArgumentException();
        }
        this.id = id;
        this.version = version;
    }
}
