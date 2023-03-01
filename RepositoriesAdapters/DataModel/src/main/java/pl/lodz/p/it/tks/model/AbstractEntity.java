package pl.lodz.p.it.tks.model;

import java.io.Serializable;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

    @Version
    private long version = 0;

    public AbstractEntity(long version) {
        this.version = version;
    }

}
