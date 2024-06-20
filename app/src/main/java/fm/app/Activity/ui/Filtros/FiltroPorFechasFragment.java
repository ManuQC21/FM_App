package fm.app.Activity.ui.Filtros;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Calendar;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.Activity.ui.equipos.DetalleEquipoFragment;
import fm.app.Activity.ui.equipos.ModificarFragment;
import fm.app.R;
import fm.app.adapter.EquipoAdapter;
import fm.app.viewModel.EquipoViewModel;

public class FiltroPorFechasFragment extends Fragment {

    private EquipoViewModel equipoViewModel;
    private RecyclerView recyclerView;
    private EquipoAdapter equipoAdapter;
    private TextInputEditText edtFechaInicio, edtFechaFin;
    private Button btnFiltrar;
    private DatePickerDialog datePickerDialogInicio, datePickerDialogFin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtro_por_fechas, container, false);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        setupUI(view);
        setupDatePickerDialogs();
        loadInitialData();
        return view;
    }

    private void setupUI(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewFiltroFechas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        edtFechaInicio = view.findViewById(R.id.edtFechaInicio);
        edtFechaFin = view.findViewById(R.id.edtFechaFin);
        btnFiltrar = view.findViewById(R.id.btnFiltrarFechas);

        btnFiltrar.setOnClickListener(v -> applyDateFilter());

        edtFechaInicio.setOnClickListener(v -> datePickerDialogInicio.show());
        edtFechaFin.setOnClickListener(v -> datePickerDialogFin.show());

        equipoAdapter = new EquipoAdapter(new ArrayList<>(), new EquipoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int equipoId) {
                Log.d("FiltroPorFechasFragment", "Edit clicked for ID: " + equipoId);
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
    }

    private void setupDatePickerDialogs() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialogInicio = new DatePickerDialog(getContext(), this::setStartDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialogFin = new DatePickerDialog(getContext(), this::setEndDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setStartDate(DatePicker view, int year, int month, int dayOfMonth) {
        edtFechaInicio.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));
    }

    private void setEndDate(DatePicker view, int year, int month, int dayOfMonth) {
        edtFechaFin.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));
    }

    private void loadInitialData() {
        equipoViewModel.listAllEquipos().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRpta() == 1) {
                equipoAdapter.updateData(response.getBody());
            } else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Error al cargar los datos iniciales")
                        .show();
            }
        });
    }

    private void applyDateFilter() {
        String startDate = edtFechaInicio.getText().toString().trim();
        String endDate = edtFechaFin.getText().toString().trim();
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            equipoViewModel.filtroFechaRevisionBetween(startDate, endDate).observe(getViewLifecycleOwner(), response -> {
                if (response != null && response.getRpta() == 1) {
                    equipoAdapter.updateData(response.getBody());
                } else {
                    equipoAdapter.updateData(new ArrayList<>());
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sin resultados")
                            .setContentText("No se encontraron datos para las fechas seleccionadas")
                            .show();
                }
            });
        } else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Fechas incompletas")
                    .setContentText("Por favor, seleccione ambas fechas de inicio y fin")
                    .show();
        }
    }
}
