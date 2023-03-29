package pl.lodz.p.it.tks.user.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.tks.user.exception.BaseApplicationException;
import pl.lodz.p.it.tks.user.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.user.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.user.exception.user.CreateUserException;
import pl.lodz.p.it.tks.user.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.user.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<BaseApplicationException> {
    @Override
    public Response toResponse(BaseApplicationException e) {
        if (e.getClass() == UserNotFoundException.class) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (e.getClass() == CreateUserException.class
            || e.getClass() == UpdateUserException.class) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        if (e.getClass() == InvalidInputException.class) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (e.getClass() == InactiveUserException.class
            || e.getClass() == AuthenticationException.class) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
