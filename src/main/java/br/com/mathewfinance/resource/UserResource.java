package br.com.mathewfinance.resource;

import br.com.mathewfinance.dto.UserRequestDTO;
import br.com.mathewfinance.dto.UserResponseDTO;
import br.com.mathewfinance.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/users")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    @GET
    @Path("/{id}")
    public UserResponseDTO findById(@PathParam("id") Long id) {
        return userService.findById(id);
    }

    @GET
    @Path("/search")
    public List<UserResponseDTO> findByName(@QueryParam("name") String name) {
        return userService.findByName(name);
    }

    @POST
    public Response create(@Valid UserRequestDTO dto) {
        UserResponseDTO created = userService.create(dto);
        return Response.created(URI.create("/users/" + created.id()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public UserResponseDTO update(@PathParam("id") Long id, @Valid UserRequestDTO dto) {
        return userService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.noContent().build();
    }
}
