package fm.app.api;

import java.util.List;

import fm.app.entity.GenericResponse;
import fm.app.entity.service.Ubicacion;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UbicacionApi {
    @GET("ubicaciones/listar")
    Call<GenericResponse<List<Ubicacion>>> listarUbicaciones();
}
