package pl.lodz.p.it.tks.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Version
    private long version = 0;

    public AbstractEntity(long version) {
        this.version = version;
    }
    public AbstractEntity(UUID id) {
        this.id = id;
    }

}
