package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fm.app.api.ConfigApi;
import fm.app.api.EmpleadoApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Empleado;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadoRepository {

    private EmpleadoApi empleadoApi;

    public EmpleadoRepository() {
        empleadoApi = ConfigApi.getEmpleadoApi();
    }

    public LiveData<GenericResponse<List<Empleado>>> listarEmpleados() {
        MutableLiveData<GenericResponse<List<Empleado>>> liveData = new MutableLiveData<>();
        empleadoApi.listarEmpleados().enqueue(new Callback<GenericResponse<List<Empleado>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Empleado>>> call, Response<GenericResponse<List<Empleado>>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(null, -1, "Error al obtener empleados", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Empleado>>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(null, -1, t.getMessage(), null));
            }
        });
        return liveData;
    }
}
