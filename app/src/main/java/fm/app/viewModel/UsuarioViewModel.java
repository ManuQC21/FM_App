package fm.app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fm.app.entity.GenericResponse;
import fm.app.entity.service.Usuario;
import fm.app.repository.UsuarioRepository;

/**
 * ViewModel para manejar la lógica de UI relacionada con los usuarios.
 */
public class UsuarioViewModel extends AndroidViewModel {
    private final UsuarioRepository repository;

    /**
     * Constructor del ViewModel.
     * @param application Instancia de la aplicación para contexto global.
     */
    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        this.repository = UsuarioRepository.getInstance();
    }

    /**
     * Inicia sesión de un usuario con correo y clave.
     * @param correo Correo del usuario.
     * @param clave Clave del usuario.
     * @return LiveData con la respuesta del servidor.
     */
    public LiveData<GenericResponse<Usuario>> login(String correo, String clave){
        return repository.login(correo, clave);
    }

    /**
     * Registra un nuevo usuario.
     * @param u Objeto Usuario a guardar.
     * @return LiveData con la respuesta del servidor.
     */
    public LiveData<GenericResponse<Usuario>> save(Usuario u){
        return repository.save(u);
    }
}
