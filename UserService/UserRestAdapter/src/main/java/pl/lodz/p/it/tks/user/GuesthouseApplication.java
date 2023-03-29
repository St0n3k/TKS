package pl.lodz.p.it.tks.user;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
@DeclareRoles({"ADMIN", "EMPLOYEE", "CLIENT", "GUEST"})
public class GuesthouseApplication extends Application {
}
