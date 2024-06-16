package fm.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import fm.app.R;
import fm.app.entity.service.Usuario;
import fm.app.viewModel.UsuarioViewModel;

public class MainActivity extends AppCompatActivity {
    private EditText edtMail, edtPassword;
    private Button btnIniciarSesion;
    private UsuarioViewModel viewModel;
    private TextInputLayout txtInputUsuario, txtInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();
        init();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {
        edtMail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(view -> {
            if (validar()) {
                viewModel.login(edtMail.getText().toString(), edtPassword.getText().toString()).observe(this, usuarioGenericResponse -> {
                    if (usuarioGenericResponse.getRpta() == 1) {
                        Usuario u = usuarioGenericResponse.getBody();
                        saveUsuarioPreferences(u);
                        clearFields();
                        startActivityWithTransition(u);
                    } else {
                        toastIncorrecto(usuarioGenericResponse.getMessage());
                    }
                });
            } else {
                toastIncorrecto("Por favor, complete todos los campos");
            }
        });

        edtMail.addTextChangedListener(new GenericTextWatcher(txtInputUsuario));
        edtPassword.addTextChangedListener(new GenericTextWatcher(txtInputPassword));
    }

    private void saveUsuarioPreferences(Usuario u) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.putString("UsuarioJson", gson.toJson(u));
        editor.apply();
    }

    private void clearFields() {
        edtMail.setText("");
        edtPassword.setText("");
    }

    private boolean validar() {
        return validarCampo(edtMail, txtInputUsuario, "Ingrese su usuario y/o correo electrónico") &&
                validarEmail(edtMail, txtInputUsuario) &&
                validarCampo(edtPassword, txtInputPassword, "Ingrese su contraseña");
    }

    private boolean validarCampo(EditText editText, TextInputLayout textInputLayout, String error) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(error);
            return false;
        }
        textInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validarEmail(EditText editText, TextInputLayout textInputLayout) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches()) {
            textInputLayout.setError("Correo electrónico no válido");
            return false;
        }
        return true;
    }

    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(isError ? R.layout.custom_toast_error : R.layout.custom_toast_ok,
                (ViewGroup) findViewById(isError ? R.id.ll_custom_toast_error : R.id.ll_custom_toast_ok));

        TextView text = layout.findViewById(isError ? R.id.txtMensajeToast2 : R.id.txtMensajeToast1);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void toastCorrecto(String msg) {
        showToast(msg, false);
    }

    public void toastIncorrecto(String msg) {
        showToast(msg, true);
    }

    private class GenericTextWatcher implements TextWatcher {
        private final TextInputLayout layout;

        GenericTextWatcher(TextInputLayout layout) {
            this.layout = layout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                layout.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private void startActivityWithTransition(Usuario usuario) {
        Intent intent = new Intent(this, InicioActivity.class);
        intent.putExtra("UsuarioJson", new Gson().toJson(usuario));
        startActivity(intent);
        overridePendingTransition(R.anim.rigth_in, R.anim.left_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForActiveSession();
    }

    private void checkForActiveSession() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usuarioJson = preferences.getString("UsuarioJson", "");
        if (!usuarioJson.isEmpty()) {
            toastCorrecto("Se detectó una sesión activa, el login será omitido!");
            Intent intent = new Intent(this, InicioActivity.class);
            intent.putExtra("UsuarioJson", usuarioJson);
            startActivity(intent);
            finish();
        }
    }
}
