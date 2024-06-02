package fm.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.Activity.ui.Filtros.EscanearCodigoBarrasFragment;
import fm.app.Activity.ui.Filtros.FiltroPorCodigoPatrimonialFragment;
import fm.app.Activity.ui.Filtros.FiltroPorFechasFragment;
import fm.app.Activity.ui.Filtros.FiltroPorNombreFragment;
import fm.app.R;
import fm.app.entity.service.Usuario;
import fm.app.Activity.ui.equipos.*;

public class InicioActivity extends AppCompatActivity {
    private Usuario usuario;
    private ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        initUsuario();
        initViews();
    }

    private void initUsuario() {
        String usuarioJson = getIntent().getStringExtra("UsuarioJson");
        if (usuarioJson == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            usuarioJson = preferences.getString("UsuarioJson", "");
        }

        if (!usuarioJson.isEmpty()) {
            usuario = new Gson().fromJson(usuarioJson, new TypeToken<Usuario>() {}.getType());
        }
    }

    private void initViews() {
        btnLogout = findViewById(R.id.btnCerrarSesion);
        btnLogout.setOnClickListener(view -> onBackPressed());
        findViewById(R.id.contenedorAgregarEquipo).setOnClickListener(v -> navigateToFragment(new AgregarFragment()));
        findViewById(R.id.contenedorListarEquipos).setOnClickListener(v -> navigateToFragment(new ListarFragment()));
        findViewById(R.id.contenedorListarNombreEquipo).setOnClickListener(v -> navigateToFragment(new FiltroPorNombreFragment()));
        findViewById(R.id.contenedorListarCodigoPatrimonial).setOnClickListener(v -> navigateToFragment(new FiltroPorCodigoPatrimonialFragment()));
        findViewById(R.id.contenedorListarPorFechas).setOnClickListener(v -> navigateToFragment(new FiltroPorFechasFragment()));
        findViewById(R.id.contenedorEscanearCodigoBarras).setOnClickListener(v -> navigateToFragment(new EscanearCodigoBarrasFragment()));
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // Cambio aquí
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Desea salir de la aplicación?")
                .setConfirmText("Sí, salir")
                .setCancelText("No, cancelar")
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    logout();
                }).show();
    }

    private void logout() {
        clearUsuarioPreferences();
        showToastLogout();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void clearUsuarioPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
    }

    private void showToastLogout() {
        View layout = LayoutInflater.from(this).inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView text = layout.findViewById(R.id.txtMensajeToast1);
        text.setText("Has cerrado sesión correctamente");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
