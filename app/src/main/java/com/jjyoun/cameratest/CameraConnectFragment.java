package com.jjyoun.cameratest;

import android.Manifest;
import android.os.Bundle;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.ListenableFuture;
import com.jjyoun.cameratest.databinding.FragmentCameraConnectBinding;

import java.util.concurrent.ExecutionException;


public class CameraConnectFragment extends Fragment {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final int PERMISSIONS_REQUEST = 1;

    private FragmentCameraConnectBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCameraConnectBinding.inflate(inflater, container, false);

        camera();

        return binding.getRoot();
    }

    private void camera(){
        Log.d("x", "카메라ㅠ");
        //CameraProvider 요청
        //cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        PreviewView previewView = binding.previewView;

        //CameraProvider 사용 가능 여부 확인
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();


                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                Camera camera = cameraProvider.bindToLifecycle(
                        ((LifecycleOwner)this), cameraSelector, preview
                );
                //bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));

    }
}