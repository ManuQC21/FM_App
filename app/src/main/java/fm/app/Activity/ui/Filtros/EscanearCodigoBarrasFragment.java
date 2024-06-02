package fm.app.Activity.ui.Filtros;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

import fm.app.R;
import fm.app.databinding.FragmentEscanearCodigoBarrasBinding;
import fm.app.entity.Global;
import fm.app.entity.service.Equipo;
import fm.app.viewModel.EquipoViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EscanearCodigoBarrasFragment extends Fragment {

    private FragmentEscanearCodigoBarrasBinding binding;
    private EquipoViewModel equipoViewModel;
    private ImageCapture imageCapture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEscanearCodigoBarrasBinding.inflate(inflater, container, false);
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
        binding.btnVolverAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

        binding.btnAnalyze.setOnClickListener(v -> takePhoto());
        return binding.getRoot();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getContext(), "Error starting camera: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_DEFAULT)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageCapture = new ImageCapture.Builder()
                .setTargetResolution(new Size(1920, 1080))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void takePhoto() {
        File photoFile = new File(requireContext().getExternalFilesDir(null), "scan_" + System.currentTimeMillis() + ".png");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = Uri.fromFile(photoFile);
                sendImageToServer(savedUri, photoFile);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(getContext(), "Error taking photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                if (photoFile.exists()) {
                    photoFile.delete();
                }
            }
        });
    }

    private void sendImageToServer(Uri fileUri, File photoFile) {
        RequestBody requestFile = RequestBody.create(MediaType.get("image/png"), photoFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoFile.getName(), requestFile);

        equipoViewModel.scanAndCopyBarcodeData(body).observe(getViewLifecycleOwner(), response -> {
            // Eliminar el archivo después de enviarlo al servidor, sin importar el resultado
            if (photoFile.exists()) {
                photoFile.delete();
            }

            if (response.getRpta() == Global.RPTA_OK) {
                showCustomDialog(response.getBody());
            } else {
                showAlert("Error", response.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showCustomDialog(Equipo equipo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_equipo, null);
        builder.setView(customLayout);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Configuración de los campos de texto para la información del equipo
        ((TextView) customLayout.findViewById(R.id.tvNombreEquipo)).setText(safeString(equipo.getNombreEquipo()));
        ((TextView) customLayout.findViewById(R.id.tvTipoEquipo)).setText(safeString(equipo.getTipoEquipo()));
        ((TextView) customLayout.findViewById(R.id.tvMarca)).setText(safeString(equipo.getMarca()));
        ((TextView) customLayout.findViewById(R.id.tvModelo)).setText(safeString(equipo.getModelo()));
        ((TextView) customLayout.findViewById(R.id.tvSerie)).setText(safeString(equipo.getSerie()));
        ((TextView) customLayout.findViewById(R.id.tvNumeroOrden)).setText(safeString(equipo.getNumeroOrden()));
        ((TextView) customLayout.findViewById(R.id.tvDescripcion)).setText(safeString(equipo.getDescripcion()));
        ((TextView) customLayout.findViewById(R.id.tvEstado)).setText(safeString(equipo.getEstado()));
        ((TextView) customLayout.findViewById(R.id.tvCodigoPatrimonial)).setText(safeString(equipo.getCodigoPatrimonial()));
        ((TextView) customLayout.findViewById(R.id.tvCodigoBarra)).setText(safeString(equipo.getCodigoBarra()));
        ((TextView) customLayout.findViewById(R.id.tvFechaCompra)).setText(equipo.getFechaCompra() != null ? equipo.getFechaCompra().toString() : "-");

        // Configuración de los campos de texto para la información del responsable
        ((TextView) customLayout.findViewById(R.id.tvNombreResponsable)).setText(safeString(equipo.getResponsable() != null ? equipo.getResponsable().getNombre() : "-"));
        ((TextView) customLayout.findViewById(R.id.tvCargoResponsable)).setText(safeString(equipo.getResponsable() != null ? equipo.getResponsable().getCargo() : "-"));

        // Configuración de los campos de texto para la información de la ubicación
        ((TextView) customLayout.findViewById(R.id.tvAmbiente)).setText(safeString(equipo.getUbicacion() != null ? equipo.getUbicacion().getAmbiente() : "-"));
        ((TextView) customLayout.findViewById(R.id.tvUbicacionFisica)).setText(safeString(equipo.getUbicacion() != null ? equipo.getUbicacion().getUbicacionFisica() : "-"));
    }

    private String safeString(String text) {
        return text != null ? text : "-";
    }


    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(getContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
