package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import fm.app.api.ConfigApi;
import fm.app.api.EquipoApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Equipo;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipoRepository {

    private final EquipoApi equipoApi;

    public EquipoRepository() {
        equipoApi = ConfigApi.getEquipoApi();
    }

    // Agregar equipo
    public LiveData<GenericResponse<Equipo>> addEquipo(Equipo equipo) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        equipoApi.addEquipo(equipo).enqueue(new Callback<GenericResponse<Equipo>>() {
            @Override
            public void onResponse(Call<GenericResponse<Equipo>> call, Response<GenericResponse<Equipo>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(null, -1, "Error al agregar equipo", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Equipo>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Modificar equipo
    public LiveData<GenericResponse<Equipo>> updateEquipo(Equipo equipo) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        equipoApi.updateEquipo(equipo).enqueue(new Callback<GenericResponse<Equipo>>() {
            @Override
            public void onResponse(Call<GenericResponse<Equipo>> call, Response<GenericResponse<Equipo>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(null, -1, "Error al modificar equipo", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Equipo>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Eliminar equipo
    public LiveData<GenericResponse<Void>> deleteEquipo(Integer id) {
        MutableLiveData<GenericResponse<Void>> liveData = new MutableLiveData<>();
        equipoApi.deleteEquipo(id).enqueue(new Callback<GenericResponse<Void>>() {
            @Override
            public void onResponse(Call<GenericResponse<Void>> call, Response<GenericResponse<Void>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(null, -1, "Error al eliminar equipo", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Void>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Listar todos los equipos
    public LiveData<GenericResponse<List<Equipo>>> listAllEquipos() {
        MutableLiveData<GenericResponse<List<Equipo>>> liveData = new MutableLiveData<>();
        equipoApi.listAllEquipos().enqueue(new Callback<GenericResponse<List<Equipo>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Equipo>>> call, Response<GenericResponse<List<Equipo>>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(null, -1, "Error al listar equipos", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Equipo>>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Buscar un equipo por código patrimonial
    public LiveData<GenericResponse<Equipo>> findEquipoByCodigoPatrimonial(String codigoPatrimonial) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        equipoApi.findEquipoByCodigoPatrimonial(codigoPatrimonial).enqueue(new Callback<GenericResponse<Equipo>>() {
            @Override
            public void onResponse(Call<GenericResponse<Equipo>> call, Response<GenericResponse<Equipo>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new GenericResponse<>(null, -1, "Error al buscar equipo", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Equipo>> call, Throwable t) {
                liveData.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return liveData;
    }
}