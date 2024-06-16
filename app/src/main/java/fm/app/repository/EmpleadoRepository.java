package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fm.app.api.ConfigApi;
import fm.app.api.EmpleadoApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Empleado;
import fm.app.entity.Global;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadoRepository {

    private final EmpleadoApi empleadoApi = ConfigApi.getEmpleadoApi();

    // Método genérico para manejar respuestas de Retrofit
    private <T> void handleResponse(Call<GenericResponse<T>> call, MutableLiveData<GenericResponse<T>> liveData) {
        call.enqueue(new Callback<GenericResponse<T>>() {
            @Override
            public void onResponse(Call<GenericResponse<T>> call, Response<GenericResponse<T>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Error al obtener empleados: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<T>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, "Conexión fallida: " + t.getMessage(), null));
            }
        });
    }

    public LiveData<GenericResponse<List<Empleado>>> listarEmpleados() {
        MutableLiveData<GenericResponse<List<Empleado>>> liveData = new MutableLiveData<>();
        handleResponse(empleadoApi.listarEmpleados(), liveData);
        return liveData;
    }
}
