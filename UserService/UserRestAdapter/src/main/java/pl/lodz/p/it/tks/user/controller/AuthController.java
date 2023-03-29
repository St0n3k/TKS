package pl.lodz.p.it.tks.user.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.tks.user.dto.auth.ChangePasswordDTO;
import pl.lodz.p.it.tks.user.dto.auth.LoginDTO;
import pl.lodz.p.it.tks.user.dto.auth.LoginResponse;
import pl.lodz.p.it.tks.user.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.user.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.user.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.service.AuthService;

@RequestScoped
@Path("/")
public class AuthController {

    @Inject
    private AuthService authService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public LoginResponse login(@Valid LoginDTO loginDTO)
        throws AuthenticationException, InactiveUserException {
        String jwt = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return new LoginResponse(jwt);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/changePassword")
    public Response changePassword(@Valid ChangePasswordDTO dto)
        throws UserNotFoundException, InvalidInputException {
        authService.changePassword(dto.getOldPassword(), dto.getNewPassword());
        return Response.ok().build();
    }

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.noContent().build();
    }
}
