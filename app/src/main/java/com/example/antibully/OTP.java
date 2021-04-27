package com.example.antibully;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OTP extends AppCompatActivity {
    Button otp_verity;
    EditText otp_message;
    TextView waitmessage;
    String otp, phone_number;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    Button verify_btn2;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        otp = getIntent().getStringExtra("auth");
        phone_number = getIntent().getStringExtra("phoneNo");
        otp_message = findViewById(R.id.otp);
        waitmessage = findViewById(R.id.progress_text);
        firebaseAuth = FirebaseAuth.getInstance();
        verify_btn2 = findViewById(R.id.verify_btn);
        findViewById(R.id.verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closekeyboard();
                verify_btn2.setEnabled(false);
                verify_btn2.setText("Please wait...");
                findViewById(R.id.progress_circular).setVisibility(View.VISIBLE);
                waitmessage.setVisibility(View.VISIBLE);
                waitmessage.setText("Please wait..");
                waitmessage.setTextColor(Color.DKGRAY);
                if(otp_message.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(OTP.this,"Please enter OTP",Toast.LENGTH_LONG).show();


                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp,otp_message.getText().toString());
                    signin(credential);


                }
                verify_btn2.setEnabled(true);
                verify_btn2.setText("Verify OTP");





            }
        });

    }
    void signin(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(OTP.this,"",Toast.LENGTH_LONG).show();

                    addname();




                }
                else{
                    //Toast.makeText(OTP_Verification.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    waitmessage.setText("Failed to verify OTP : "+task.getException().getMessage().toString());
                    waitmessage.setTextColor(Color.RED);
                    findViewById(R.id.progress_circular).setVisibility(View.INVISIBLE);


                }
            }
        });
        verify_btn2.setEnabled(true);
        verify_btn2.setText("Verify OTP");



    }


    void closekeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    void addname(){
        try{
            dialog = new Dialog(OTP.this );
            dialog.setContentView(R.layout.name_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            final EditText name = dialog.findViewById(R.id.name);
            Button cont = dialog.findViewById(R.id.contin);
            cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please enter name",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Name").setValue(name.getText().toString().trim());
                        Intent intent = new Intent(OTP.this,My_Reporters.class);
                        intent.putExtra("otp_act","yes");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                        overridePendingTransition(0,0);
                        finish();
                    }
                }
            });
            dialog.show();

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            finishAffinity();
            System.exit(0);

        }
        catch (Exception e){

        }
    }
}
