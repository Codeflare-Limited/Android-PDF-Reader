package com.app.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener, OnPageErrorListener {
  PDFView pdfView;
  ProgressDialog progressDialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    pdfView = findViewById(R.id.pdfView);
    progressDialog = new ProgressDialog(this);
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("\tLoading ...");
    progressDialog.setCancelable(false);
    progressDialog.show();


    FileLoader.with(this)
      .load("https://sites.ualberta.ca/~enoch/Readings/The_Art_Of_War.pdf",true) //2nd parameter is optioal, pass true to force load from network
      .asFile(new FileRequestListener<File>() {
        @Override
        public void onLoad(FileLoadRequest request, FileResponse<File> response) {
          File url= response.getBody();
          // do something with the file

          try{
            pdfView.fromFile(url)
              .pages(0, 2, 1, 3, 3, 3)
              .defaultPage(0)
              .enableSwipe(true)
              .enableAnnotationRendering(true)
              .onLoad(MainActivity.this)
              .onPageChange(MainActivity.this)
              .scrollHandle(new DefaultScrollHandle(MainActivity.this))
              .enableDoubletap(true)
              .onPageError(MainActivity.this)
              .swipeHorizontal(true)
              .spacing(0)
              .fitEachPage(false)
              .nightMode(false)
              .pageFitPolicy(FitPolicy.WIDTH)
              .autoSpacing(false)
              .load();
          }catch (Exception e){
            e.printStackTrace();
          }

        }

        @Override
        public void onError(FileLoadRequest request, Throwable t) {
          progressDialog.dismiss();
          Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }
      });


  }

  @Override
  public void loadComplete(int nbPages) {
    progressDialog.dismiss();

  }

  @Override
  public void onPageChanged(int page, int pageCount) {

  }

  @Override
  public void onPageError(int page, Throwable t) {

  }
}
