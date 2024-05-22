package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import fm.app.api.ConfigApi;
import fm.app.api.UsuarioApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private static UsuarioRepository repository;
    private final UsuarioApi api;

    public UsuarioRepository() {
        this.api = ConfigApi.getUsuarioApi();
    }

    public static synchronized UsuarioRepository getInstance(){
        if (repository == null) {
            repository = new UsuarioRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Usuario>> login(String correo, String clave){
        MutableLiveData<GenericResponse<Usuario>> liveData = new MutableLiveData<>();
        api.login(correo, clave).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    // Manejo de errores cuando la respuesta no es exitosa
                    liveData.setValue(new GenericResponse<>("error", response.code(), "Error en el login: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                // Mejora el manejo de errores para incluir más detalles
                liveData.setValue(new GenericResponse<>("error", 500, "Fallo de red: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<GenericResponse<Usuario>> save(Usuario u){
        MutableLiveData<GenericResponse<Usuario>> liveData = new MutableLiveData<>();
        api.save(u).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>("error", response.code(), "Error al guardar: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>("error", 500, "Fallo de red: " + t.getMessage(), null));
            }
        });
        return liveData;
    }
}
