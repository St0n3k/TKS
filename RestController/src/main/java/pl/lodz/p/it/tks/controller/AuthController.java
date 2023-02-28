package pl.lodz.p.it.tks.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pl.lodz.p.it.tks.dto.LoginDTO;
import pl.lodz.p.it.tks.dto.LoginResponse;
import pl.lodz.p.it.tks.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.command.AuthCommandService;

@RequestScoped
@Path("/")
public class AuthController {

    @Inject
    private AuthCommandService authCommandService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public LoginResponse login(@Valid LoginDTO loginDTO)
            throws AuthenticationException, InactiveUserException {
        String jwt = authCommandService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return new LoginResponse(jwt);
    }

//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/changePassword")
//    public Response changePassword(@Valid ChangePasswordDTO dto)
//            throws UserNotFoundException, InvalidInputException {
//        authService.changePassword(dto.getOldPassword(), dto.getNewPassword());
//        return Response.ok().build();
//    }
}
