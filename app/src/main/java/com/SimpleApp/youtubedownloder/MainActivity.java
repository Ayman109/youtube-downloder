package com.SimpleApp.youtubedownloder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText youtubUrl ;
    private Button search , mp3 , mp4 ;
    private ImageView videoImage ;
    private TextView videoTitle ;
    private CardView cardView  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init(){
        youtubUrl = (EditText) findViewById(R.id.youtubUrl );
        search = (Button) findViewById(R.id.search);
        mp3 = (Button) findViewById(R.id.mp3);
        mp4 = (Button) findViewById(R.id.mp4);
        videoImage = (ImageView) findViewById(R.id.videoImage);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        cardView = (CardView) findViewById(R.id.videoInfo);
    }
    //pRpeEdMmmQ0
    private void getVideoInfo(String url){

        YoutubeDownloader downloader = new YoutubeDownloader();
        RequestVideoInfo request = new RequestVideoInfo(url).async();
        VideoInfo video = downloader.getVideoInfo(request).data();
        VideoDetails details = video.details();
        videoTitle.setText(details.title());
        Picasso.get().load(details.thumbnails().get(details.thumbnails().size()-1).toString()).into(videoImage);
        cardView.setVisibility(View.VISIBLE);
    //----------------------------------------------------------
        mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                download(video.bestVideoWithAudioFormat().url(),"mp4",details.title());
            }
        });

    //-----------------------------------------
        mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download(video.bestAudioFormat().url(),"mp3",details.title());
            }
        });


    }

    private void download(String url , String type , String title){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            // this will request for permission when user has not granted permission for the app
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title+"."+type);
        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }}

    public void search(View view) {
        String videoId = getVideoid();
        if(!videoId.equals("")){
            getVideoInfo(videoId);
        }else {
            youtubUrl.setText("");
            Toast.makeText(getApplicationContext(),"URL non valide",Toast.LENGTH_LONG).show();
        }

    }

    private String getVideoid(){
        String url = youtubUrl.getText().toString();
        return verifUrl(url);
    }

    private String  verifUrl(String url){
        if(url.indexOf("https://www.youtube.com/watch?v=")==0)
            return videoId(url.replace("https://www.youtube.com/watch?v=",""));
        if (url.indexOf("https://youtu.be/")==0)
            return videoId(url.replace("https://youtu.be/",""));
        else return "";

    }

    public String videoId(String url){
        String ch = "";
        for (int i = 0 ; i<11 ; i++){
            ch+=url.charAt(i);
        }
        return ch ;
    }

}