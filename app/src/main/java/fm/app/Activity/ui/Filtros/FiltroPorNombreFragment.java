package fm.app.Activity.ui.Filtros;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

import fm.app.Activity.ui.equipos.DetalleEquipoFragment;
import fm.app.Activity.ui.equipos.ModificarFragment;
import fm.app.R;
import fm.app.adapter.EquipoAdapter;
import fm.app.viewModel.EquipoViewModel;

public class FiltroPorNombreFragment extends Fragment {

    private EquipoViewModel equipoViewModel;
    private RecyclerView recyclerView;
    private EquipoAdapter equipoAdapter;
    private TextInputEditText edtBuscarNombre;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtro_por_nombre, container, false);
        edtBuscarNombre = view.findViewById(R.id.edtBuscarNombre);
        recyclerView = view.findViewById(R.id.recyclerViewFiltroNombre);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        equipoAdapter = new EquipoAdapter(new ArrayList<>(), new EquipoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int equipoId) {
                Log.d("FiltroPorNombreFragment", "Edit clicked for ID: " + equipoId);
                ModificarFragment modificarFragment = new ModificarFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("equipoId", equipoId);
                modificarFragment.setArguments(bundle);
                if (isAdded()) {
                    getActivity().getSupportFragmentManager().beginTransaction()
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
                        .replace(R.id.fragment_container, detalleFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDeleteClick(int equipoId) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Eliminar Equipo")
                        .setContentText("¿Estás seguro de eliminar este equipo?")
                        .setConfirmText("Sí")
                        .setConfirmClickListener(sDialog -> {
                            equipoViewModel.deleteEquipo(equipoId).observe(getViewLifecycleOwner(), response -> {
                                if (response != null && response.getRpta() == 1) {
                                    sDialog
                                            .setTitleText("Eliminado")
                                            .setContentText("Equipo eliminado correctamente")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), equipoResponse -> {
                                        if (equipoResponse != null && equipoResponse.getRpta() == 1) {
                                            equipoAdapter.updateData(equipoResponse.getBody());
                                        }
                                    });
                                } else {
                                    sDialog
                                            .setTitleText("Error")
                                            .setContentText("Error al eliminar equipo: " + (response != null ? response.getMessage() : "Error desconocido"))
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            });
                        })
                        .setCancelButton("No", SweetAlertDialog::dismissWithAnimation)
                        .show();
            }
        });
        recyclerView.setAdapter(equipoAdapter);
        setupEditText();
        return view;
    }

    private void setupEditText() {
        edtBuscarNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> equipoViewModel.filtroPorNombre(s.toString()).observe(getViewLifecycleOwner(), response -> {
                    if (response != null && response.getBody() != null) {
                        equipoAdapter.updateData(response.getBody());
                    } else {
                        equipoAdapter.updateData(new ArrayList<>());
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Sin resultados")
                                .setContentText("No se encontraron datos para el nombre ingresado")
                                .show();
                    }
                });
                handler.postDelayed(searchRunnable, 500); // 0.5 segundos
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);

        // Cargar todos los equipos al iniciar el Fragmento
        equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getBody() != null) {
                equipoAdapter.updateData(response.getBody());
            } else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Error al cargar los datos iniciales")
                        .show();
            }
        });
    }
}
