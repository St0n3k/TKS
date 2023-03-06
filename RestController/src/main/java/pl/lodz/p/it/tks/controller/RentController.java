package pl.lodz.p.it.tks.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.tks.dto.CreateRentDTO;
import pl.lodz.p.it.tks.dto.UpdateRentBoardDTO;
import pl.lodz.p.it.tks.exception.rent.CreateRentException;
import pl.lodz.p.it.tks.exception.rent.RemoveRentException;
import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.ui.command.RentCommandUseCase;
import pl.lodz.p.it.tks.ui.query.RentQueryUseCase;

import java.util.List;
import java.util.UUID;

@RequestScoped
@Path("/rents")
public class RentController {

    @Inject
    private RentQueryUseCase rentQueryUseCase;

    @Inject
    private RentCommandUseCase rentCommandUseCase;

    /**
     * Enpoint for creating a new rent. Rent will be created if client and room exists in database, and if rent period
     * is not colliding with existing rents.
     *
     * @param createRentDTO object containing information about rent which creation will be attempted.
     * @return status code 201 (CREATED) if rent was successfully created,
     * 409 (CONFLICT) if rent could not be created due to rent time period,
     * 400 (BAD_REQUEST) if client or room do not exist in database
     */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public Response rentRoom(@Valid CreateRentDTO createRentDTO)
        throws UserNotFoundException, RoomNotFoundException, InactiveUserException, CreateRentException {
        Rent fromDTO = new Rent(createRentDTO.getBeginTime(),
                createRentDTO.getEndTime(),
                createRentDTO.isBoard(),
                0,
                null,
                null
        );
        Rent rent = rentCommandUseCase.rentRoom(fromDTO,
                createRentDTO.getClientId(),
                createRentDTO.getRoomId());
        return Response.status(Response.Status.CREATED).entity(rent).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentById(@PathParam("id") UUID id) throws RentNotFoundException {
        Rent rent = rentQueryUseCase.getRentById(id);
        return Response.status(Response.Status.OK).entity(rent).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public Response getAllRents() {
        List<Rent> rents = rentQueryUseCase.getAllRents();
        return Response.status(Response.Status.OK).entity(rents).build();
    }


    /**
     * Endpoint used to change board option for given rent, cost is recalculated before saving to database
     *
     * @param id id of the rent to be updated
     * @param dto object containing the choice of board option (true/false)
     * @return status 200 (OK) if rent was updated, 409 (CONFLICT) otherwise
     */
    @PATCH
    @Path("/{id}/board")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public Response updateRentBoard(@PathParam("id") UUID id, @Valid UpdateRentBoardDTO dto)
        throws InvalidInputException, RentNotFoundException {
        Rent rent = rentCommandUseCase.updateRentBoard(id, dto.getBoard());
        return Response.status(Response.Status.OK).entity(rent).build();
    }

    /**
     * Endpoint for removing future rents, archived rent will not be removed
     *
     * @param rentId id of the rent to be removed
     * @return status code 204 (NO_CONTENT) if rent was removed, otherwise 409 (CONFLICT)
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public Response removeRent(@PathParam("id") UUID rentId) throws RemoveRentException {
        rentCommandUseCase.removeRent(rentId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
