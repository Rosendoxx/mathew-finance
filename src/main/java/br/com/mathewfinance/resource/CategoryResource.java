package br.com.mathewfinance.resource;

import br.com.mathewfinance.dto.CategoryRequestDTO;
import br.com.mathewfinance.dto.CategoryResponseDTO;
import br.com.mathewfinance.model.CategoryType;
import br.com.mathewfinance.service.CategoryService;
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

@Path("/categories")
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    public List<CategoryResponseDTO> findAll() {
        return categoryService.findAll();
    }

    @GET
    @Path("/{id}")
    public CategoryResponseDTO findById(@PathParam("id") Long id) {
        return categoryService.findById(id);
    }

    @GET
    @Path("/user/{userId}")
    public List<CategoryResponseDTO> findByUserId(@PathParam("userId") Long userId) {
        return categoryService.findByUserId(userId);
    }

    @GET
    @Path("/search")
    public List<CategoryResponseDTO> findByName(@QueryParam("name") String name) {
        return categoryService.findByName(name);
    }

    @GET
    @Path("/type/{type}")
    public List<CategoryResponseDTO> findByType(@PathParam("type") CategoryType type) {
        return categoryService.findByType(type);
    }

    @POST
    public Response create(@Valid CategoryRequestDTO dto) {
        CategoryResponseDTO created = categoryService.create(dto);
        return Response.created(URI.create("/categories/" + created.id()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public CategoryResponseDTO update(@PathParam("id") Long id, @Valid CategoryRequestDTO dto) {
        return categoryService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        categoryService.delete(id);
        return Response.noContent().build();
    }
}
