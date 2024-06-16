package fm.app.api;

import fm.app.entity.GenericResponse;
import fm.app.entity.service.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface UsuarioApi {
    String BASE_PATH = "/usuario";

    @FormUrlEncoded
    @POST(BASE_PATH + "/login")
    Call<GenericResponse<Usuario>> login(@Field("correo") String correo, @Field("clave") String clave);
}
