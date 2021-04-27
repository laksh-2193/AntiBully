package com.example.antibully;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Counsellor_Activity extends AppCompatActivity {
    TextView phone, dateandtime, remarks;
    private static final int CREDENTIAL_PICKER_REQUEST=120;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_);
        phone = findViewById(R.id.phone_no);
        remarks = findViewById(R.id.special_remarks);
        dateandtime = findViewById(R.id.dateandtime);

        dateandtime.setInputType(InputType.TYPE_NULL);



       // phone.setInputType(InputType.TYPE_NULL);
        HintRequest hintRequest=new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent= Credentials.getClient(Counsellor_Activity.this).getHintPickerIntent(hintRequest);
        try{
            startIntentSenderForResult(intent.getIntentSender(),CREDENTIAL_PICKER_REQUEST,null,0,0,0,new Bundle());

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }





        dateandtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateandtimepicker();
            }
        });

        findViewById(R.id.schedule_appont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().trim().length()==0 || phone.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();

                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Booked_Appoinments").child(FirebaseAuth.getInstance().getUid()).child("Phone").setValue(phone.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Booked_Appoinments").child(FirebaseAuth.getInstance().getUid()).child("Date_and_Time").setValue(dateandtime.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Booked_Appoinments").child(FirebaseAuth.getInstance().getUid()).child("Special remarks").setValue(remarks.getText().toString());
                    Toast.makeText(getApplicationContext(),"Appoinment Booked",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();

                }






            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CREDENTIAL_PICKER_REQUEST && resultCode==RESULT_OK){
            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            phone.setText(credential.getId().substring(3));
            try{
                checkdata();

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }

        }
        else{
            try{
                checkdata();

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }

        }
    }


    void dateandtimepicker(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        dateandtime.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                };
                new TimePickerDialog(Counsellor_Activity.this,timeSetListener,calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MINUTE),true).show();


            }
        };
        new DatePickerDialog(Counsellor_Activity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


   void checkdata(){
        try{
            FirebaseDatabase.getInstance().getReference().child("Booked_Appoinments").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        try{
                            String dnt=snapshot.child("Date_and_Time").getValue().toString();
                            dialog = new Dialog(Counsellor_Activity.this );
                            dialog.setContentView(R.layout.counsellor_check_dialog_box);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false);
                            TextView txtv = dialog.findViewById(R.id.txtv);
                            txtv.setText("Your appoinment is already booked. Please wait till "+dnt+", our counsellors will call you. You can proceed only if you want to reschedule the timings.");
                            dialog.findViewById(R.id.schedule_appont2).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();

                        }
                        catch (Exception e){

                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();


        }
    }


}
