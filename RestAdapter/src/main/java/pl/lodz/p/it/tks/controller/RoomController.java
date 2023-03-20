package pl.lodz.p.it.tks.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pl.lodz.p.it.tks.dto.rent.RentDTO;
import pl.lodz.p.it.tks.dto.rent.RentRoomForSelfDTO;
import pl.lodz.p.it.tks.dto.room.*;
import pl.lodz.p.it.tks.dtoMapper.RoomMapper;
import pl.lodz.p.it.tks.exception.rent.CreateRentException;
import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.ui.command.RentCommandUseCase;
import pl.lodz.p.it.tks.ui.command.RoomCommandUseCase;
import pl.lodz.p.it.tks.ui.query.RoomQueryUseCase;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@Path("/rooms")
public class RoomController {

    @Context
    private SecurityContext securityContext;

    @Inject
    private RoomCommandUseCase roomCommandUseCase;

    @Inject
    private RoomQueryUseCase roomQueryUseCase;

    @Inject
    private RentCommandUseCase rentCommandUseCase;

    @Inject
    private RoomMapper roomMapper;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response addRoom(@Valid CreateRoomDTO dto) throws CreateRoomException {
        Room room = new Room(dto.getRoomNumber(), dto.getPrice(), dto.getSize());
        room = roomCommandUseCase.addRoom(room);
        return Response.status(Response.Status.CREATED).entity(new RoomDTO(room)).build();
    }

    @POST
    @Path("/apartments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response addApartment(@Valid CreateApartmentDTO dto) throws CreateRoomException {
        Apartment apartment = new Apartment(dto.getRoomNumber(), dto.getPrice(), dto.getSize(), dto.getBalconyArea());
        apartment = roomCommandUseCase.addApartment(apartment);
        return Response.status(Response.Status.CREATED).entity(new ApartmentDTO(apartment)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("id") UUID id) throws RoomNotFoundException {
        Room room = roomQueryUseCase.getRoomById(id);
        return Response.status(Response.Status.OK).entity(roomMapper.mapToDto(room)).build();
    }

    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"CLIENT"})
    public Response rentRoomForSelf(@PathParam("id") UUID roomID, @Valid RentRoomForSelfDTO dto)
            throws UserNotFoundException, RoomNotFoundException, InactiveUserException, CreateRentException {
        Principal principal = securityContext.getUserPrincipal();
        if (principal instanceof User user) {
            UUID clientID = user.getId();
            Rent rent = new Rent(dto.getBeginTime(), dto.getEndTime(), dto.isBoard(), 0, null, null);

            rent = rentCommandUseCase.rentRoom(rent, clientID, roomID);
            return Response.status(Response.Status.CREATED).entity(new RentDTO(rent)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    /**
     * Endpoint which is used to get all saved rooms if param number is not set,
     * otherwise it will return room with given room number
     *
     * @return status code
     * 200(OK) and list of all rooms
     * 200(OK) if number parameter was set and room was found
     * 404(NOT_FOUND) if number parameter was set, but room was not found
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        List<RoomDTO> rooms = roomQueryUseCase.getAllRooms()
                .stream()
                .map(room -> roomMapper.mapToDto(room))
                .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(rooms).build();
    }

    @GET
    @Path("/search/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomByNumber(@PathParam("number") Integer number) throws RoomNotFoundException {
        Room room = roomQueryUseCase.getRoomByNumber(number);
        return Response.status(Response.Status.OK).entity(roomMapper.mapToDto(room)).build();
    }


    /**
     * Endpoint which returns list of rents of given room
     *
     * @param roomId room id
     * @param past   flag which indicates if the result will be list of past rents or future rents.
     *               If this parameter is not set, the result of the method will be list of all rents of given room
     * @return list of rents that meet given criteria
     */
    @GET
    @Path("/{roomId}/rents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRentsOfRoom(@PathParam("roomId") UUID roomId,
                                      @QueryParam("past") Boolean past) throws RoomNotFoundException {
        List<RentDTO> rents = roomQueryUseCase.getAllRentsOfRoom(roomId, past)
                .stream()
                .map(RentDTO::new)
                .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(rents).build();
    }


    /**
     * Endpoint which is used to update room properties
     *
     * @param id            id of room to be updated
     * @param dto object containing new properties of existing room
     */
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response updateRoom(@PathParam("id") UUID id,
                               @Valid UpdateRoomDTO dto) throws RoomNotFoundException, UpdateRoomException {
        Room room = roomCommandUseCase.updateRoom(id, new Room(dto.getRoomNumber(), dto.getPrice(), dto.getSize()));
        return Response.status(Response.Status.OK).entity(roomMapper.mapToDto(room)).build();
    }

    @PUT
    @Path("/apartments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response updateApartment(@PathParam("id") UUID id,
                               @Valid UpdateApartmentDTO dto) throws RoomNotFoundException, UpdateRoomException, InvalidInputException {
        Apartment apartment = roomCommandUseCase.updateApartment(id, new Apartment(dto.getRoomNumber(), dto.getPrice(), dto.getSize(), dto.getBalconyArea()));
        return Response.status(Response.Status.OK).entity(roomMapper.mapToDto(apartment)).build();
    }


    /**
     * Endpoint for removing room from database. Room can be removed only if there are no current or future rents
     *
     * @param id id of the room to be removed
     * @return status code
     * 204(NO_CONTENT) if room was removed or was not found
     * 409(CONFLICT) if there are current or future rents for room with given id
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response removeRoom(@PathParam("id") UUID id) throws RoomHasActiveReservationsException {
        roomCommandUseCase.removeRoom(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
