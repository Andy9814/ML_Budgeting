//package com.example.nrip.td_ml_project;
//
//import android.Manifest;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.FileProvider;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static androidx.core.app.ActivityCompat.startActivityForResult;
//
//public class CameraHelper {
//    // setup a permission we required.
//    private static String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,};
//
//    private  Context context;
//    Bitmap bitmapImg;
//    Uri picUri;
//    String mCurrentPhotoPath = "";
//    private int REQUEST_CODE_PERMISSIONS = 200;
//    static final int IMAGE_FROM_CAMERA = 1;
//
//    public void startCamera(Context c){
//        context= c;
//        // Check for the permissonis
//        if (checkPermissionsGranted()) {
//            // if permission Granted Open the Camera
//            dispatchTakePictureIntent();
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }
//    }
//
//    private void dispatchTakePictureIntent() {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                picUri= FileProvider.getUriForFile(context,
//                        "com.example.android.fileprovider",
//                        photoFile);
////                picUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",new File(Environment.getExternalStorageDirectory(),
////                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//                // picUri= FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", photoFile);
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
//    // Helper Method to convert the file from camera
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); // getExternalFileDir is saving in app storage once delted these iamges will be delted as well
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//
//    /*
//    Boolean check for Permission if they are granted.
//     */
//    private boolean checkPermissionsGranted() {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        if (requestCode == REQUEST_CODE_PERMISSIONS) {
////            if (allPermissionsGranted()) {
//}
