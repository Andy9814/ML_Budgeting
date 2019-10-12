package com.example.nrip.td_ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Live detection and tracking
        FirebaseVisionObjectDetectorOptions options =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
                        .enableClassification()  // Optional
                        .build();

//// Multiple object detection in static images
//        FirebaseVisionObjectDetectorOptions options =
//                new FirebaseVisionObjectDetectorOptions.Builder()
//                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
//                        .enableMultipleObjects()
//                        .enableClassification()  // Optional
//                        .build();

        FirebaseVisionObjectDetector objectDetector =
                FirebaseVision.getInstance().getOnDeviceObjectDetector();
    }





}
