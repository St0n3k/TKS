package pl.lodz.p.it.tks.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.service.RentService;

@RequestScoped
//@Path("/rents")
public class RentController {

    @Inject
    private RentService rentService;



    /**
     * Enpoint for creating a new rent. Rent will be created if client and room exists in database, and if rent period
     * is not colliding with existing rents.
     *
     * @param createRentDTO object containing information about rent which creation will be attempted.
     * @return status code 201 (CREATED) if rent was successfully created,
     * 409 (CONFLICT) if rent could not be created due to rent time period,
     * 400 (BAD_REQUEST) if client or room do not exist in database
     */

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({"EMPLOYEE", "ADMIN"})
//    public Response rentRoom(@Valid CreateRentDTO createRentDTO)
//        throws UserNotFoundException, RoomNotFoundException, InactiveUserException, CreateRentException {
//        Rent rent = rentService.rentRoom(createRentDTO);
//        return Response.status(Response.Status.CREATED).entity(rent).build();
//    }
//
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getRentById(@PathParam("id") Long id) throws RentNotFoundException {
//        Rent rent = rentService.getRentById(id);
//        return Response.status(Response.Status.OK).entity(rent).build();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({"EMPLOYEE", "ADMIN"})
//    public Response getAllRents() {
//        List<Rent> rents = rentService.getAllRents();
//        return Response.status(Response.Status.OK).entity(rents).build();
//    }
//
//
//    /**
//     * Endpoint used to change board option for given rent, cost is recalculated before saving to database
//     *
//     * @param id id of the rent to be updated
//     * @param dto object containing the choice of board option (true/false)
//     * @return status 200 (OK) if rent was updated, 409 (CONFLICT) otherwise
//     */
//    @PATCH
//    @Path("/{id}/board")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({"EMPLOYEE", "ADMIN"})
//    public Response updateRentBoard(@PathParam("id") Long id, @Valid UpdateRentBoardDTO dto)
//        throws InvalidInputException, RentNotFoundException {
//        Rent rent = rentService.updateRentBoard(id, dto);
//        return Response.status(Response.Status.OK).entity(rent).build();
//    }
//
//    /**
//     * Endpoint for removing future rents, archived rent will not be removed
//     *
//     * @param rentId id of the rent to be removed
//     * @return status code 204 (NO_CONTENT) if rent was removed, otherwise 409 (CONFLICT)
//     */
//    @DELETE
//    @Path("/{id}")
//    @RolesAllowed({"EMPLOYEE", "ADMIN"})
//    public Response removeRent(@PathParam("id") Long rentId) throws RemoveRentException {
//        rentService.removeRent(rentId);
//        return Response.status(Response.Status.NO_CONTENT).build();
//    }
}
