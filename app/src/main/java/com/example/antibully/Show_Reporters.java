package com.example.antibully;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Show_Reporters extends AppCompatActivity {
    String[] keynames;
    String[] texts_key;
    String[] dnt;
    String[] locations;
    String[] gps_location;
    String path_save;

    private FirebaseAuth firebaseAuth;
    int totalrelations=0;
    ListView listView;
    Dialog dialog;
    int images[]={R.drawable.bully_2,R.drawable.bully_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__reporters);
        firebaseAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listview);
        try{
            fetchdata();
            arrow_backpressed();

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }


    }
    void fetchdata(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Live_Bullying").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    totalrelations = (int) snapshot.getChildrenCount();
                    keynames = new String[totalrelations];
                    texts_key= new String[totalrelations];
                    dnt = new String[totalrelations];
                    locations = new String[totalrelations];
                    int c = 0;
                    for(DataSnapshot datas: snapshot.getChildren()){

                        // Toast.makeText(getApplicationContext(),datas.getKey().toString(),Toast.LENGTH_SHORT).show();
//                    String nm = datas.child("Name").getValue().toString();
//                    String rel = datas.child("Email").getValue().toString();
//                    namesofreporters[c] = nm;
//                    relationofreporters[c]=rel;
                        String key=datas.getKey().toString();
                        String tk = datas.getValue().toString();

                        keynames[c] = key;
                        texts_key[c] = tk;
                        locations[c]=texts_key[c].substring(texts_key[c].indexOf("-")+1,texts_key[c].lastIndexOf("{")).trim();
                        dnt[c]=texts_key[c].substring(texts_key[c].indexOf("&")+1,texts_key[c].indexOf("-")).trim();




                        c++;


                    }
                    keynames = reversearray(keynames);
                    texts_key = reversearray(texts_key);
                    locations = reversearray(locations);
                    dnt = reversearray(dnt);


                }
                catch (Exception e){
                   // Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }

                try{
                    CustomAdapter customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                            try{
                                path_save = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+keynames[position2]+"_bullying_recording.3gp";
                                Toast.makeText(getApplicationContext(),"File Location - "+path_save,Toast.LENGTH_SHORT).show();
                                MediaPlayer mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(path_save);
                                mediaPlayer.prepare();
                                mediaPlayer.start();









                            }
                            catch (Exception e){
                                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(getApplicationContext(),Integer.toString(totalrelations),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    String[] reversearray(String[] arr){
        int j=totalrelations-1;
        String[] temp = new String[totalrelations];

        for(int i = 0;i<totalrelations;i++){
            temp[i] = arr[i];

        }

        for(int i=0;i<totalrelations;i++){

            arr[j]=temp[i];
            j--;

        }


        return arr;



    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dnt.length;
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
        public View getView(final int position, View convertView, ViewGroup parent) {


           // position=(dnt.length-1)-pos;
           // Toast.makeText(getApplicationContext(),position+"-"+keynames[position],Toast.LENGTH_SHORT).show();


            View view = getLayoutInflater().inflate(R.layout.listview_layout,null);
            ImageView imgv = view.findViewById(R.id.imgv);




            imgv.setImageResource(R.drawable.recording_coice);


            TextView n = view.findViewById(R.id.name_rel);
            TextView e = view.findViewById(R.id.email_id_rel);
            e.setTextColor(Color.parseColor("#3949AB"));
            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String txt2 = texts_key[position];
                    String longitude = txt2.substring(txt2.indexOf("{")+1,txt2.lastIndexOf(",")).trim();
                    String lattitude = txt2.substring(txt2.lastIndexOf(",")+1,txt2.length()-1);
                    String url = "https://www.google.com/maps?q="+longitude+","+lattitude;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            n.setText(dnt[position]);
            e.setText(locations[position]+"\n");

            return view;

        }
    }
    void arrow_backpressed(){
        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
