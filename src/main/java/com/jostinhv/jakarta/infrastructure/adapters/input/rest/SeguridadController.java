package com.jostinhv.jakarta.infrastructure.adapters.input.rest;

import com.jostinhv.jakarta.domain.annotations.Adapter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.security.Principal;
import java.util.Set;

@Adapter(type = Adapter.AdapterType.REST)
@Path("/ruta")
public class SeguridadController {

    @Inject
    private JsonWebToken jwt;

    @GET
    @Path("/helloworld")
    public String helloWorld(@Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String caller = principal == null ? "anonymous" : principal.getName();
        return "Hello " + caller;
    }

    @GET
    @Path("/protegido")
    @RolesAllowed({"admin"})
    public String protectedEndpoint() {
        return "Acceso autorizado";
    }

    @GET()
    @Path("/saludo")
    @RolesAllowed({"user"})
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        Set<String> roles = jwt.getClaim("groups");
        StringBuilder rolesString = new StringBuilder();
        for (String role : roles) {
            rolesString.append(role).append(" ");
        }
        return String.format("Hola + %s, hasJWT: %s, roles: %s", name, hasJWT, rolesString);
    }

}

