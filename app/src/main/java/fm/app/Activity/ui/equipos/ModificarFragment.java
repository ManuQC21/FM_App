package fm.app.Activity.ui.equipos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.Activity.InicioActivity;
import fm.app.R;
import fm.app.entity.service.Empleado;
import fm.app.entity.service.Equipo;
import fm.app.entity.service.Ubicacion;
import fm.app.viewModel.EmpleadoViewModel;
import fm.app.viewModel.EquipoViewModel;
import fm.app.viewModel.UbicacionViewModel;

public class ModificarFragment extends Fragment {
    private EquipoViewModel equipoViewModel;
    private EmpleadoViewModel empleadoViewModel;
    private UbicacionViewModel ubicacionViewModel;

    private TextInputEditText txtTipoEquipo, txtCodigoPatrimonial, txtDescripcion, txtMarca, txtModelo, txtNombreEquipo, txtNumeroOrden, txtNumeroSerie, edtFechaCompra;
    private AutoCompleteTextView dropdownEstado, dropdownResponsable, dropdownUbicacion;
    private TextInputLayout txtInputTipoEquipo, txtInputCodigoPatrimonial, txtInputDescripcion, txtInputMarca, txtInputModelo, txtInputNombreDeEquipo, txtInputNumeroDeOrden, txtInputNumeroDeSerie, txtInputFechaCompra, txtInputEstado, txtInputResponsable, txtInputUbicacion;
    private Button btnModificarEquipo;
    private int equipoId;

    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipoId = getArguments().getInt("equipoId", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar, container, false);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
        empleadoViewModel = new ViewModelProvider(this).get(EmpleadoViewModel.class);
        ubicacionViewModel = new ViewModelProvider(this).get(UbicacionViewModel.class);

        // Referencia a los TextInputLayouts
        txtInputTipoEquipo = view.findViewById(R.id.txtInputTipoEquipoModificar);
        txtInputCodigoPatrimonial = view.findViewById(R.id.txtInputCodigoPatrimonialModificar);
        txtInputDescripcion = view.findViewById(R.id.txtInputDescripcionModificar);
        txtInputMarca = view.findViewById(R.id.txtInputMarcaModificar);
        txtInputModelo = view.findViewById(R.id.txtInputModeloModificar);
        txtInputNombreDeEquipo = view.findViewById(R.id.txtInputNombreDeEquipoModificar);
        txtInputNumeroDeOrden = view.findViewById(R.id.txtInputNumeroDeOrdenModificar);
        txtInputNumeroDeSerie = view.findViewById(R.id.txtInputNumeroDeSerieModificar);
        txtInputFechaCompra = view.findViewById(R.id.txtInputFechaCompraModificar);
        txtInputEstado = view.findViewById(R.id.txtInputEstadoModificar);
        txtInputResponsable = view.findViewById(R.id.txtInputResponsableModificar);
        txtInputUbicacion = view.findViewById(R.id.txtInputUbicacionModificar);

