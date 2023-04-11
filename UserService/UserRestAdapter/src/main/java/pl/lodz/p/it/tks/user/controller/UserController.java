package pl.lodz.p.it.tks.user.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.tks.user.dto.user.AdminDTO;
import pl.lodz.p.it.tks.user.dto.user.ClientDTO;
import pl.lodz.p.it.tks.user.dto.user.EmployeeDTO;
import pl.lodz.p.it.tks.user.dto.user.RegisterAdminDTO;
import pl.lodz.p.it.tks.user.dto.user.RegisterClientDTO;
import pl.lodz.p.it.tks.user.dto.user.RegisterEmployeeDTO;
import pl.lodz.p.it.tks.user.dto.user.UpdateClientDTO;
import pl.lodz.p.it.tks.user.dto.user.UpdateEmployeeDTO;
import pl.lodz.p.it.tks.user.dto.user.UserDTO;
import pl.lodz.p.it.tks.user.event.ClientCreatedEvent;
import pl.lodz.p.it.tks.user.exception.user.CreateUserException;
import pl.lodz.p.it.tks.user.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.mapper.UserMapper;
import pl.lodz.p.it.tks.user.messaging.ClientProducer;
import pl.lodz.p.it.tks.user.model.Address;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.Employee;
import pl.lodz.p.it.tks.user.model.users.User;
import pl.lodz.p.it.tks.user.ui.command.UserCommandUseCase;
import pl.lodz.p.it.tks.user.ui.query.UserQueryUseCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@Path("/users")
public class UserController {

    @Inject
    private UserCommandUseCase userCommandUseCase;

    @Inject
    private UserQueryUseCase userQueryUseCase;

    @Inject
    private UserMapper userMapper;

    @Inject
    private ClientProducer clientProducer;

    /**
     * Endpoint which is used to register new client,
     * username of client has to be unique, otherwise exception will be thrown.
     *
     * @param rcDTO object containing information of client
     *
     * @return status code
     *     201(CREATED) + saved client if registration was successful
     *     409(CONFLICT) if registration attempt was unsuccessful (could be due to not unique username)
     */
    @POST
    @Path("/clients")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public Response registerClient(@Valid RegisterClientDTO rcDTO) throws CreateUserException {
        Address address = new Address(rcDTO.getCity(), rcDTO.getStreet(), rcDTO.getNumber());

        Client client = new Client(rcDTO.getUsername(),
            rcDTO.getPassword(),
            rcDTO.getFirstName(),
            rcDTO.getLastName(),
            rcDTO.getPersonalID(),
            address);

        Client registeredClient = userCommandUseCase.registerClient(client);

        clientProducer.produce(new ClientCreatedEvent(registeredClient, false));

        return Response.status(Response.Status.CREATED)
            .entity(new ClientDTO(registeredClient))
            .build();
    }


    /**
     * Endpoint which is used to register new employee,
     * username of employee has to be unique, otherwise exception will be thrown.
     *
     * @param reDTO object containing information of client
     *
     * @return status code
     *     201(CREATED) + saved employee if registration was successful
     *     409(CONFLICT) if registration attempt was unsuccessful (could be due to not unique username)
     */
    @POST
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response registerEmployee(@Valid RegisterEmployeeDTO reDTO) throws CreateUserException {
        Employee employee = new Employee(reDTO.getUsername(),
            reDTO.getFirstName(),
            reDTO.getLastName(),
            reDTO.getPassword());

        Employee registeredEmployee = userCommandUseCase.registerEmployee(employee);
        return Response.status(Response.Status.CREATED).entity(new EmployeeDTO(registeredEmployee)).build();
    }

