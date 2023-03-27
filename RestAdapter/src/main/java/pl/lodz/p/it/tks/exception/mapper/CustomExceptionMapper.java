package pl.lodz.p.it.tks.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.tks.exception.BaseApplicationException;
import pl.lodz.p.it.tks.exception.rent.CreateRentException;
import pl.lodz.p.it.tks.exception.rent.RemoveRentException;
import pl.lodz.p.it.tks.exception.rent.RentNotFoundException;
import pl.lodz.p.it.tks.exception.rent.UpdateRentException;
import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.exception.user.CreateUserException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<BaseApplicationException> {
    @Override
    public Response toResponse(BaseApplicationException e) {
        if (e.getClass() == UserNotFoundException.class
            || e.getClass() == RoomNotFoundException.class
            || e.getClass() == RentNotFoundException.class) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (e.getClass() == CreateRoomException.class
            || e.getClass() == RoomHasActiveReservationsException.class
            || e.getClass() == UpdateRoomException.class
            || e.getClass() == CreateRentException.class
            || e.getClass() == UpdateRentException.class
            || e.getClass() == RemoveRentException.class
            || e.getClass() == CreateUserException.class
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
