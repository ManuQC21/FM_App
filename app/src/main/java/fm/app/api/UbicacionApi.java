package fm.app.api;

import java.util.List;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Ubicacion;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UbicacionApi {
    String BASE_PATH = "/ubicaciones";

    @GET(BASE_PATH + "/listar")
    Call<GenericResponse<List<Ubicacion>>> listarUbicaciones();
}
