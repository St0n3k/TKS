package pl.lodz.p.it.tks.out;

import pl.lodz.p.it.tks.model.Rent;

import java.util.Optional;

public interface RentCommandPort {
    Rent add(Rent rent);
    void remove(Rent rent);
    Optional<Rent> update(Rent rent);
    boolean removeById(Long rentId);
}
