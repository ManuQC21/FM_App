package fm.app.Activity.ui.inventariobd;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fm.app.R;

public class AgregarArticuloAlInventarioDataFragment extends Fragment {
    private AgregarArticuloAlInventarioDataFragment binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_articulo_al_inventario_data, container, false);
    }
}