    @POST
    @Path("/admins")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response registerAdmin(@Valid RegisterAdminDTO raDto) throws CreateUserException {
        Admin admin = new Admin(raDto.getUsername(), raDto.getPassword());
        Admin registeredAdmin = userCommandUseCase.registerAdmin(admin);
        return Response.status(Response.Status.CREATED)
            .entity(new AdminDTO(registeredAdmin))
            .build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response getUserById(@PathParam("id") UUID id) throws UserNotFoundException {
        User user = userQueryUseCase.getUserById(id);
        return Response.status(Response.Status.OK).entity(userMapper.mapToDto(user)).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response getAllUsers(@QueryParam("username") String username) {
        List<UserDTO> users = userQueryUseCase.getAllUsers(username)
            .stream()
            .map(user -> userMapper.mapToDto(user))
            .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Path("/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClients(@QueryParam("username") String username) {
        List<ClientDTO> clients = userQueryUseCase.getClients(username)
            .stream()
            .map(ClientDTO::new)
            .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(clients).build();
    }

    @GET
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getAllEmployees() {
        List<EmployeeDTO> employees = userQueryUseCase.getEmployees()
            .stream()
            .map(EmployeeDTO::new)
            .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(employees).build();
    }

    @GET
    @Path("/admins")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getAllAdmins() {
        List<AdminDTO> admins = userQueryUseCase.getAdmins()
            .stream()
            .map(AdminDTO::new)
            .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(admins).build();
    }

    @GET
    @Path("/search/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getUserByUsername(@PathParam("username") String username) throws UserNotFoundException {
        User user = userQueryUseCase.getUserByUsername(username);
        return Response.status(Response.Status.OK).entity(userMapper.mapToDto(user)).build();
    }


    /**
     * Endpoint used for updating given user.
     *
     * @param dto object containing new properties of user
     *
     * @return status code
     *     <ul>
     *         <li>200(OK) if update was successful</li>
     *         <li>409(CONFLICT) if update was unsuccessful (could be due to new username not being unique)</li>
     *     </ul>
     */
    @PUT
    @Path("clients/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response updateClient(@PathParam("id") UUID id, @Valid UpdateClientDTO dto)
        throws UserNotFoundException, UpdateUserException {
        Address address = new Address(dto.getCity(),
            dto.getStreet(),
            dto.getNumber() == null ? 0 : dto.getNumber());

        User user = new Client(dto.getFirstName(),
            dto.getLastName(),
            dto.getPersonalId(),
            address);

        User updatedUser = userCommandUseCase.updateUser(id, user);

        clientProducer.produce(new ClientCreatedEvent((Client) updatedUser, true));

        return Response.status(Response.Status.OK).entity(userMapper.mapToDto(updatedUser)).build();
    }

    @PUT
    @Path("employees/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response updateEmployee(@PathParam("id") UUID id, @Valid UpdateEmployeeDTO dto)
        throws UserNotFoundException, UpdateUserException {
        User user = new Employee(dto.getFirstName(),
            dto.getLastName());

        User updatedUser = userCommandUseCase.updateUser(id, user);
        return Response.status(Response.Status.OK).entity(userMapper.mapToDto(updatedUser)).build();
    }

    /**
     * Endpoint used for activating given user.
     *
     * @param id id of the user
     *
     * @return status code
     *     <ul>
     *         <li>200(OK) if activation was successful</li>
     *         <li>409(CONFLICT) if activation was unsuccessful</li>
     *     </ul>
     */
    @PUT
    @Path("/{id}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response activateUser(@PathParam("id") UUID id) throws UserNotFoundException, UpdateUserException {
        User user = userCommandUseCase.activateUser(id);
        return Response.status(Response.Status.OK).entity(userMapper.mapToDto(user)).build();
    }


    /**
     * Endpoint used for deactivating given user.
     *
     * @param id id of the user
     *
     * @return status code
     *     <ul>
     *         <li>200(OK) if deactivation was successful</li>
     *         <li>409(CONFLICT) if deactivation was unsuccessful</li>
     *     </ul>
     */
    @PUT
    @Path("/{id}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response deactivateUser(@PathParam("id") UUID id) throws UserNotFoundException, UpdateUserException {
        User user = userCommandUseCase.deactivateUser(id);
        return Response.status(Response.Status.OK).entity(userMapper.mapToDto(user)).build();
    }
}
