package com.jostinhv.jakarta.infrastructure.adapters.input.rest;


import com.jostinhv.jakarta.application.dto.request.LoginRequest;
import com.jostinhv.jakarta.application.dto.request.RegisterRequest;
import com.jostinhv.jakarta.application.dto.response.ApiResponse;
import com.jostinhv.jakarta.application.dto.response.TokenResponse;
import com.jostinhv.jakarta.application.dto.response.UsuarioResponse;
import com.jostinhv.jakarta.application.ports.input.AutenticarUseCase;
import com.jostinhv.jakarta.application.ports.input.RegistrarUseCase;
import com.jostinhv.jakarta.domain.annotations.Adapter;
import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.infrastructure.annotations.RateLimit;
import com.jostinhv.jakarta.infrastructure.mapper.UsuarioMapper;
import com.jostinhv.jakarta.infrastructure.utils.JwtUtil;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Adapter(type = Adapter.AdapterType.REST)
@Path("/autenticacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutenticacionController {

    @Inject
    private RegistrarUseCase registrarUseCase;

    @Inject
    private AutenticarUseCase autenticarUseCase;

    @Inject
    private JwtUtil jwtUtil;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        Optional<Usuario> newUser = registrarUseCase.registrar(request.getUsername(), request.getPassword());

        if (newUser.isPresent()) {

            String accessToken = jwtUtil.generateToken(newUser.get().getUsername(), newUser.get().getRolesAsString());
            String refreshToken = jwtUtil.generateRefreshToken(newUser.get().getUsername(), newUser.get().getRolesAsString());

            ApiResponse<UsuarioResponse> apiResponse = new ApiResponse<>(
                    Response.Status.CREATED.getStatusCode(),
                    "Usuario registrado exitosamente",
                    UsuarioMapper.toResponse(newUser.get()),
                    new TokenResponse(accessToken, refreshToken)
            );

            if (accessToken != null && refreshToken != null) {
                return Response.status(Response.Status.CREATED)
                        .entity(apiResponse)
                        .build();
            }

            return Response.status(Response.Status.CREATED).entity(apiResponse).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("El usuario ya existe").build();
        }
    }

    @POST
    @Path("/login")
    @RateLimit(limit = 10, minutes = 5)
    public Response login(@Valid LoginRequest request) {
        Optional<Usuario> user = autenticarUseCase.login(request.getUsername(), request.getPassword());
        if (user.isPresent()) {
            String accessToken = jwtUtil.generateToken(user.get().getUsername(), user.get().getRolesAsString());
            String refreshToken = jwtUtil.generateRefreshToken(user.get().getUsername(), user.get().getRolesAsString());
            if (accessToken != null && refreshToken != null) {

                ApiResponse<UsuarioResponse> apiResponse = new ApiResponse<>(
                        Response.Status.OK.getStatusCode(),
                        "Inicio de sesión exitoso",
                        UsuarioMapper.toResponse(user.get()),
                        new TokenResponse(accessToken, refreshToken)
                );
                return Response.ok(apiResponse)
                        .build();
            }
        }
        ApiResponse<UsuarioResponse> apiResponse = new ApiResponse<>(
                Response.Status.UNAUTHORIZED.getStatusCode(),
                "Credenciales inválidas",
                null,
                null
        );
        return Response.status(Response.Status.UNAUTHORIZED).entity(apiResponse).build();
    }

}
