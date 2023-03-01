package pl.lodz.p.it.tks.ui;

import pl.lodz.p.it.tks.exception.InvalidInputException;
import pl.lodz.p.it.tks.exception.rent.CreateRentException;
import pl.lodz.p.it.tks.exception.rent.RemoveRentException;
import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Rent;

import java.util.List;

public interface RentUseCase {
    Rent rentRoom(Rent tempRent, Long clientId, Long roomId) throws
            UserNotFoundException,
            RoomNotFoundException,
            InactiveUserException,
            CreateRentException;

    Rent getRentById(Long id) throws RentNotFoundException;

    List<Rent> getAllRents();

    Rent updateRentBoard(Long id, Boolean board) throws InvalidInputException, RentNotFoundException;

    void removeRent(Long rentId) throws RemoveRentException;
}
