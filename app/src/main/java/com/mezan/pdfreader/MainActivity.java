package com.mezan.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    PDFView pdfView;
    FloatingActionButton SelectFile;
    boolean Access = false;
    TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView = findViewById(R.id.pdfview);
        SelectFile = findViewById(R.id.select);
        hint = findViewById(R.id.hint);
        SelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadFile();
            }
        });
       // StoragePermission();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                  //  filePath = fileUri.getPath();
                    hint.setVisibility(View.INVISIBLE);
                    pdfView.setVisibility(View.VISIBLE);
                    //pdfView.fromAsset("flex.pdf")
                    pdfView.fromUri(fileUri)
                            .enableSwipe(true) // allows to block changing pages using swipe
                            .swipeHorizontal(true)
                            .enableDoubletap(true)
                            .defaultPage(0)
                            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                            .password(null)
                            .scrollHandle(null)
                            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                            // spacing between pages in dp. To define spacing color, set view background
                            .spacing(0)
                            .load();

                   // String PathHolder = data.getData().getPath();
//                    Uri uri = data.getData();
//                    File file = FileUtil.from(MainActivity.this,uri);


                    Toast.makeText(MainActivity.this, "File name", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
    private void StoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Access = true;
                    ReadFile();
                } else {
                    Access = false;

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    StoragePermission();
                    Toast.makeText(MainActivity.this, "Permission Rejected!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void ReadFile(){
        if(Access){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent = Intent.createChooser(intent, "Choose a pdf file");
            startActivityForResult(intent, 7);
        }else {
            StoragePermission();
        }
    }
}
