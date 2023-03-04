package pl.lodz.p.it.tks.exceptionMapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.tks.exception.RuntimeApplicationException;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;

@Provider
public class UncheckedExceptionMapper implements ExceptionMapper<RuntimeApplicationException> {
    @Override
    public Response toResponse(RuntimeApplicationException e) {
        if (e.getClass() == ConstructorArgumentException.class) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
