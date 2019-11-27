//package com.example.nrip.td_ml_project;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.FileProvider;
//
//import android.Manifest;
//import android.content.ActivityNotFoundException;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class previewImage extends AppCompatActivity {
//    private Button capBtn, showBtn;
//    private ImageView imageView;
//    private TextView picTxtV;
//    String mCurrentPhotoPath = "";
//    private int REQUEST_CODE_PERMISSIONS = 200;
//    //keep track of cropping intent
//    final int PIC_CROP = 2;
//    private static final int CROP_FROM_CAMERA = 2;
//    private static String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,};
//    Bitmap bitmapImg;
//    Uri picUri;
//    String costImage = "";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_preview_image);
//        imageView = findViewById(R.id.imageV);
//        capBtn = findViewById(R.id.capBtn);
//        showBtn= findViewById(R.id.showTxtBtn);
//        picTxtV = findViewById(R.id.picTxtV);
//        // ask for permission
//        if (allPermissionsGranted()) {
//
//            // if permission Granted Open the Camera
//            dispatchTakePictureIntent();
//
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }
//
//    }
//    static final int IMAGE_FROM_CAMERA = 1;
//
//    private void dispatchTakePictureIntent() {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                picUri= FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
////                picUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",new File(Environment.getExternalStorageDirectory(),
////                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//              // picUri= FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", photoFile);
//
//
//
//                try {
//                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    takePictureIntent.putExtra("return-data", true);
//
//                    startActivityForResult(takePictureIntent, IMAGE_FROM_CAMERA);
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) return;
//        switch (requestCode) {
//            case IMAGE_FROM_CAMERA:
//                performCorp();
//
//                break;
//
//            case CROP_FROM_CAMERA:
//                Bundle extras = data.getExtras();
//
//                if (extras != null) {
//                    Bitmap imageBitmap = extras.getParcelable("data");
//                    bitmapImg = imageBitmap;
//                    imageView.setImageBitmap(imageBitmap);
//                }
//
////                File f = new File(mImageCaptureUri.getPath());
////
////                if (f.exists()) f.delete();
//
//                break;
//            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    Uri resultUri = result.getUri();
//                    try {
//                        bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    imageView.setImageURI(resultUri);
//
//
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                }
//
//
//                break;
//
//        }
//
////        Bundle extras = data.getExtras();
//////                Bitmap imageBitmap = (Bitmap) extras.get("data");
//////        bitmapImg = imageBitmap;
//////        imageView.setImageBitmap(bitmapImg);
////        if (resultCode == RESULT_OK)
////        {
////           // picUri = data.getData();
////
////            performCorp();
////        }
////
////        //user is returning from cropping the image
////        else if(requestCode ==  PIC_CROP){
////
////            if (extras != null) {
////                Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");
////                bitmapImg = imageBitmap;
////                imageView.setImageBitmap(bitmapImg);
////            }
////        }
//
//
//
//
////        Bundle extras = data.getExtras();
////        Bitmap imageBitmap = (Bitmap) extras.get("data");
////        bitmapImg = imageBitmap;
////        imageView.setImageBitmap(bitmapImg);
////        if ( resultCode == RESULT_OK) {
//////            CropImage.activity(data.getdata())
////////                    .setGuidelines(CropImageView.Guidelines.ON)
//////                    .start(this);
////            // start cropping activity for pre-acquired image saved on the device
////            CropImage.activity(picUri)
////                    .start(this);
////            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////                CropImage.ActivityResult result = CropImage.getActivityResult(data);
////                if (resultCode == RESULT_OK) {
////                    Uri resultUri = result.getUri();
////                    Bundle extras = data.getExtras();
////                    Bitmap imageBitmap = (Bitmap) extras.get("data");
////                    bitmapImg = imageBitmap;
////                     //imageView.setImageBitmap(bitmapImg);
////                  imageView.setImageURI(resultUri);
////                    // bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
////
////                  //  bitmapImg =  bitmapDrawable.getBitmap();
////
////                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                    Exception error = result.getError();
////                }
////            }
////
////        }
//    }
//
//    private void performCorp() {
//        try {
//            CropImage.activity(picUri)
//                    .start(this);
////          //  Intent intent = new Intent("com.android.camera.action.CROP");
////            Intent intent = new Intent(this, CropImage.class);
////            intent.setType("image/*");
////            List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
////            int size = list.size();
////            if (size == 0) {
////                Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
////
////                return;
////            } else {
////                intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
////                intent.putExtra(CropImage.SCALE, true);
////
////                intent.putExtra(CropImage.ASPECT_X, 3);
////                intent.putExtra(CropImage.ASPECT_Y, 2);
//////                intent.setData(picUri);
//////                intent.putExtra("outputX", 200);
//////                intent.putExtra("outputY", 200);
//////                intent.putExtra("aspectX", 1);
//////                intent.putExtra("aspectY", 1);
//////                intent.putExtra("scale", true);
//////                intent.putExtra("return-data", true);
////                if (size == 1) {
////                    Intent i        = new Intent(intent);
////                    ResolveInfo res = list.get(0);
////
////                    i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
////
////                    startActivityForResult(i, CROP_FROM_CAMERA);
////                } else {
//////                    final CropOption co = new CropOption();
//////
//////                    co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
//////                    co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
//////                    co.appIntent= new Intent(intent);
//////
//////                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//////
//////                    cropOptions.add(co);
////                }
////
////            }
////            //call the standard crop action intent (the user device may not support it)
////            Intent cropIntent = new Intent("com.android.camera.action.CROP");
////            //indicate image type and Uri
////            cropIntent.setDataAndType(picUri, "image/*");
////            //set crop properties
////            cropIntent.putExtra("crop", "true");
////            //indicate aspect of desired crop
////            cropIntent.putExtra("aspectX", 1);
////            cropIntent.putExtra("aspectY", 1);
////            //indicate output X and Y
////            cropIntent.putExtra("outputX", 256);
////            cropIntent.putExtra("outputY", 256);
////            //retrieve data on return
////            cropIntent.putExtra("return-data", true);
////            //start the activity - we handle returning in onActivityResult
////            startActivityForResult(cropIntent, PIC_CROP);
//        }
//        catch(ActivityNotFoundException anfe){
//            //display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
//
//        @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (allPermissionsGranted()) {
//                // As soon as you request for permission go for camera
//
//                dispatchTakePictureIntent();
////                capBtn.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        dispatchTakePictureIntent();
////
////                    }
////                });
////
////                showBtn.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        detectTextHelper dt  = new detectTextHelper();
////                        //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
////                        // bitmapImg = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
////                        FirebaseVisionImage imageFirbase = FirebaseVisionImage.fromBitmap(bitmapImg);
////                        dt.recognizeText(imageFirbase);
////
////
////                    }
////                });
//            } else {
//                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
//// Helper Method to convert the file from camera
//private File createImageFile() throws IOException {
//    // Create an image file name
//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//    String imageFileName = "JPEG_" + timeStamp + "_";
//    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // getExternalFileDir is saving in app storage once delted these iamges will be delted as well
//    File image = File.createTempFile(
//            imageFileName,  /* prefix */
//            ".jpg",         /* suffix */
//            storageDir      /* directory */
//    );
//
//    // Save a file: path for use with ACTION_VIEW intents
//    mCurrentPhotoPath = image.getAbsolutePath();
//    return image;
//}
//    private boolean allPermissionsGranted() {
//
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void onClickCapture(View view) {
//        dispatchTakePictureIntent();
//        FirebaseVisionImage imageFirbase = FirebaseVisionImage.fromBitmap(bitmapImg);
//        //dt.recognizeText(imageFirbase);
//        recognizeText(imageFirbase);
//    }
//
//    public void onClickShowTxt(View view) {
//     //   detectTextHelper dt  = new detectTextHelper();
//        //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        // bitmapImg = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
//        //FirebaseVisionImage imageFirbase = FirebaseVisionImage.fromBitmap(bitmapImg);
//        //dt.recognizeText(imageFirbase);
////        recognizeText(imageFirbase);
//        //picTxtV.setText(costImage);
//       validateCoseImage(costImage);
//    }
//
//    public void   recognizeText(FirebaseVisionImage image) {
//        // [START get_detector_default]
//        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
//                .getOnDeviceTextRecognizer();
//        // [END get_detector_default]
//
//        // [START run_detector]
//        Task<FirebaseVisionText> result =
//                detector.processImage(image)
//                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
//                        {
//                            @Override
//                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                                // Task completed successfully
//                                // [START_EXCLUDE]
//                                // [START get_text]
//                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
//                                    Rect boundingBox = block.getBoundingBox();
//                                    Point[] cornerPoints = block.getCornerPoints();
//                                    String text = block.getText();
//                                    //tb =  block;
//                                    for (FirebaseVisionText.Line line: block.getLines()) {
//                                        Log.d("----------------------------------------------------",line.getText());
//
//                                        costImage += line.getText();
//                                        for (FirebaseVisionText.Element element: line.getElements()) {
////                                            Log.d("----------------------------------------------------",element.getText());
//                                        }
//                                    }
//                                }
//                                // [END get_text]
//                                // [END_EXCLUDE]
//                            }
//
//                        })
//                        .addOnFailureListener(
//                                new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Task failed with an exception
//                                        // ...
//                                    }
//                                }
//                        );
//
//    }
//
//    public void validateCoseImage(String cost){
//
//        // convert String to char[] array
//        char[] chars = cost.toCharArray();
//// using simple for loop
////        for (int i = 0; i < cost.length(); i++) {
////            if(cost.charAt(i) == '$'){
////              //  picTxtV.setText("$$$$$");
////                Log.d("",cost.);
////            }
////        }
//        for(char ch : chars){
//           if(ch  == '$'){
//
//
//           }
//
//        }
//        picTxtV.setText(costImage); // there is a issue fix this
//
//    }
//}
//
