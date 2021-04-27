package com.example.antibully;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import java.io.IOException;
import java.util.ArrayList;

public class Boost_Confidence extends AppCompatActivity {
    ImageSlider imageSlider;
    TextView music1,music2,music3,music4,music5;
    String url_id="";
    MediaPlayer mediaPlayer;
    ProgressDialog progressDialog;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boost__confidence);

        progressDialog = new ProgressDialog(this);



        music1 = findViewById(R.id.music_1);

        music2 = findViewById(R.id.music_2);
        music3 = findViewById(R.id.music_3);
        music4 = findViewById(R.id.music_4);
        music5 = findViewById(R.id.music_5);
        imageSlider = findViewById(R.id.slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://www.pixelstalk.net/wp-content/uploads/2016/06/Happy-Wallpapers-HD-Free-Download.jpg",null));
        slideModels.add(new SlideModel("https://wallpapercave.com/wp/wp2411857.jpg",null));
        slideModels.add(new SlideModel("https://www.hdwallpapersfreedownload.com/uploads/large/special-days/international-day-of-happiness-hd-wallpaper-flower-smiley-face.jpg",null));
        slideModels.add(new SlideModel("https://c0.wallpaperflare.com/preview/986/187/750/human-jump-luck-success.jpg",null));
        slideModels.add(new SlideModel("https://cdn.hipwallpaper.com/i/41/91/AgG0dr.jpg",null));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        musicControls();
        glidemusic();
        showdialogbox();
        exerciseimagessrc();



    }


    void musicControls(){
        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music1.getText().toString().equalsIgnoreCase("Play")){

                    music1.setText("Stop");
                    music2.setText("Play");
                    music3.setText("Play");
                    music4.setText("Play");
                    music5.setText("Play");

                    try{

                        stopmusic();

                    }
                    catch (Exception e){

                    }
                    playmusic("https://firebasestorage.googleapis.com/v0/b/antibully-b7705.appspot.com/o/Songs%2Fmusic_1.mp3?alt=media&token=ba146efd-dc0b-4b25-957d-c4def5bfb938");

                }
                else{
                    music1.setText("Play");

                    stopmusic();

                }

            }
        });
        music2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(music2.getText().toString().equalsIgnoreCase("Play")){

                    try{

                        stopmusic();

                    }
                    catch (Exception e){

                    }
                    music2.setText("Stop");
                    music1.setText("Play");
                    music3.setText("Play");
                    music4.setText("Play");
                    music5.setText("Play");
                    playmusic("https://firebasestorage.googleapis.com/v0/b/antibully-b7705.appspot.com/o/Songs%2Fmusic_2.mp3?alt=media&token=67403cbc-aa98-4756-b3c9-1a312f10fa10");

                }
                else{
                    music2.setText("Play");
                    stopmusic();

                }

            }
        });
        music3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(music3.getText().toString().equalsIgnoreCase("Play")){
                    try{

                        stopmusic();

                    }
                    catch (Exception e){

                    }
                    music3.setText("Stop");
                    music2.setText("Play");
                    music1.setText("Play");
                    music4.setText("Play");
                    music5.setText("Play");
                    playmusic("https://firebasestorage.googleapis.com/v0/b/antibully-b7705.appspot.com/o/Songs%2Fmusic_3.mp3?alt=media&token=d9648e4f-24ce-4776-9b41-f04a84aa138f");

                }
                else{
                    music3.setText("Play");
                    stopmusic();

                }

            }
        });
        music4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(music4.getText().toString().equalsIgnoreCase("Play")){
                    try{

                        stopmusic();

                    }
                    catch (Exception e){

                    }
                    music4.setText("Stop");
                    music2.setText("Play");
                    music3.setText("Play");
                    music1.setText("Play");
                    music5.setText("Play");
                    playmusic("https://firebasestorage.googleapis.com/v0/b/antibully-b7705.appspot.com/o/Songs%2Fmusic_4.mp3?alt=media&token=86c23515-b51d-448b-a14e-d15e2bae7961");

                }
                else{
                    music4.setText("Play");
                    stopmusic();

                }

            }
        });
        music5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(music5.getText().toString().equalsIgnoreCase("Play")){
                    try{

                        stopmusic();

                    }
                    catch (Exception e){

                    }
                    music5.setText("Stop");
                    music2.setText("Play");
                    music3.setText("Play");
                    music4.setText("Play");
                    music1.setText("Play");
                    playmusic("https://firebasestorage.googleapis.com/v0/b/antibully-b7705.appspot.com/o/Songs%2Fmusic_5.mp3?alt=media&token=f0ab7485-e23f-425e-ba19-98e3ee93211f");


                }
                else{
                    music5.setText("Play");
                    stopmusic();

                }

            }
        });



    }

    void playmusic(String url){
        progressDialog.setMessage("Get music from URL. Please wait");
        progressDialog.setTitle("Fetching data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Toast.makeText(getApplicationContext(), "Please wait loading music...", Toast.LENGTH_SHORT).show();
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(url);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        progressDialog.dismiss();
    }

    void stopmusic(){
        if (mediaPlayer.isPlaying()) {
            // pausing the media player if media player
            // is playing we are calling below line to
            // stop our media player.
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            // below line is to display a message
            // when media player is paused.
            //Toast.makeText(getApplicationContext(), "Music has been stopped", Toast.LENGTH_SHORT).show();
        }
    }


    void glidemusic(){
        Glide.with(this).load("https://i.pinimg.com/originals/f3/e1/bd/f3e1bd6cd32e4271bdf700c468a56074.jpg").into((ImageView) findViewById(R.id.mi_1));
        Glide.with(this).load("https://d2fdpghjkacqa1.cloudfront.net/wp-content/uploads/2019/02/Feb7_BlogHeader.jpg").into((ImageView) findViewById(R.id.mi_2));
        Glide.with(this).load("https://www.wallpapertip.com/wmimgs/8-82164_soothing-background.jpg").into((ImageView) findViewById(R.id.mi_3));
        Glide.with(this).load("https://i.pinimg.com/originals/7c/e5/c9/7ce5c92c35e16cd485a6db969f525f0d.jpg").into((ImageView) findViewById(R.id.mi_4));
        Glide.with(this).load("https://cdn.tricycle.org/wp-content/uploads/2014/09/soothing-coals-of-rage-1503x1125.jpg").into((ImageView) findViewById(R.id.mi_5));


    }


    void showdialogbox(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Boost_Confidence.this);

        // Setting Dialog Title
        alertDialog.setTitle("Information");
        alertDialog.setCancelable(true);

        // Setting Dialog Message
        alertDialog.setMessage("Playing music might take some time depending on your internet speed as music is fetched from a URL. So please wait for a while after you select the music.\nThankyou");

        // Setting Icon to Dialog




        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    void exercise_dialog(String h, String d, String url){
        try{
            dialog = new Dialog(Boost_Confidence.this );
            dialog.setContentView(R.layout.excercises_layout);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);

            TextView headings = dialog.findViewById(R.id.exercuse_heading);
            ImageView images = dialog.findViewById(R.id.image_exer);

            TextView description = dialog.findViewById(R.id.exercuse_description);
            headings.setText(h);
            Glide.with(Boost_Confidence.this).load(url).into(images);
            description.setText(d);
            dialog.show();


        }
        catch (Exception e){

        }





    }

    void exerciseimagessrc(){
        findViewById(R.id.exercise_txt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hd="Meditation";
                String des="You should sit in meditation for 20 minutes every day — unless you're too busy. Then you should sit for an hour. All kidding aside, it's best to start in small moments of time, even 5 or 10 minutes, and grow from there.";

                String url_imgv="https://i0.wp.com/post.healthline.com/wp-content/uploads/2020/09/meditation-eyes-closed-1296x728-header-1.jpg?w=1155&h=1528";
                exercise_dialog(hd,des,url_imgv);


            }
        });
        findViewById(R.id.exercise_txt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hd="Acupressure";
                String des="Apply pressure with your thumb at the point where your wrist forms a crease with your hand on the little finger side.";
                String url_imgv="http://wrightacupuncture.co.uk/wp-content/uploads/2016/10/IMG_2011web.jpg";
                exercise_dialog(hd,des,url_imgv);


            }
        });
        findViewById(R.id.exercise_txt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hd="Physical Activties";
                String des="Try doing some physical activties like running, walking, cycling. Have a quality time with your family members. Chill out with your friends. This will help you to reduce your anxiety";

                String url_imgv="https://images.theconversation.com/files/374811/original/file-20201214-23-12333ej.jpg?ixlib=rb-1.1.0&rect=11%2C5%2C3982%2C2652&q=45&auto=format&w=926&fit=clip";
                exercise_dialog(hd,des,url_imgv);


            }
        });





    }




}