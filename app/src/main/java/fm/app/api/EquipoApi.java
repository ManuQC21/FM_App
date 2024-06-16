package fm.app.api;

import java.util.List;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Equipo;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface EquipoApi {
    String BASE_PATH = "/equipo";

    @POST(BASE_PATH + "/agregar")
    Call<GenericResponse<Equipo>> addEquipo(@Body Equipo equipo);

    @PUT(BASE_PATH + "/modificar")
    Call<GenericResponse<Equipo>> updateEquipo(@Body Equipo equipo);

    @DELETE(BASE_PATH + "/eliminar/{id}")
    Call<GenericResponse<Void>> deleteEquipo(@Path("id") Integer id);

    @GET(BASE_PATH + "/listar")
    Call<GenericResponse<List<Equipo>>> listAllEquipos();

    @GET(BASE_PATH + "/{id}")
    Call<GenericResponse<Equipo>> getEquipoById(@Path("id") Integer id);

    @GET(BASE_PATH + "/filtro/nombre")
    Call<GenericResponse<List<Equipo>>> filtroPorNombre(@Query("nombreEquipo") String nombreEquipo);

    @GET(BASE_PATH + "/filtro/codigoPatrimonial")
    Call<GenericResponse<List<Equipo>>> filtroCodigoPatrimonial(@Query("codigoPatrimonial") String codigoPatrimonial);

    @GET(BASE_PATH + "/filtro/fechaCompra")
    Call<GenericResponse<List<Equipo>>> filtroFechaCompraBetween(@Query("fechaInicio") String fechaInicio, @Query("fechaFin") String fechaFin);

    @Multipart
    @POST(BASE_PATH + "/escanearCodigoBarra")
    Call<GenericResponse<Equipo>> scanBarcode(@Part MultipartBody.Part file);

    @GET(BASE_PATH + "/descargarReporte")
    Call<ResponseBody> downloadExcelReport();
}
