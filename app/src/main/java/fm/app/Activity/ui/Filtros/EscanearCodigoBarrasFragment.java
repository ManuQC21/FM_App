package fm.app.Activity.ui.Filtros;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                sendImageToServer(savedUri);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(getContext(), "Error taking photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendImageToServer(Uri fileUri) {
        File file = new File(fileUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.get("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        equipoViewModel.scanAndCopyBarcodeData(body).observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == Global.RPTA_OK) {
                navigateToDetailsFragment(response.getBody());
            } else {
                showAlert("Error", response.getMessage());
            }
        });
    }

    private void navigateToDetailsFragment(Equipo equipo) {
        Toast.makeText(getContext(), "Equipo: " + equipo.getNombreEquipo(), Toast.LENGTH_LONG).show();
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
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