        // Referencia a los TextInputEditTexts
        txtTipoEquipo = view.findViewById(R.id.txtTipoEquipoModificar);
        txtCodigoPatrimonial = view.findViewById(R.id.txtCodigoPatrimonialModificar);
        txtDescripcion = view.findViewById(R.id.txtDescripcionModificar);
        txtMarca = view.findViewById(R.id.txtMarcaModificar);
        txtModelo = view.findViewById(R.id.txtModeloModificar);
        txtNombreEquipo = view.findViewById(R.id.txtNombreDeEquipoModificar);
        txtNumeroOrden = view.findViewById(R.id.txtNumeroDeOrdenModificar);
        txtNumeroSerie = view.findViewById(R.id.txtNumeroDeSerieModificar);
        edtFechaCompra = view.findViewById(R.id.edtFechaCompraModificar);
        dropdownEstado = view.findViewById(R.id.dropdownEstadoModificar);
        dropdownResponsable = view.findViewById(R.id.dropdownResponsableModificar);
        dropdownUbicacion = view.findViewById(R.id.dropdownUbicacionModificar);
        btnModificarEquipo = view.findViewById(R.id.btnModificarEquipo);

        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtrasModificar);
        btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        edtFechaCompra.setOnClickListener(v -> mostrarDatePickerDialog());
        btnModificarEquipo.setOnClickListener(v -> modificarEquipo());

        cargarEstados();
        cargarResponsables();
        cargarUbicaciones();
        cargarDatosEquipo();

        return view;
    }

    private void mostrarDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            edtFechaCompra.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void cargarDatosEquipo() {
        if (equipoId != -1) {
            equipoViewModel.getEquipoById(equipoId).observe(getViewLifecycleOwner(), response -> {
                if (response != null && response.getRpta() == 1 && response.getBody() != null) {
                    Equipo equipo = response.getBody();
                    txtTipoEquipo.setText(equipo.getTipoEquipo());
                    txtCodigoPatrimonial.setText(equipo.getCodigoPatrimonial());
                    txtDescripcion.setText(equipo.getDescripcion());
                    txtMarca.setText(equipo.getMarca());
                    txtModelo.setText(equipo.getModelo());
                    txtNombreEquipo.setText(equipo.getNombreEquipo());
                    txtNumeroOrden.setText(equipo.getNumeroOrden());
                    txtNumeroSerie.setText(equipo.getSerie());
                    if (equipo.getFechaCompra() != null && !equipo.getFechaCompra().isEmpty()) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            Date fecha = sdf.parse(equipo.getFechaCompra());
                            edtFechaCompra.setText(sdf.format(fecha));
                        } catch (ParseException e) {
                            showErrorAlert("Error al parsear la fecha");
                        }
                    }
                    dropdownEstado.setText(equipo.getEstado(), false);
                    if (equipo.getResponsable() != null) {
                        dropdownResponsable.setText(equipo.getResponsable().getNombre(), false);
                    }
                    if (equipo.getUbicacion() != null) {
                        dropdownUbicacion.setText(equipo.getUbicacion().getAmbiente(), false);
                    }
                } else {
                    showErrorAlert("Error al cargar datos del equipo");
                }
            });
        }
    }

    private void modificarEquipo() {
        Equipo equipo = new Equipo();
        equipo.setId(equipoId);
        equipo.setTipoEquipo(txtTipoEquipo.getText().toString());
        equipo.setCodigoPatrimonial(txtCodigoPatrimonial.getText().toString());
        equipo.setDescripcion(txtDescripcion.getText().toString());
        equipo.setEstado(dropdownEstado.getText().toString());
        equipo.setFechaCompra(edtFechaCompra.getText().toString());
        equipo.setMarca(txtMarca.getText().toString());
        equipo.setModelo(txtModelo.getText().toString());
        equipo.setNombreEquipo(txtNombreEquipo.getText().toString());
        equipo.setNumeroOrden(txtNumeroOrden.getText().toString());
        equipo.setSerie(txtNumeroSerie.getText().toString());
        String nombreResponsable = dropdownResponsable.getText().toString();
        String ambienteUbicacion = dropdownUbicacion.getText().toString();

        StringBuilder mensajeError = new StringBuilder();
        if (!validarCampos(equipo, mensajeError)) {
            String mensaje = "Le falta completar:\n" + mensajeError.toString();
            showWarningAlert(mensaje);
            return;
        }

        // Si todos los campos son válidos, procede con las demás operaciones
        empleadoViewModel.listarEmpleados().observe(getViewLifecycleOwner(), empleadoResponse -> {
            if (empleadoResponse != null && empleadoResponse.getRpta() == 1) {
                for (Empleado empleado : empleadoResponse.getBody()) {
                    if (empleado.getNombre().equals(nombreResponsable)) {
                        equipo.setResponsable(empleado);
                        break;
                    }
                }
            }

            ubicacionViewModel.listarUbicaciones().observe(getViewLifecycleOwner(), ubicacionResponse -> {
                if (ubicacionResponse != null && ubicacionResponse.getRpta() == 1) {
                    for (Ubicacion ubicacion : ubicacionResponse.getBody()) {
                        if (ubicacion.getAmbiente().equals(ambienteUbicacion)) {
                            equipo.setUbicacion(ubicacion);
                            break;
                        }
                    }

                    equipoViewModel.updateEquipo(equipo).observe(getViewLifecycleOwner(), updateResponse -> {
                        if (updateResponse != null && updateResponse.getRpta() == 1) {
                            showSuccessAlertAndClose("Equipo modificado con éxito");
                        } else {
                            showErrorAlert("Error al modificar equipo: " + (updateResponse != null ? updateResponse.getMessage() : "Error desconocido"));
                        }
                    });
                }
            });
        });
    }

    private boolean validarCampos(Equipo equipo, StringBuilder mensajeError) {
        boolean esValido = true;

        if (equipo.getTipoEquipo() == null || equipo.getTipoEquipo().isEmpty()) {
            mensajeError.append("- Falta tipo de equipo.\n");
            esValido = false;
        }
        if (equipo.getCodigoPatrimonial() == null || equipo.getCodigoPatrimonial().isEmpty()) {
            mensajeError.append("- Falta código patrimonial.\n");
            esValido = false;
        }
        if (equipo.getDescripcion() == null || equipo.getDescripcion().isEmpty()) {
            mensajeError.append("- Falta descripción.\n");
            esValido = false;
        }
        if (equipo.getEstado() == null || equipo.getEstado().isEmpty()) {
            mensajeError.append("- Falta estado.\n");
            esValido = false;
        }
        if (equipo.getFechaCompra() == null || equipo.getFechaCompra().isEmpty()) {
            mensajeError.append("- Falta fecha de compra.\n");
            esValido = false;
        }
        if (equipo.getMarca() == null || equipo.getMarca().isEmpty()) {
            mensajeError.append("- Falta marca.\n");
            esValido = false;
        }
        if (equipo.getModelo() == null || equipo.getModelo().isEmpty()) {
            mensajeError.append("- Falta modelo.\n");
            esValido = false;
        }
        if (equipo.getNombreEquipo() == null || equipo.getNombreEquipo().isEmpty()) {
            mensajeError.append("- Falta nombre de equipo.\n");
            esValido = false;
        }
        if (equipo.getNumeroOrden() == null || equipo.getNumeroOrden().isEmpty()) {
            mensajeError.append("- Falta número de orden.\n");
            esValido = false;
        }
        if (equipo.getSerie() == null || equipo.getSerie().isEmpty()) {
            mensajeError.append("- Falta serie.\n");
            esValido = false;
        }
        if (dropdownResponsable.getText().toString() == null || dropdownResponsable.getText().toString().isEmpty()) {
            mensajeError.append("- Falta responsable.\n");
            esValido = false;
        }
        if (dropdownUbicacion.getText().toString() == null || dropdownUbicacion.getText().toString().isEmpty()) {
            mensajeError.append("- Falta ubicación.\n");
            esValido = false;
        }

        return esValido;
    }

    private void limpiarCampos() {
        txtTipoEquipo.setText("");
        txtDescripcion.setText("");
        dropdownEstado.setText("");
        edtFechaCompra.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtNombreEquipo.setText("");
        txtNumeroOrden.setText("");
        txtNumeroSerie.setText("");
        dropdownResponsable.setText("");
        dropdownUbicacion.setText("");
    }

    private void cargarEstados() {
        List<String> estados = new ArrayList<>();
        estados.add("Mal estado");
        estados.add("Estable");
        estados.add("Buen estado");
        estados.add("Requiere mantenimiento");
        estados.add("En reparación");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, estados);
        dropdownEstado.setAdapter(adapter);
    }

    private void cargarResponsables() {
        empleadoViewModel.listarEmpleados().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                List<String> nombres = new ArrayList<>();
                for (Empleado empleado : response.getBody()) {
                    nombres.add(empleado.getNombre());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombres);
                dropdownResponsable.setAdapter(adapter);
            }
        });
    }

    private void cargarUbicaciones() {
        ubicacionViewModel.listarUbicaciones().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                List<String> ubicaciones = new ArrayList<>();
                for (Ubicacion ubicacion : response.getBody()) {
                    ubicaciones.add(ubicacion.getAmbiente());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, ubicaciones);
                dropdownUbicacion.setAdapter(adapter);
            }
        });
    }

    private void showSuccessAlertAndClose(String message) {
        SweetAlertDialog successDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Éxito")
                .setContentText(message);

        successDialog.show();

        // Esperar 1 segundo antes de cerrar la alerta
        new Handler().postDelayed(() -> {
            successDialog.dismissWithAnimation();
            // Esperar 0.5 segundos después de cerrar la alerta antes de cerrar la ventana
            new Handler().postDelayed(() -> {
                limpiarCampos();
                Intent intent = new Intent(getContext(), InicioActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }, 500);
        }, 1000);
    }

    private void showErrorAlert(String message) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(message)
                .show();
    }

    private void showWarningAlert(String message) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Cuidado")
                .setContentText(message)
                .show();
    }
}
