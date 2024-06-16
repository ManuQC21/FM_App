package fm.app.Activity.ui.equipos;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.R;
import fm.app.entity.service.Equipo;
import fm.app.viewModel.EquipoViewModel;

public class DetalleEquipoFragment extends Fragment {

    private EquipoViewModel equipoViewModel;
    private TextView txtTipoEquipo, txtCodigoBarra, txtNombreEquipo, txtMarca, txtModelo, txtSerie,
            txtNumeroOrden, txtDescripcion, txtEstado, txtCodigoPatrimonial, txtFechaCompra,
            txtNombreResponsable, txtCargoResponsable, txtAmbiente, txtUbicacionFisica;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_equipo, container, false);

        // Inicializar las vistas
        inicializarVistas(view);

        // Configurar el botón de volver atrás
        configurarBotonVolverAtras(view);

        // Cargar detalles del equipo si hay un ID
        if (getArguments() != null && getArguments().containsKey("equipoId")) {
            int equipoId = getArguments().getInt("equipoId", -1);
            if (equipoId != -1) {
                cargarDetalleEquipo(equipoId);
            } else {
                mostrarError("ID del equipo no válido");
            }
        }

        return view;
    }

    private void inicializarVistas(View view) {
        txtTipoEquipo = view.findViewById(R.id.txtTipoEquipo);
        txtCodigoBarra = view.findViewById(R.id.txtCodigoBarra);
        txtNombreEquipo = view.findViewById(R.id.txtNombreEquipo);
        txtMarca = view.findViewById(R.id.txtMarca);
        txtModelo = view.findViewById(R.id.txtModelo);
        txtSerie = view.findViewById(R.id.txtSerie);
        txtNumeroOrden = view.findViewById(R.id.txtNumeroOrden);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtEstado = view.findViewById(R.id.txtEstado);
        txtCodigoPatrimonial = view.findViewById(R.id.txtCodigoPatrimonial);
        txtFechaCompra = view.findViewById(R.id.txtFechaCompra);
        txtNombreResponsable = view.findViewById(R.id.txtNombreResponsable);
        txtCargoResponsable = view.findViewById(R.id.txtCargoResponsable);
        txtAmbiente = view.findViewById(R.id.txtAmbiente);
        txtUbicacionFisica = view.findViewById(R.id.txtUbicacionFisica);
    }

    private void configurarBotonVolverAtras(View view) {
        ImageView btnVolverAtras = view.findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void cargarDetalleEquipo(int equipoId) {
        equipoViewModel.getEquipoById(equipoId).observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRpta() == 1) {
                mostrarDetalleEquipo(response.getBody());
            } else {
                mostrarError("Error al cargar los detalles del equipo");
            }
        });
    }

    private void mostrarDetalleEquipo(Equipo equipo) {
        txtTipoEquipo.setText(equipo.getTipoEquipo() != null ? equipo.getTipoEquipo() : "-");
        txtCodigoBarra.setText(equipo.getCodigoBarra() != null ? equipo.getCodigoBarra() : "-");
        txtNombreEquipo.setText(equipo.getNombreEquipo() != null ? equipo.getNombreEquipo() : "-");
        txtMarca.setText(equipo.getMarca() != null ? equipo.getMarca() : "-");
        txtModelo.setText(equipo.getModelo() != null ? equipo.getModelo() : "-");
        txtSerie.setText(equipo.getSerie() != null ? equipo.getSerie() : "-");
        txtNumeroOrden.setText(equipo.getNumeroOrden() != null ? equipo.getNumeroOrden() : "-");
        txtDescripcion.setText(equipo.getDescripcion() != null ? equipo.getDescripcion() : "-");
        txtEstado.setText(equipo.getEstado() != null ? equipo.getEstado() : "-");
        txtCodigoPatrimonial.setText(equipo.getCodigoPatrimonial() != null ? equipo.getCodigoPatrimonial() : "-");
        txtFechaCompra.setText(equipo.getFechaCompra() != null ? equipo.getFechaCompra() : "-");

        if (equipo.getResponsable() != null) {
            txtNombreResponsable.setText(equipo.getResponsable().getNombre() != null ? equipo.getResponsable().getNombre() : "-");
            txtCargoResponsable.setText(equipo.getResponsable().getCargo() != null ? equipo.getResponsable().getCargo() : "-");
        } else {
            txtNombreResponsable.setText("-");
            txtCargoResponsable.setText("-");
        }

        if (equipo.getUbicacion() != null) {
            txtAmbiente.setText(equipo.getUbicacion().getAmbiente() != null ? equipo.getUbicacion().getAmbiente() : "-");
            txtUbicacionFisica.setText(equipo.getUbicacion().getUbicacionFisica() != null ? equipo.getUbicacion().getUbicacionFisica() : "-");
        } else {
            txtAmbiente.setText("-");
            txtUbicacionFisica.setText("-");
        }

        mostrarAlerta("Detalles Cargados", "Los detalles del equipo se han cargado correctamente.");
    }

    private void mostrarError(String mensaje) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(mensaje)
                .show();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        SweetAlertDialog successDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje);

        successDialog.show();

        new Handler().postDelayed(successDialog::dismissWithAnimation, 1000);
    }
}
