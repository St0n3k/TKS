package pl.lodz.p.it.tks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractModel {
    private Long id;
    private long version;

    public AbstractModel(Long id) {
        this.id = id;
    }
}
