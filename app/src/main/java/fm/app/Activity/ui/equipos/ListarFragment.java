package fm.app.Activity.ui.equipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fm.app.R;
import fm.app.adapter.EquipoAdapter;
import fm.app.entity.service.Equipo;
import fm.app.viewModel.EquipoViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListarFragment extends Fragment implements EquipoAdapter.OnItemClickListener {

    private EquipoViewModel equipoViewModel;
    private RecyclerView recyclerViewEquipos;
    private EquipoAdapter equipoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar, container, false);

        recyclerViewEquipos = view.findViewById(R.id.recyclerViewEquipos);
        recyclerViewEquipos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el adaptador aquí con una lista vacía o inicial
        equipoAdapter = new EquipoAdapter(new ArrayList<>(), this);
        recyclerViewEquipos.setAdapter(equipoAdapter);

        equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRpta() == 1 && response.getBody() != null) {
                equipoAdapter.updateData(response.getBody());  // Actualiza los datos en el adaptador
            }
        });

        return view;
    }

    @Override
    public void onEditClick(int equipoId) {
        ModificarFragment modificarFragment = new ModificarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("equipoId", equipoId);
        modificarFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, modificarFragment)
                .addToBackStack(null)
                .commit();
    }
}
