package fm.app.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Empleado;
import fm.app.repository.EmpleadoRepository;
import java.util.List;

public class EmpleadoViewModel extends AndroidViewModel {

    private EmpleadoRepository empleadoRepository;

    public EmpleadoViewModel(@NonNull Application application) {
        super(application);
        empleadoRepository = new EmpleadoRepository();
    }

    public LiveData<GenericResponse<List<Empleado>>> listarEmpleados() {
        return empleadoRepository.listarEmpleados();
    }
}
