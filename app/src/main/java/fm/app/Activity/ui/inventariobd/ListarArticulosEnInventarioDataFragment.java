package fm.app.Activity.ui.inventariobd;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fm.app.R;

public class ListarArticulosEnInventarioDataFragment extends Fragment {
    private ListarArticulosEnInventarioDataFragment binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listar_articulos_en_inventario_data, container, false);
    }
}