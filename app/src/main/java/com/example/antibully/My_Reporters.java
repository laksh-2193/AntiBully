package com.example.antibully;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import org.w3c.dom.Text;

public class My_Reporters extends AppCompatActivity {
    Dialog dialog;
    private FirebaseAuth firebaseAuth;
    int totalrelations=0;
    String[] namesofreporters;
    String[] relationofreporters;
    String[] storedrelation;
    int[] images = {R.drawable.user,R.drawable.user_2,R.drawable.user_3,R.drawable.user_4};
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__reporters);
        firebaseAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listview);
        try{
            Intent i = getIntent();
            if(i.getStringExtra("otp_act").equalsIgnoreCase("yes")){
                showdialogforadding();

            }

        }
        catch (Exception e){

        }

        fetchdata();
        findViewById(R.id.add_ret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogforadding();



            }
        });

        arrow_backpressed();




    }
    void showdialogforadding(){
        dialog = new Dialog(My_Reporters.this );
        dialog.setContentView(R.layout.add_reporters);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        final EditText relation = dialog.findViewById(R.id.relation);
        final EditText name = dialog.findViewById(R.id.name);
        final EditText email = dialog.findViewById(R.id.email);

        dialog.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(relation.getText().toString().trim().equalsIgnoreCase("") || name.getText().toString().trim().equalsIgnoreCase("") || relation.getText().toString().trim().equalsIgnoreCase(""))
                {
                    new GlideToast.makeToast(My_Reporters.this,"Please fill all details", GlideToast.LENGTHLONG,GlideToast.WARNINGTOAST).show();


                }
                else {
                    try{

                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Reporters").child(relation.getText().toString().trim().toUpperCase()).child("Name").setValue(name.getText().toString().trim());
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Reporters").child(relation.getText().toString().trim().toUpperCase()).child("Email").setValue(email.getText().toString().trim());
                        new GlideToast.makeToast(My_Reporters.this,"Person added", GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST).show();
                        dialog.cancel();
                      //  onBackPressed();


                    }
                    catch (Exception e){


                    }

                }


            }
        });

        dialog.show();

    }


    void fetchdata(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Reporters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalrelations = (int) snapshot.getChildrenCount();
                namesofreporters = new String[totalrelations];
                relationofreporters = new String[totalrelations];
                storedrelation = new String[totalrelations];
                int c = 0;
                for(DataSnapshot datas: snapshot.getChildren()){
                    try{

                        storedrelation[c] = datas.getKey().toString();
                        String nm = datas.child("Name").getValue().toString();
                        String rel = datas.child("Email").getValue().toString();
                        namesofreporters[c] = nm;
                        relationofreporters[c]=rel;
                        c++;

                    }
                    catch (Exception e){

                    }





                }
                try{
                    CustomAdapter customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position2, long id) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(My_Reporters.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Delete?");
                            alertDialog.setCancelable(true);

                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure that you want to delete this reporter?");
                            // Setting Icon to Dialog




                            // Setting Negative "NO" Button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try{
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Reporters").child(storedrelation[position2]).setValue(null);
                                        new GlideToast.makeToast(My_Reporters.this,"Person Deleted", GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST).show();


                                    }
                                    catch (Exception e){
                                       }



                                }
                            });
                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                    });

                }
                catch (Exception e){


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    class CustomAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return namesofreporters.length;
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
        public View getView(int position, View convertView, ViewGroup parent) {
           // position=(namesofreporters.length-1)-pos;

            View view = getLayoutInflater().inflate(R.layout.listview_layout,null);
            ImageView imgv = view.findViewById(R.id.imgv);
            int pint = position%images.length;
            imgv.setImageResource(images[pint]);
            TextView n = view.findViewById(R.id.name_rel);
            TextView e = view.findViewById(R.id.email_id_rel);
            n.setText(namesofreporters[position]);
            e.setText(relationofreporters[position]);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(My_Reporters.this,Dashboard.class);
        //intent.putExtra("otp_act","yes");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

                super.onBackPressed();

    }
}
