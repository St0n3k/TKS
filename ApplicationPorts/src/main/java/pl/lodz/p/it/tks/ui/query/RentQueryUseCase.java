package pl.lodz.p.it.tks.ui.query;

import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.model.Rent;

import java.util.List;

public interface RentQueryUseCase {

    Rent getRentById(Long id) throws RentNotFoundException;

    List<Rent> getAllRents();
}
