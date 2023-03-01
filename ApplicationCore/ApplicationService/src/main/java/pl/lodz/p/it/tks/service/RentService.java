package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.InvalidInputException;
import pl.lodz.p.it.tks.exception.rent.CreateRentException;
import pl.lodz.p.it.tks.exception.rent.RemoveRentException;
import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.in.RentQueryPort;
import pl.lodz.p.it.tks.in.RoomQueryPort;
import pl.lodz.p.it.tks.in.UserQueryPort;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.out.RentCommandPort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
public class RentService {


    @Inject
    private UserQueryPort userQueryPort;
    @Inject
    private RoomQueryPort roomQueryPort;
    @Inject
    private RentQueryPort rentQueryPort;
    @Inject
    private RentCommandPort rentCommandPort;

    /**
     * Method for creating a new rent. Rent will be created if client and room exists in database, and if rent period
     * is not colliding with existing rents.
     *
     * @param tempRent object containing information about rent which creation will be attempted.
     * @return
     * @throws UserNotFoundException if user was not found
     * @throws RoomNotFoundException if room was not found
     * @throws InactiveUserException if user is inactive
     */
    public Rent rentRoom(Rent tempRent, Long clientId, Long roomId) throws
            UserNotFoundException,
            RoomNotFoundException,
            InactiveUserException,
            CreateRentException {
        Optional<User> optionalUser = userQueryPort.getById(clientId);
        Optional<Room> optionalRoom = roomQueryPort.getById(roomId);


        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException();
        }

        Client client = (Client) optionalUser.get();
        Room room = optionalRoom.get();

        if (!client.isActive()) {
            throw new InactiveUserException();
        }

        double finalCost = calculateTotalCost(tempRent.getBeginTime(), tempRent.getEndTime(),
                                              room.getPrice(), tempRent.isBoard());
        Rent rent = new Rent(tempRent.getBeginTime(), tempRent.getEndTime(), tempRent.isBoard(),
                             finalCost, client, room);

        try{
            Rent created = rentCommandPort.add(rent); //synchronized method
            return created;
        } catch (Exception e) {
            throw new CreateRentException();
        }
    }


    /**
     * Method used to find rent by id
     *
     * @param id id of rent
     * @return rent
     * @throws RentNotFoundException if rent with given id was not found
     */
    public Rent getRentById(Long id) throws RentNotFoundException {
        Optional<Rent> optionalRent = rentQueryPort.getById(id);

        if (optionalRent.isEmpty()) {
            throw new RentNotFoundException();
        }
        return optionalRent.get();
    }


    /**
     * @return list of all rents in the database
     */
    public List<Rent> getAllRents() {
        return rentQueryPort.getAll();
    }


    /**
     * Method used to change board option for given rent, cost is recalculated before saving to database
     *
     * @param id id of the rent to be updated
     * @param board object containing the choice of board option (true/false)
     * @return status 200 (OK) if rent was updated, 409 (CONFLICT) otherwise
     * @throws InvalidInputException if user input is invalid
     * @throws RentNotFoundException if rent with given id does not exist
     */
    public Rent updateRentBoard(Long id, Boolean board) throws InvalidInputException, RentNotFoundException {
        if (board == null) {
            throw new InvalidInputException();
        }
        Optional<Rent> optionalRent = rentQueryPort.getById(id);

        if (optionalRent.isEmpty()) {
            throw new RentNotFoundException();
        }
        if (board == null) {
            throw new InvalidInputException();
        }
        Rent rentToModify = optionalRent.get();

        rentToModify.setBoard(board);
        double newCost = calculateTotalCost(rentToModify.getBeginTime(),
                                            rentToModify.getEndTime(),
                                            rentToModify.getRoom().getPrice(),
                                            rentToModify.isBoard());
        rentToModify.setFinalCost(newCost);

        Optional<Rent> updatedRent = rentCommandPort.update(rentToModify);
        if (updatedRent.isEmpty()) {
            throw new InvalidInputException();
        }
        return updatedRent.get();
    }

    /**
     * Method for removing future rents, archived rent will not be removed
     *
     * @param rentId id of the rent to be removed
     * @return void
     */
    public void removeRent(Long rentId) throws RemoveRentException {
        Optional<Rent> optionalRent = rentQueryPort.getById(rentId);
        if (optionalRent.isEmpty()) {
            return;
        }
        Rent rent = optionalRent.get();

        LocalDateTime now = LocalDateTime.now();
        if (rent.getBeginTime().isAfter(now)) {
            rentCommandPort.removeById(rentId);
            return;
        }
        throw new RemoveRentException();
    }

    /**
     * Private method used to calculate total cost of rent on creation or on board option update
     *
     * @param beginTime begin date of the rent
     * @param endTime end date of the rent
     * @param costPerDay room price per day
     * @param board determines if board option is chosen
     * @return total cost
     */
    private double calculateTotalCost(LocalDateTime beginTime, LocalDateTime endTime, double costPerDay, boolean board) {
        Duration duration = Duration.between(beginTime, endTime);
        if (board) {
            costPerDay += 50; //Daily board is worth 50
        }
        return Math.ceil(duration.toHours() / 24.0) * costPerDay;
    }
}
