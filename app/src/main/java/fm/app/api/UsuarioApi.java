package fm.app.api;

import fm.app.entity.GenericResponse;
import fm.app.entity.service.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface UsuarioApi {
    // Ruta base para todos los endpoints de la API de Usuario
    String base = "/usuario";

    // Método para realizar el login de usuario
    @FormUrlEncoded
    @POST(base + "/login")
    Call<GenericResponse<Usuario>> login(@Field("correo") String correo, @Field("clave") String clave);

    // Método para registrar un nuevo usuario
    @POST(base + "/registro")
    Call<GenericResponse<Usuario>> save(@Body Usuario usuario);
}
