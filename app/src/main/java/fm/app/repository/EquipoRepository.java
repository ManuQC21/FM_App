package fm.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fm.app.api.ConfigApi;
import fm.app.api.EquipoApi;
import fm.app.entity.GenericResponse;
import fm.app.entity.Global;
import fm.app.entity.service.Equipo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipoRepository {

    private final EquipoApi equipoApi = ConfigApi.getEquipoApi();

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

    public LiveData<GenericResponse<Equipo>> addEquipo(Equipo equipo) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.addEquipo(equipo), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<Equipo>> updateEquipo(Equipo equipo) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.updateEquipo(equipo), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<Void>> deleteEquipo(Integer id) {
        MutableLiveData<GenericResponse<Void>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.deleteEquipo(id), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<List<Equipo>>> listAllEquipos() {
        MutableLiveData<GenericResponse<List<Equipo>>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.listAllEquipos(), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<Equipo>> getEquipoById(Integer id) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.getEquipoById(id), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<List<Equipo>>> filtroPorNombre(String nombreEquipo) {
        MutableLiveData<GenericResponse<List<Equipo>>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.filtroPorNombre(nombreEquipo), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<List<Equipo>>> filtroCodigoPatrimonial(String codigoPatrimonial) {
        MutableLiveData<GenericResponse<List<Equipo>>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.filtroCodigoPatrimonial(codigoPatrimonial), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<List<Equipo>>> filtroFechaCompraBetween(String fechaInicio, String fechaFin) {
        MutableLiveData<GenericResponse<List<Equipo>>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.filtroFechaCompraBetween(fechaInicio, fechaFin), liveData);
        return liveData;
    }

    public LiveData<GenericResponse<Equipo>> scanAndCopyBarcodeData(MultipartBody.Part file) {
        MutableLiveData<GenericResponse<Equipo>> liveData = new MutableLiveData<>();
        handleResponse(equipoApi.scanBarcode(file), liveData);
        return liveData;
    }

    public LiveData<ResponseBody> downloadExcelReport() {
        MutableLiveData<ResponseBody> liveData = new MutableLiveData<>();
        equipoApi.downloadExcelReport().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }
}
