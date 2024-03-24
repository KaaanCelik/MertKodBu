package com.example.javaqrscannernew;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.javaqrscannernew.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<String> requestPremissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    showCamera();
                }
                else {

                }
                    });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result ->{
        if (result.getContents() == null){
            Toast.makeText(this, "İptal Edildi", Toast.LENGTH_SHORT).show();
        } else {
            setResult(result.getContents());
        }
    });

    private void setResult(String contents) {
        binding.textResult.setText(contents);
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("QR Kodu Okutun");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();

    }

    private void initViews() {
        binding.fab.setOnClickListener(view -> {
            checkPermissionAndShowActivity(this);
            


        });
    }

    private void checkPermissionAndShowActivity(Context context) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Kamera izni gereklidir", Toast.LENGTH_SHORT).show();
        } else {
            requestPremissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}