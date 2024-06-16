package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fm.app.api.ConfigApi;
import fm.app.api.UbicacionApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.Global;
import fm.app.entity.service.Ubicacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbicacionRepository {

    private final UbicacionApi ubicacionApi = ConfigApi.getUbicacionApi();

    // Método genérico para manejar respuestas de Retrofit
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

    public LiveData<GenericResponse<List<Ubicacion>>> listarUbicaciones() {
        MutableLiveData<GenericResponse<List<Ubicacion>>> liveData = new MutableLiveData<>();
        handleResponse(ubicacionApi.listarUbicaciones(), liveData);
        return liveData;
    }
}
