package com.example.nrip.td_ml_project;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class detectTextHelper {
     public  String word = "";


    public void   recognizeText(FirebaseVisionImage image) {


        // [START get_detector_default]
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        // [END get_detector_default]

        // [START run_detector]
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
                        {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]
                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();
                                    //tb =  block;

                                    for (FirebaseVisionText.Line line: block.getLines()) {
                                        Log.d("----------------------------------------------------",line.getText());

                                        word += line.getText();
                                        for (FirebaseVisionText.Element element: line.getElements()) {
//                                            Log.d("----------------------------------------------------",element.getText());
                                        }
                                    }
                                }
                                // [END get_text]
                                // [END_EXCLUDE]
                            }

                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                }
                                );
        // [END run_detector]

    }
}
