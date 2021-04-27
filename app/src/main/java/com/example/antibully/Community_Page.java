package com.example.antibully;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Community_Page extends AppCompatActivity {
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Uri imageuri;
    ImageView image_selected;
    int hasimage=0;
    String post_text;
    EditText thoughts;
    String url_fs;
    StorageTask uploadTask;
    String rf="", head="";
    EditText h;
    ListView listView;
    int totalposts=0;
    String[] headings;
    String[] texts;
    String[] images;
    String[] keyarea;

    TextView activeusers, tposts;
    int position;
    int sh=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community__page);
        firebaseAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listview);
        image_selected = findViewById(R.id.post_img);
        thoughts = findViewById(R.id.thoughts);
        activeusers = findViewById(R.id.active_users);
        tposts = findViewById(R.id.total_posts);


        h = findViewById(R.id.title);

        storageReference = FirebaseStorage.getInstance().getReference("Post_Images");

        findViewById(R.id.Upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Post upload is in progress",Toast.LENGTH_SHORT).show();
                }
                else{
                    rf = System.currentTimeMillis()+"";
                    FileUploader();

                }

            }
        });
        final TextView sfjks = findViewById(R.id.hider);
        findViewById(R.id.hider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sh++;
                if(sh%2==0){
                    sfjks.setText("Hide Post Editor");
                    findViewById(R.id.cards).setVisibility(View.VISIBLE);

                }
                else{

                    sfjks.setText("Show Post Editor");
                    findViewById(R.id.cards).setVisibility(View.GONE);

                }
            }
        });

        findViewById(R.id.choose_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        try{
            fetchdata();

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Message ID 100 - "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        ActiveUsers();
    }

    void ActiveUsers(){
        FirebaseDatabase.getInstance().getReference().child("Active").child(FirebaseAuth.getInstance().getUid()).setValue("Active");
    }

    void RemoveUsers(){
        FirebaseDatabase.getInstance().getReference().child("Active").child(FirebaseAuth.getInstance().getUid()).setValue(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RemoveUsers();
    }

    @Override
    protected void onPause() {
        RemoveUsers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActiveUsers();
    }

    @Override
    protected void onStop() {
        //RemoveUsers();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        RemoveUsers();
        super.onBackPressed();
    }

    void chooseimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void FileUploader(){
        post_text = thoughts.getText().toString().trim();
        head = h.getText().toString().trim();

        try{
            if(post_text.length()==0 || head.length()==0){
                Toast.makeText(getApplicationContext(),"Please write post",Toast.LENGTH_SHORT).show();

            }
            else{
                FirebaseDatabase.getInstance().getReference().child("Community").child(String.valueOf(rf+"_post")).child("Post_text").setValue(post_text);
                FirebaseDatabase.getInstance().getReference().child("Community").child(String.valueOf(rf+"_post")).child("Post_heading").setValue(head);
                FirebaseDatabase.getInstance().getReference().child("Community").child(String.valueOf(rf+"_post")).child("Post_image").setValue("no_img");


                if(hasimage==1){
                    StorageReference Ref = storageReference.child(System.currentTimeMillis()+"."+getExtension(imageuri));
                    uploadTask = Ref.putFile(imageuri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isComplete());
                                    url_fs = uriTask.getResult().toString();
                                    FirebaseDatabase.getInstance().getReference().child("Community").child(String.valueOf(rf+"_post")).child("Post_image").setValue(url_fs);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();


                                }
                            });
                    startActivity(new Intent(Community_Page.this,Community_Page.class));
                    overridePendingTransition(0,0);


                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Community").child(String.valueOf(rf+"_post")).child("Post_image").setValue("no_img");
                    startActivity(new Intent(Community_Page.this,Community_Page.class));
                    overridePendingTransition(0,0);


                }



            }

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Message ID 101 - "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

    }
    void fetchdata(){
        FirebaseDatabase.getInstance().getReference().child("Community").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    totalposts = (int) snapshot.getChildrenCount();
                    headings = new String[totalposts];
                    texts = new String[totalposts];
                    images = new String[totalposts];
                    keyarea = new String[totalposts];
                    tposts.setText(totalposts+" posts");

                    int c = 0;
                    for(DataSnapshot datas: snapshot.getChildren()){
                        // Toast.makeText(getApplicationContext(),datas.getKey().toString(),Toast.LENGTH_SHORT).show();
                        String hd = datas.child("Post_heading").getValue().toString();
                        String ig = datas.child("Post_image").getValue().toString();
                        String txt = datas.child("Post_text").getValue().toString();
                        keyarea[c] = datas.getKey().toString();
                        headings[c] = hd;
                        texts[c]=txt;
                        images[c]=ig;



                        c++;


                    }

                }
                catch (Exception e)
                {
                    //Toast.makeText(getApplicationContext(),"Message ID 102 - "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }

               // Toast.makeText(getApplicationContext(),Integer.toString(totalposts),Toast.LENGTH_SHORT).show();
                try{
                    CustomAdapter customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Message ID 103 - "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }

                try{
                    FirebaseDatabase.getInstance().getReference().child("Active").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int num = (int) snapshot.getChildrenCount();

                            activeusers.setText(num+" users Active");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                catch (Exception e){

                }

                //Toast.makeText(getApplicationContext(),Integer.toString(totalrelations),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            hasimage=1;
            imageuri=data.getData();
            image_selected.setImageURI(imageuri);
        }
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return headings.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            position=(headings.length-1)-pos;

            View view = getLayoutInflater().inflate(R.layout.post_view,null);

            final ImageView imgv = view.findViewById(R.id.post_image);
            final LottieAnimationView like = view.findViewById(R.id.like_btn);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.playAnimation();
                   // Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Community").child(keyarea[position]+"").child("likes").child(FirebaseAuth.getInstance().getUid()).setValue("liked");

                }
            });
            TextView hea = view.findViewById(R.id.heading);
            TextView des = view.findViewById(R.id.post_description);
            final TextView likes = view.findViewById(R.id.total_likes);
            try{
                FirebaseDatabase.getInstance().getReference().child("Community").child(keyarea[position]).child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            likes.setText(snapshot.getChildrenCount()+" likes");

                        }
                        catch (Exception e){

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            catch (Exception e){

            }



            hea.setText("\n"+headings[position]);
            des.setText(texts[position]+"\n");
            if(images[position].equalsIgnoreCase("no_img")){
                imgv.setVisibility(View.GONE);
                try{
                    view.findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, headings[position]);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, headings[position]+"\n"+texts[position]);
                            startActivity(Intent.createChooser(sharingIntent, "Share text via"));

                        }
                    });


                }
                catch (Exception e){

                }


            }
            else {
                Glide.with(Community_Page.this)
                        .load(images[position])
                        .into(imgv);
                try{
                    view.findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            BitmapDrawable drawable = (BitmapDrawable)imgv.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            String bitmappath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"title","");
                            Uri uri = Uri.parse(bitmappath);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_STREAM,uri);
                            intent.putExtra(Intent.EXTRA_TEXT,headings[position]+"\n"+texts[position]+"\n"+images[position]+"\nPost shared from app - \nAntiBully");
                            startActivity(Intent.createChooser(intent,"Share via."));

                        }
                    });

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }

            }

//            ImageView imgv = view.findViewById(R.id.imgv);
//
//            int pint = position%images.length;
//
//
//            imgv.setImageResource(images[pint]);
//
//            TextView n = view.findViewById(R.id.name_rel);
//            TextView e = view.findViewById(R.id.email_id_rel);
//            n.setText(namesofreporters[position]);
//            e.setText(relationofreporters[position]);
            return view;

        }
    }
}
