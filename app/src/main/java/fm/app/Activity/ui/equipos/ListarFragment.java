package fm.app.Activity.ui.equipos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.R;
import fm.app.adapter.EquipoAdapter;
import fm.app.viewModel.EquipoViewModel;
import java.util.ArrayList;

public class ListarFragment extends Fragment implements EquipoAdapter.OnItemClickListener {

    private EquipoViewModel equipoViewModel;
    private RecyclerView recyclerViewEquipos;
    private EquipoAdapter equipoAdapter;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar, container, false);

        initViews(view);
        setupRecyclerView();
        loadEquipos();

        return view;
    }

    private void initViews(View view) {
        recyclerViewEquipos = view.findViewById(R.id.recyclerViewEquipos);
        progressBar = view.findViewById(R.id.progressBar);
        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerViewEquipos.setLayoutManager(new LinearLayoutManager(getContext()));
        equipoAdapter = new EquipoAdapter(new ArrayList<>(), this);
        recyclerViewEquipos.setAdapter(equipoAdapter);
    }

    private void loadEquipos() {
        progressBar.setVisibility(View.VISIBLE);
        equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), response -> {
            progressBar.setVisibility(View.GONE);
            if (response != null && response.getRpta() == 1 && response.getBody() != null) {
                equipoAdapter.updateData(response.getBody());
            } else {
                showErrorAlert("Error al cargar equipos", response != null ? response.getMessage() : "Error desconocido");
            }
        });
    }

    @Override
    public void onEditClick(int equipoId) {
        Log.d("ListarFragment", "Edit clicked for ID: " + equipoId);
        ModificarFragment modificarFragment = new ModificarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("equipoId", equipoId);
        modificarFragment.setArguments(bundle);
        if (isAdded()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, modificarFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onViewClick(int equipoId) {
        DetalleEquipoFragment detalleFragment = new DetalleEquipoFragment();
        Bundle args = new Bundle();
        args.putInt("equipoId", equipoId);
        detalleFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, detalleFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeleteClick(int equipoId) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Equipo")
                .setMessage("¿Estás seguro de eliminar este equipo?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    deleteEquipo(equipoId);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteEquipo(int equipoId) {
        equipoViewModel.deleteEquipo(equipoId).observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRpta() == 1) {
                showSuccessAlert("Equipo eliminado correctamente");
                refreshEquiposList();
            } else {
                showErrorAlert("Error al eliminar equipo", response != null ? response.getMessage() : "Error desconocido");
            }
        });
    }

    private void refreshEquiposList() {
        equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), equipoResponse -> {
            if (equipoResponse != null && equipoResponse.getRpta() == 1) {
                equipoAdapter.updateData(equipoResponse.getBody());
            } else {
                showErrorAlert("Error al recargar la lista de equipos", equipoResponse != null ? equipoResponse.getMessage() : "Error desconocido");
            }
        });
    }

    private void showSuccessAlert(String message) {
        SweetAlertDialog successDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Éxito")
                .setContentText(message);
        successDialog.show();
        new android.os.Handler().postDelayed(successDialog::dismissWithAnimation, 1000);
    }

    private void showErrorAlert(String title, String message) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }
}
