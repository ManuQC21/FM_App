package fm.app.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Ubicacion;
import fm.app.repository.UbicacionRepository;
import java.util.List;

public class UbicacionViewModel extends AndroidViewModel {

    private UbicacionRepository ubicacionRepository;

    public UbicacionViewModel(@NonNull Application application) {
        super(application);
        ubicacionRepository = new UbicacionRepository();
    }

    public LiveData<GenericResponse<List<Ubicacion>>> listarUbicaciones() {
        return ubicacionRepository.listarUbicaciones();
    }
}
