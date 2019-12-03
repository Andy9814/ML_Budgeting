package com.example.nrip.td_ml_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class AlertDialogActivity extends AppCompatActivity {
    // setup a permission we required.
    private static String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    private  Context context;
    Bitmap bitmapImg;
    Uri picUri;
    String mCurrentPhotoPath = "";
    String costImage   = "";
    String autoInvest  = "";
    String manualPrice = "";

    private int REQUEST_CODE_PERMISSIONS = 200;
    static final int IMAGE_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    Intent intentBudget ;
    EditText startupAutoInvestEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        intentBudget = new Intent(AlertDialogActivity.this, BudgetLayout.class);
        Intent intt = getIntent();
        Bundle extras = intt.getExtras();
        autoInvest = extras.getString("AutoInvest");
        startCamera();
    }


//    private void showStartDialog() {
//        // also you can put the custom xml instead of using EditText.
//        MaterialAlertDialogBuilder ad =      new MaterialAlertDialogBuilder(AlertDialogActivity.this)
//                //.setTitle("AutoInvestment")
//                .setMessage("Please Provide the AutoInvestment : ");
//                startupAutoInvestEt = new EditText(this);
//                ad.setView(startupAutoInvestEt);
//                ad.setCancelable(false);
//                ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        autoInvest = startupAutoInvestEt.getText().toString();
//                        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putBoolean("firstStart", false);
//                        editor.putString("sharedPrefAutoInvest", autoInvest);
//                        editor.apply();
//                       // startCameraDialog();
//                        startCamera();
//                    }
//                })
//                .create().show();
//
//
//    }
//
//    // we not gonna use it
//    public void startCameraDialog(){
//        new MaterialAlertDialogBuilder(AlertDialogActivity.this)
//        //new MaterialAlertDialogBuilder(AlertDialogActivity.this, R.style.AlertDialogTheme)
//                //.setTitle("Title")
//                .setMessage("Go to the Camera to scan the price")
//                .setCancelable(false)
//                .setPositiveButton("Go To Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startCamera();
//                    }
//                })
//                .setNeutralButton("History", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .show();
//    }

    public void startCamera(){
        // Check for the permissonis
        if (checkPermissionsGranted()) {
            // if permission Granted Open the Camera
            dispatchTakePictureIntent();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception Ex){
                Log.d("Exception ocurrs ",Ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                picUri= FileProvider.getUriForFile(context,
                        "com.example.android.fileprovider",
                        photoFile);
                String msg = "Pic captured at " + photoFile.getAbsolutePath();
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                try {
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    takePictureIntent.putExtra("return-data", true);

                    startActivityForResult(takePictureIntent, IMAGE_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case IMAGE_FROM_CAMERA:
                performCorp();
                break;
            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = extras.getParcelable("data");
                    bitmapImg = imageBitmap;
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        FirebaseVisionImage imageFirebase = FirebaseVisionImage.fromBitmap(bitmapImg);
                        recognizeText(imageFirebase);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;

        }
    }

    public void   recognizeText(FirebaseVisionImage image) {
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer(); // Use Cloud Api
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
                        {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();
                                    for (FirebaseVisionText.Line line: block.getLines()) {
                                        costImage += line.getText();
                                        for (FirebaseVisionText.Element element: line.getElements()) {
                                        }
                                    }
                                }
                                intentBudget.putExtra("costImage",costImage);
                                intentBudget.putExtra("autoInvestAmt", autoInvest);
                                startActivity( intentBudget);
                            }

                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                }
                        );
    }
    private void performCorp() {
        try {
            CropImage.activity(picUri)
                    .start(this);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

        // Helper Method to convert the file from camera
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // getExternalFileDir is saving in app storage once delted these iamges will be delted as well
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    /*
    Boolean check for Permission if they are granted.
     */
    private boolean checkPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //while Requesting Permission, take us back to camera again.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissionsGranted()) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
