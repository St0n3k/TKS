package pl.lodz.p.it.tks.ui.query;

import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.model.Rent;

import java.util.List;
import java.util.UUID;

public interface RentQueryUseCase {

    Rent getRentById(UUID id) throws RentNotFoundException;

    List<Rent> getAllRents();
}
