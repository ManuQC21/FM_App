package fm.app.api;

import fm.app.entity.service.Foto;
import fm.app.entity.GenericResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FotoApi {
    // Ruta base para todos los endpoints de la API de Fotos
    String base = "/fotos";

    // Método para guardar una foto
    @Multipart
    @POST(base)
    Call<GenericResponse<Foto>> save(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);
}
