package fm.app.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Equipo;
import fm.app.repository.EquipoRepository;
import java.util.List;

public class EquipoViewModel extends AndroidViewModel {

    private final EquipoRepository equipoRepository;

    public EquipoViewModel(@NonNull Application application) {
        super(application);
        equipoRepository = new EquipoRepository();
    }

    // Método para agregar un equipo
    public LiveData<GenericResponse<Equipo>> addEquipo(Equipo equipo) {
        return equipoRepository.addEquipo(equipo);
    }

    // Método para actualizar un equipo
    public LiveData<GenericResponse<Equipo>> updateEquipo(Equipo equipo) {
        return equipoRepository.updateEquipo(equipo);
    }

    // Método para eliminar un equipo
    public LiveData<GenericResponse<Void>> deleteEquipo(Integer id) {
        return equipoRepository.deleteEquipo(id);
    }

    // Método para listar todos los equipos
    public LiveData<GenericResponse<List<Equipo>>> listAllEquipos() {
        return equipoRepository.listAllEquipos();
    }

    // Método para buscar un equipo por su código patrimonial
    public LiveData<GenericResponse<Equipo>> findEquipoByCodigoPatrimonial(String codigoPatrimonial) {
        return equipoRepository.findEquipoByCodigoPatrimonial(codigoPatrimonial);
    }
}
