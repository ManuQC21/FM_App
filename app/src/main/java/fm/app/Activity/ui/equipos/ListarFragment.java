package fm.app.Activity.ui.equipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fm.app.R;
import fm.app.adapter.EquipoAdapter;
import fm.app.entity.service.Equipo;
import fm.app.viewModel.EquipoViewModel;

public class ListarFragment extends Fragment {

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

        Toolbar toolbar = view.findViewById(R.id.toolbarListar);
        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                List<Equipo> equipos = response.getBody();
                equipoAdapter = new EquipoAdapter(equipos);
                recyclerViewEquipos.setAdapter(equipoAdapter);
            }
        });

        return view;
    }
}