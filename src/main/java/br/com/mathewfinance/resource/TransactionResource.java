package br.com.mathewfinance.resource;

import br.com.mathewfinance.dto.TransactionRequestDTO;
import br.com.mathewfinance.dto.TransactionResponseDTO;
import br.com.mathewfinance.model.TransactionType;
import br.com.mathewfinance.service.TransactionService;
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
import java.time.LocalDate;
import java.util.List;

@Path("/transactions")
public class TransactionResource {

    @Inject
    TransactionService transactionService;

    @GET
    public List<TransactionResponseDTO> findAll() {
        return transactionService.findAll();
    }

    @GET
    @Path("/{id}")
    public TransactionResponseDTO findById(@PathParam("id") Long id) {
        return transactionService.findById(id);
    }

    @GET
    @Path("/user/{userId}")
    public List<TransactionResponseDTO> findByUserId(@PathParam("userId") Long userId) {
        return transactionService.findByUserId(userId);
    }

    @GET
    @Path("/user/{userId}/type/{type}")
    public List<TransactionResponseDTO> findByUserIdAndType(
            @PathParam("userId") Long userId,
            @PathParam("type") TransactionType type) {
        return transactionService.findByUserIdAndType(userId, type);
    }

    @GET
    @Path("/category/{categoryId}")
    public List<TransactionResponseDTO> findByCategoryId(@PathParam("categoryId") Long categoryId) {
        return transactionService.findByCategoryId(categoryId);
    }

    @GET
    @Path("/user/{userId}/date-range")
    public List<TransactionResponseDTO> findByDateRange(
            @PathParam("userId") Long userId,
            @QueryParam("start") LocalDate start,
            @QueryParam("end") LocalDate end) {
        return transactionService.findByDateRange(userId, start, end);
    }

    @GET
    @Path("/user/{userId}/date-range/{type}")
    public List<TransactionResponseDTO> findByDateRangeAndType(
            @PathParam("userId") Long userId,
            @QueryParam("start") LocalDate start,
            @QueryParam("end") LocalDate end,
            @PathParam("type") TransactionType type) {
        return transactionService.findByUserIdAndDateRangeAndType(userId, start, end, type);
    }

    @GET
    @Path("/user/{userId}/search")
    public List<TransactionResponseDTO> findByDescription(
            @PathParam("userId") Long userId,
            @QueryParam("description") String description) {
        return transactionService.findByDescription(userId, description);
    }

    @POST
    public Response create(@Valid TransactionRequestDTO dto) {
        TransactionResponseDTO created = transactionService.create(dto);
        return Response.created(URI.create("/transactions/" + created.id()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public TransactionResponseDTO update(@PathParam("id") Long id,
                                         @Valid TransactionRequestDTO dto) {
        return transactionService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        transactionService.delete(id);
        return Response.noContent().build();
    }
}
