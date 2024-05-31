package fm.app.Activity.ui.equipos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fm.app.R;
import fm.app.entity.service.Equipo;
import fm.app.viewModel.EquipoViewModel;

public class ModificarFragment extends Fragment {
    private EquipoViewModel equipoViewModel;
    private TextInputEditText txtTipoEquipo, txtCodigoPatrimonial, txtDescripcion, txtMarca, txtModelo, txtNombreEquipo, txtNumeroOrden, txtNumeroSerie, edtFechaCompra;
    private AutoCompleteTextView dropdownEstado;
    private Button btnModificarEquipo;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar, container, false);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);

        txtTipoEquipo = view.findViewById(R.id.txtTipoEquipoModificar);
        txtDescripcion = view.findViewById(R.id.txtDescripcionModificar);
        txtMarca = view.findViewById(R.id.txtMarcaModificar);
        txtModelo = view.findViewById(R.id.txtModeloModificar);
        txtNombreEquipo = view.findViewById(R.id.txtNombreDeEquipoModificar);
        txtNumeroOrden = view.findViewById(R.id.txtNumeroDeOrdenModificar);
        txtNumeroSerie = view.findViewById(R.id.txtNumeroDeSerieModificar);
        edtFechaCompra = view.findViewById(R.id.edtFechaCompraModificar);
        dropdownEstado = view.findViewById(R.id.dropdownEstadoModificar);
        btnModificarEquipo = view.findViewById(R.id.btnModificarEquipo);

        // Configuración de la toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarModificar);
        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtrasModificar);
        btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        edtFechaCompra.setOnClickListener(v -> mostrarDatePickerDialog());
        btnModificarEquipo.setOnClickListener(v -> modificarEquipo());

        cargarEstados();

        return view;
    }

    private void mostrarDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            edtFechaCompra.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void modificarEquipo() {
        Equipo equipo = new Equipo();
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

        equipoViewModel.updateEquipo(equipo).observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                Toast.makeText(getContext(), "Equipo modificado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al modificar equipo: " + response.getMessage(), Toast.LENGTH_LONG).show();
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
}
