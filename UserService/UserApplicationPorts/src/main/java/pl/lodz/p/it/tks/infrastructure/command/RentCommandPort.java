package pl.lodz.p.it.tks.infrastructure.command;

import pl.lodz.p.it.tks.model.Rent;

import java.util.Optional;
import java.util.UUID;

public interface RentCommandPort {
    Rent add(Rent rent);

    void remove(Rent rent);

    Optional<Rent> update(Rent rent);

    boolean removeById(UUID rentId);
}
