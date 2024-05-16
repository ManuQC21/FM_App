package fm.app.api;

import java.util.List;

import fm.app.entity.service.Equipo;
import fm.app.entity.GenericResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface EquipoApi {
    // Ruta base para todos los endpoints de la API de Equipo
    String base = "/equipo";

    // Método para agregar un equipo
    @POST(base + "/agregar")
    Call<GenericResponse<Equipo>> addEquipo(@Body Equipo equipo);

    // Método para modificar un equipo
    @PUT(base + "/modificar")
    Call<GenericResponse<Equipo>> updateEquipo(@Body Equipo equipo);

    // Método para eliminar un equipo
    @DELETE(base + "/eliminar/{id}")
    Call<GenericResponse<Void>> deleteEquipo(@Path("id") Integer id);

    // Método para listar todos los equipos
    @GET(base + "/listar")
    Call<GenericResponse<List<Equipo>>> listAllEquipos();

    // Método para buscar un equipo por código patrimonial
    @GET(base + "/buscarPorCodigoPatrimonial/{codigoPatrimonial}")
    Call<GenericResponse<Equipo>> findEquipoByCodigoPatrimonial(@Path("codigoPatrimonial") String codigoPatrimonial);
}
