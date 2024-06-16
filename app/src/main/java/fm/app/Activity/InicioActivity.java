package fm.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.app.Activity.ui.Filtros.EscanearCodigoBarrasFragment;
import fm.app.Activity.ui.Filtros.FiltroPorCodigoPatrimonialFragment;
import fm.app.Activity.ui.Filtros.FiltroPorFechasFragment;
import fm.app.Activity.ui.Filtros.FiltroPorNombreFragment;
import fm.app.BuildConfig;
import fm.app.R;
import fm.app.entity.service.Usuario;
import fm.app.Activity.ui.equipos.AgregarFragment;
import fm.app.Activity.ui.equipos.ListarFragment;
import fm.app.viewModel.EquipoViewModel;
import okhttp3.ResponseBody;

public class InicioActivity extends AppCompatActivity {
    private Usuario usuario;
    private ImageView btnLogout;
    private Uri fileUri;

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
        findViewById(R.id.contenedorAgregarEquipo).setOnClickListener(v -> navigateToFragment(new AgregarFragment(), R.anim.left_in, R.anim.left_out));
        findViewById(R.id.contenedorListarEquipos).setOnClickListener(v -> navigateToFragment(new ListarFragment(), R.anim.rigth_in, R.anim.left_out));
        findViewById(R.id.contenedorListarNombreEquipo).setOnClickListener(v -> navigateToFragment(new FiltroPorNombreFragment(), R.anim.down_in, R.anim.down_out));
        findViewById(R.id.contenedorListarCodigoPatrimonial).setOnClickListener(v -> navigateToFragment(new FiltroPorCodigoPatrimonialFragment(), R.anim.above_in, R.anim.above_out));
        findViewById(R.id.contenedorListarPorFechas).setOnClickListener(v -> navigateToFragment(new FiltroPorFechasFragment(), R.anim.from_bottom_anim, R.anim.to_bottom_anim));
        findViewById(R.id.contenedorReporteEquipos).setOnClickListener(v -> showDownloadConfirmationDialog());
        findViewById(R.id.contenedorEscanearCodigoBarras).setOnClickListener(v -> navigateToFragment(new EscanearCodigoBarrasFragment(), R.anim.left_in, R.anim.left_out));
    }

    private void showDownloadConfirmationDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Desea descargar el Reporte General de Equipos")
                .setConfirmText("Sí")
                .setCancelText("No")
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    selectFileLocation();
                }).show();
    }

    private final ActivityResultLauncher<Intent> createDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fileUri = result.getData().getData();
                    downloadExcelReport();
                }
            }
    );

    private void selectFileLocation() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.putExtra(Intent.EXTRA_TITLE, "Reporte_De_Equipos.xlsx");
        createDocumentLauncher.launch(intent);
    }

    private void downloadExcelReport() {
        EquipoViewModel equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
        equipoViewModel.downloadExcelReport().observe(this, responseBody -> {
            if (responseBody != null) {
                saveExcelFile(responseBody);
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Error descargando el archivo")
                        .show();
            }
        });
    }

    private void saveExcelFile(ResponseBody body) {
        try (InputStream inputStream = body.byteStream();
             OutputStream outputStream = getContentResolver().openOutputStream(fileUri)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Descarga completa")
                    .setContentText("El archivo se ha guardado correctamente")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        openDownloadedFile(fileUri);
                    })
                    .show();

        } catch (IOException e) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("Error al guardar el archivo: " + e.getMessage())
                    .show();
        }
    }

    private void openDownloadedFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Abrir con"));
    }

    private void navigateToFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, enterAnim, exitAnim);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
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
        overridePendingTransition(R.anim.rigth_in, R.anim.left_out);
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
