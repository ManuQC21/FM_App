package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fm.app.api.ConfigApi;
import fm.app.api.UsuarioApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.Global;
import fm.app.entity.service.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private static UsuarioRepository repository;
    private final UsuarioApi api;

    private UsuarioRepository() {
        this.api = ConfigApi.getUsuarioApi();
    }

    public static synchronized UsuarioRepository getInstance(){
        if (repository == null) {
            repository = new UsuarioRepository();
        }
        return repository;
    }

    private <T> void handleResponse(Call<GenericResponse<T>> call, MutableLiveData<GenericResponse<T>> liveData) {
        call.enqueue(new Callback<GenericResponse<T>>() {
            @Override
            public void onResponse(Call<GenericResponse<T>> call, Response<GenericResponse<T>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<T>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Conexión fallida: " + t.getMessage(), null));
            }
        });
    }

    public LiveData<GenericResponse<Usuario>> login(String correo, String clave){
        MutableLiveData<GenericResponse<Usuario>> liveData = new MutableLiveData<>();
        handleResponse(api.login(correo, clave), liveData);
        return liveData;
    }
}
