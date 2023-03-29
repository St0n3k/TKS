package pl.lodz.p.it.tks.ui.command;

import pl.lodz.p.it.tks.exception.rent.CreateRentException;
import pl.lodz.p.it.tks.exception.rent.RemoveRentException;
import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Rent;

import java.util.UUID;


public interface RentCommandUseCase {
    Rent rentRoom(Rent tempRent, UUID clientId, UUID roomId) throws
        UserNotFoundException,
        RoomNotFoundException,
        InactiveUserException,
        CreateRentException;

    Rent updateRentBoard(UUID id, Boolean board) throws InvalidInputException, RentNotFoundException;

    void removeRent(UUID rentId) throws RemoveRentException;
}
