package fm.app.Activity.ui.equipos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.Activity.InicioActivity;
import fm.app.R;
import fm.app.databinding.FragmentAgregarBinding;
import fm.app.entity.service.Empleado;
import fm.app.entity.service.Equipo;
import fm.app.entity.service.Ubicacion;
import fm.app.viewModel.EmpleadoViewModel;
import fm.app.viewModel.EquipoViewModel;
import fm.app.viewModel.UbicacionViewModel;

public class AgregarFragment extends Fragment {
    private EquipoViewModel equipoViewModel;
    private TextInputEditText txtTipoEquipo, txtCodigoPatrimonial, txtDescripcion, txtMarca, txtModelo, txtNombreDeEquipo, txtNumeroDeOrden, txtNumeroDeSerie, edtFechaCompra;
    private AutoCompleteTextView dropdownEstado, dropdownResponsable, dropdownUbicacion;
    private TextInputLayout txtInputTipoEquipo, txtInputCodigoPatrimonial, txtInputDescripcion, txtInputMarca, txtInputModelo, txtInputNombreDeEquipo, txtInputNumeroDeOrden, txtInputNumeroDeSerie, txtInputFechaCompra, txtInputEstado, txtInputResponsable, txtInputUbicacion;
    private Button btnAgregarEquipo;
    private FragmentAgregarBinding binding;
    private EmpleadoViewModel empleadoViewModel;
    private UbicacionViewModel ubicacionViewModel;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        empleadoViewModel = new ViewModelProvider(this).get(EmpleadoViewModel.class);
        ubicacionViewModel = new ViewModelProvider(this).get(UbicacionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgregarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);

        // Referencia a los TextInputLayouts
        txtInputTipoEquipo = view.findViewById(R.id.txtInputTipoEquipo);
        txtInputCodigoPatrimonial = view.findViewById(R.id.txtInputCodigoPatrimonial);
        txtInputDescripcion = view.findViewById(R.id.txtInputDescripcion);
        txtInputMarca = view.findViewById(R.id.txtInputMarca);
        txtInputModelo = view.findViewById(R.id.txtInputModelo);
        txtInputNombreDeEquipo = view.findViewById(R.id.txtInputNombreDeEquipo);
        txtInputNumeroDeOrden = view.findViewById(R.id.txtInputNumeroDeOrden);
        txtInputNumeroDeSerie = view.findViewById(R.id.txtInputNumeroDeSerie);
        txtInputFechaCompra = view.findViewById(R.id.txtInputFechaCompra);
        txtInputEstado = view.findViewById(R.id.txtInputEstado);
        txtInputResponsable = view.findViewById(R.id.txtInputResponsable);
        txtInputUbicacion = view.findViewById(R.id.txtInputUbicacion);

        // Referencia a los TextInputEditTexts
        txtTipoEquipo = view.findViewById(R.id.txtTipoEquipo);
        txtCodigoPatrimonial = view.findViewById(R.id.txtCodigoPatrimonial);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtMarca = view.findViewById(R.id.txtMarca);
        txtModelo = view.findViewById(R.id.txtModelo);
        txtNombreDeEquipo = view.findViewById(R.id.txtNombreDeEquipo);
        txtNumeroDeOrden = view.findViewById(R.id.txtNumeroDeOrden);
        txtNumeroDeSerie = view.findViewById(R.id.txtNumeroDeSerie);
        edtFechaCompra = view.findViewById(R.id.edtFechaCompra);
        dropdownEstado = view.findViewById(R.id.dropdownEstado);
        dropdownResponsable = view.findViewById(R.id.dropdownResponsable);
        dropdownUbicacion = view.findViewById(R.id.dropdownUbicacion);
        btnAgregarEquipo = view.findViewById(R.id.btnAgregarEquipo);

        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        edtFechaCompra.setOnClickListener(v -> mostrarDatePickerDialog());
        btnAgregarEquipo.setOnClickListener(v -> agregarEquipo());

        cargarResponsables();
        cargarUbicaciones();
        cargarEstados();

        aplicarAnimaciones(view);

