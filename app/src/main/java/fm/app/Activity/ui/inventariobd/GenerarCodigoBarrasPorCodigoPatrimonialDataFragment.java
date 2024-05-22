package fm.app.Activity.ui.inventariobd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fm.app.R;

public class GenerarCodigoBarrasPorCodigoPatrimonialDataFragment extends Fragment {
    private GenerarCodigoBarrasPorCodigoPatrimonialDataFragment binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generar_codigo_barras_por_codigo_patrimonial_data, container, false);
    }
}