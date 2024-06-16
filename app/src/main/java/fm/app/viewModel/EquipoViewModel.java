package fm.app.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Equipo;
import fm.app.repository.EquipoRepository;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import java.util.List;

public class EquipoViewModel extends AndroidViewModel {

    private final EquipoRepository equipoRepository;

    public EquipoViewModel(@NonNull Application application) {
        super(application);
        equipoRepository = new EquipoRepository();
    }

    public LiveData<GenericResponse<Equipo>> addEquipo(Equipo equipo) { return equipoRepository.addEquipo(equipo); }
    public LiveData<GenericResponse<Equipo>> updateEquipo(Equipo equipo) { return equipoRepository.updateEquipo(equipo); }
    public LiveData<GenericResponse<Void>> deleteEquipo(Integer id) { return equipoRepository.deleteEquipo(id); }
    public LiveData<GenericResponse<List<Equipo>>> listAllEquipos() { return equipoRepository.listAllEquipos(); }
    public LiveData<GenericResponse<Equipo>> getEquipoById(Integer id) { return equipoRepository.getEquipoById(id); }
    public LiveData<GenericResponse<List<Equipo>>> filtroPorNombre(String nombreEquipo) { return equipoRepository.filtroPorNombre(nombreEquipo); }
    public LiveData<GenericResponse<List<Equipo>>> filtroCodigoPatrimonial(String codigoPatrimonial) { return equipoRepository.filtroCodigoPatrimonial(codigoPatrimonial); }
    public LiveData<GenericResponse<List<Equipo>>> filtroFechaCompraBetween(String fechaInicio, String fechaFin) { return equipoRepository.filtroFechaCompraBetween(fechaInicio, fechaFin); }
    public LiveData<GenericResponse<Equipo>> scanAndCopyBarcodeData(MultipartBody.Part file) { return equipoRepository.scanAndCopyBarcodeData(file); }
    public LiveData<ResponseBody> downloadExcelReport() { return equipoRepository.downloadExcelReport(); }
}
