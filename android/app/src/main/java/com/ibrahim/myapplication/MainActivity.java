package com.ibrahim.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{

    Button recordBtn, stopRecordBtn, playBtn;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String outputFile = null;
    final int REQUEST_PREMISSION_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);
        recordBtn = findViewById(R.id.recordBtn);
        stopRecordBtn = findViewById(R.id.stopRecordBtn);



        if (checkPermissionFromDevice())
        {
            stopRecordBtn.setEnabled(false);
            playBtn.setEnabled(false);
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/myrec.3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(outputFile);


        }
        else
        {
            requestPermissions();
        }

    }

    public void start(View view)
    {
        try
        {
            mediaRecorder.prepare();;
            mediaRecorder.start();
        }
         catch (IOException e)
        {
            e.printStackTrace();
        }

        recordBtn.setEnabled(false);
        stopRecordBtn.setEnabled(true);
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
    }
    public void stop(View view)
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        stopRecordBtn.setEnabled(false);
        playBtn.setEnabled(true);
        Toast.makeText(this, "Audio Recorded", Toast.LENGTH_SHORT).show();
    }
    public void play(View view) throws IOException
    {
        MediaPlayer m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        m.start();
        Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show();


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_PREMISSION_CODE:
            {
                if((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }
                break;
        }
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[]
        {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO

        }, REQUEST_PREMISSION_CODE);
    }

    private boolean checkPermissionFromDevice()
    {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result== PackageManager.PERMISSION_GRANTED && record_audio_result==PackageManager.PERMISSION_GRANTED;
    }


}