        return view;
    }

    private void aplicarAnimaciones(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim));
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

    private void agregarEquipo() {
        Equipo equipo = new Equipo();
        equipo.setTipoEquipo(txtTipoEquipo.getText().toString());
        equipo.setCodigoPatrimonial(txtCodigoPatrimonial.getText().toString());
        equipo.setDescripcion(txtDescripcion.getText().toString());
        equipo.setEstado(dropdownEstado.getText().toString());
        equipo.setFechaCompra(edtFechaCompra.getText().toString());
        equipo.setMarca(txtMarca.getText().toString());
        equipo.setModelo(txtModelo.getText().toString());
        equipo.setNombreEquipo(txtNombreDeEquipo.getText().toString());
        equipo.setNumeroOrden(txtNumeroDeOrden.getText().toString());
        equipo.setSerie(txtNumeroDeSerie.getText().toString());
        String nombreResponsable = dropdownResponsable.getText().toString();
        String ambienteUbicacion = dropdownUbicacion.getText().toString();

        StringBuilder mensajeError = new StringBuilder();
        if (!validarCampos(equipo, mensajeError)) {
            String mensaje = "Le falta completar:\n" + mensajeError.toString().replace("\n", "\n- ");
            showWarningAlert(mensaje);
            return;
        }

        // Si todos los campos son válidos, procede con las demás operaciones
        empleadoViewModel.listarEmpleados().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                for (Empleado empleado : response.getBody()) {
                    if (empleado.getNombre().equals(nombreResponsable)) {
                        equipo.setResponsable(empleado);
                        break;
                    }
                }
            }

            ubicacionViewModel.listarUbicaciones().observe(getViewLifecycleOwner(), ubicacionResponse -> {
                if (ubicacionResponse.getRpta() == 1) {
                    for (Ubicacion ubicacion : ubicacionResponse.getBody()) {
                        if (ubicacion.getAmbiente().equals(ambienteUbicacion)) {
                            equipo.setUbicacion(ubicacion);
                            break;
                        }
                    }
                }

                equipoViewModel.addEquipo(equipo).observe(getViewLifecycleOwner(), equipoResponse -> {
                    if (equipoResponse.getRpta() == 1) {
                        showSuccessAlertAndClose("Equipo agregado con éxito");
                    } else {
                        showErrorAlert("Error al agregar equipo");
                    }
                });
            });
        });
    }

    private boolean validarCampos(Equipo equipo, StringBuilder mensajeError) {
        boolean esValido = true;

        if (equipo.getTipoEquipo().isEmpty()) {
            mensajeError.append("Falta tipo de equipo.\n");
            esValido = false;
        }
        if (equipo.getCodigoPatrimonial().isEmpty()) {
            mensajeError.append("Falta código patrimonial.\n");
            esValido = false;
        }
        if (equipo.getDescripcion().isEmpty()) {
            mensajeError.append("Falta descripción.\n");
            esValido = false;
        }
        if (equipo.getEstado().isEmpty()) {
            mensajeError.append("Falta estado.\n");
            esValido = false;
        }
        if (equipo.getFechaCompra().isEmpty()) {
            mensajeError.append("Falta fecha de compra.\n");
            esValido = false;
        }
        if (equipo.getMarca().isEmpty()) {
            mensajeError.append("Falta marca.\n");
            esValido = false;
        }
        if (equipo.getModelo().isEmpty()) {
            mensajeError.append("Falta modelo.\n");
            esValido = false;
        }
        if (equipo.getNombreEquipo().isEmpty()) {
            mensajeError.append("Falta nombre de equipo.\n");
            esValido = false;
        }
        if (equipo.getNumeroOrden().isEmpty()) {
            mensajeError.append("Falta número de orden.\n");
            esValido = false;
        }
        if (equipo.getSerie().isEmpty()) {
            mensajeError.append("Falta serie.\n");
            esValido = false;
        }
        if (dropdownResponsable.getText().toString().isEmpty()) {
            mensajeError.append("Falta responsable.\n");
            esValido = false;
        }
        if (dropdownUbicacion.getText().toString().isEmpty()) {
            mensajeError.append("Falta ubicación.\n");
            esValido = false;
        }

        return esValido;
    }

    private void limpiarCampos() {
        txtTipoEquipo.setText("");
        txtCodigoPatrimonial.setText("");
        txtDescripcion.setText("");
        dropdownEstado.setText("");
        edtFechaCompra.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtNombreDeEquipo.setText("");
        txtNumeroDeOrden.setText("");
        txtNumeroDeSerie.setText("");
        dropdownResponsable.setText("");
        dropdownUbicacion.setText("");
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
