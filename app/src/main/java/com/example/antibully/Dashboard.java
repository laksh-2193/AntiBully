package com.example.antibully;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.io.File;
import java.io.IOException;
import java.security.acl.Permission;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

public class Dashboard extends AppCompatActivity implements LocationListener {
    private FirebaseAuth firebaseAuth;
    TextView locationtxt;
    LocationRequest locationRequest;
    final int REQUEST_CHECK_SETTINGS = 1001;
    TextView username;
    int totalrelations=0;
    String address_get="";
    String txt2="";
    int lb=0;
    CardView last_location;
    String[] namesofreporters;
    String[] relationofreporters;
    String email_id_username, password_email_id;
    MediaRecorder mediaRecorder;
    ProgressDialog progressDialog;
    MediaPlayer mediaPlayer;
    TextView tlf;
    int checkalldata=0;



    LocationManager locationManager;
    Dialog dialog;
    String longtude, lattude;
    String start_date="";
    int alreadyopened=0;
    Button emergency_btn;
    String path_save, time_bully;
    String fn="";
    String file_name_recording;
    int appoint_check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        emergency_btn = findViewById(R.id.report_btn);
        last_location = findViewById(R.id.last_bullying_location);
        tlf = findViewById(R.id.time_left);
        email_id_username = "campaign.antibully2021@gmail.com";
        password_email_id="ANTIBULLY2021#";


        final FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        if (currentuser == null) {
            Intent intent = new Intent(Dashboard.this, OnBoarding.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();

        }
        else{
           // internetconnectioncheck();
            showProgressbarinapp();
            ActiveUsers();

            changename();
            askforalltedpermissions();


            grantPermission();
            messagefromadmins();
             locationtxt = findViewById(R.id.location_text);

            checklocationisenabled();
            getLocation();
            fetchdata();
            findViewById(R.id.community_page).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Dashboard.this,Community_Page.class));
                    overridePendingTransition(0,0);

                }
            });

            findViewById(R.id.my_reporters).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Dashboard.this,My_Reporters.class));
                    overridePendingTransition(0,0);
                }
            });
            findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Logout");
                    alertDialog.setCancelable(true);

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure that you want to logout ?");

                    // Setting Icon to Dialog




                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Dashboard.this,PhoneNumberInput.class));
                            finish();


                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
            });

            findViewById(R.id.counsellors).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Dashboard.this,Counsellor_Activity.class));
                    overridePendingTransition(0,0);
                }
            });
            findViewById(R.id.boost_self_conf).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Dashboard.this,Boost_Confidence.class));
                    overridePendingTransition(0,0);

                }
            });

            locationtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Dashboard.this,Dashboard.class));
                    overridePendingTransition(0,0);
                }
            });
            emergency_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    fn=String.valueOf(System.currentTimeMillis());
                    address_get = locationtxt.getText().toString();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    String dnt = (dateFormat.format(cal.getTime())).toString();
                    dnt = dnt.replace("/"," | ");
                    time_bully=dnt;

                    String bully_Req = username.getText().toString().trim()+" & "+dnt+" - "+address_get+" {"+lattude+","+longtude;

                    //https://www.google.com/maps?q=lattitude,longitude

                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Live_Bullying").child(fn).setValue(bully_Req);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Recent bullying").child("Recent").setValue(bully_Req);
                    showvoicerecordingdialog();
//                //twitter link - https://twitter.com/intent/tweet?text=<text string>
                    try{
                        if(relationofreporters.length==0){
                            Toast.makeText(getApplicationContext(),"Please add your reporters",Toast.LENGTH_SHORT).show();


                        }
                        else {
                            for(int i=0;i<relationofreporters.length;i++){
                                sendemail(relationofreporters[i],i+1);

                            }

                        }




                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                    }

                }
            });

            findViewById(R.id.nic_report).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //https://www.google.com/maps?q=lattitude,longitude
                    String url = "https://cybercrime.gov.in/Accept.aspx";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            findViewById(R.id.show_repoertss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Dashboard.this,Show_Reporters.class));
                    overridePendingTransition(0,0);

                }
            });
            findViewById(R.id.motivation_story).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Dashboard.this,Stories_By_People.class));
                    overridePendingTransition(0,0);

                }
            });



        }


    }





    void sendemail(String em, int x){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dnt = (dateFormat.format(cal.getTime())).toString();
        String adrs = locationtxt.getText().toString();
        String mail = em;
        String gps_loc = "https://www.google.com/maps?q="+lattude+","+longtude;


        String message="Hello,\nPlease reach out to "+username.getText().toString()+". As "+(username.getText().toString().trim()+" ").substring(0,username.getText().toString().indexOf(" "))+" reported a live bullying case on the App ANTI-BULLY on "+dnt+" at "+adrs+
                ". Voice recording must have been started if allowed to do so. Link to that place - "
                +gps_loc+"\nThankyou\nRegards\nTeam\nAnti-bully";
        String subject = "Alert-"+username.getText().toString()+" reported live bullying";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message,x);
        javaMailAPI.execute();


    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);



        }
        catch (SecurityException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    private void checklocationisenabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsenabled=false;
        boolean networkenabled=false;

        try{
            gpsenabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkenabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!gpsenabled && !networkenabled){
                new AlertDialog.Builder(Dashboard.this)
                        .setTitle("Enable GPS Service")
                        .setCancelable(false)
                        .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            }
                        })
                        .setNegativeButton("Disable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    Toast.makeText(getApplicationContext(),"enabling GPS is necessary to use the app",Toast.LENGTH_SHORT).show();


                                }
                                catch (Exception e){

                                }



                            }
                        })
                        .show();
            }

        }
        catch (Exception e){

        }
    }

    private void grantPermission() {
        if(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED  &&  ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);

        }
    }

    void messagefromadmins(){
        FirebaseDatabase.getInstance().getReference().child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Mentainance").getValue().toString().equalsIgnoreCase("0")){

                }
                else{
                    Toast.makeText(getApplicationContext(),"Signed out",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Dashboard.this,PhoneNumberInput.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    overridePendingTransition(0,0);
                    finish();
                }
                if(snapshot.child("Heading").getValue().toString().equalsIgnoreCase("none")){

                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(snapshot.child("Heading").getValue().toString());
                    alertDialog.setCancelable(true);

                    // Setting Dialog Message
                    alertDialog.setMessage(snapshot.child("Message").getValue().toString());

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onLocationChanged(Location location) {


        try {
            Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            locationtxt.setText(addresses.get(0).getAddressLine(0));
            longtude = Double.toString(addresses.get(0).getLongitude());
            lattude=Double.toString(addresses.get(0).getLatitude());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHECK_SETTINGS){
            switch (resultCode){
                case Activity.RESULT_CANCELED: {
                    turnonngps();

                }

            }
        }
    }

    void turnonngps(){
        try{
            locationRequest = LocationRequest.create();

            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setPriority(5000);
            locationRequest.setFastestInterval(2000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                    .checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);


                    } catch (ApiException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Location Permission needed - Closing the app",Toast.LENGTH_SHORT).show();
            finishAffinity();
            System.exit(0);

        }





    }
    void changename(){

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    String txt = snapshot.child("Name").getValue().toString();
                    username.setText(txt);

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Booked_Appoinments").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        try {
                            appoint_check=1;
                            txt2 = snapshot.child("Date_and_Time").getValue().toString();
                            start_date = txt2;
                            TextView txtvd = findViewById(R.id.txtvs);
                            txtvd.setText("Appoinment at-"+txt2);
                            update_date();


                        }
                        catch (Exception  e){
                            appoint_check=0;

                        }

                    }
                    else{
                        //Toast.makeText(getApplicationContext(),"No last bullying report",Toast.LENGTH_SHORT).show();
                        TextView txtvd = findViewById(R.id.txtvs);
                        txtvd.setText("No Current appoinments");

                    }
                if(appoint_check==0){
                    findViewById(R.id.cancel_appoin_btn).setVisibility(View.GONE);
                }
                else{
                    findViewById(R.id.cancel_appoin_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Cancel Appoinment");
                            alertDialog.setCancelable(true);

                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure that you want to cancel this appoinment?");
                            // Setting Icon to Dialog




                            // Setting Negative "NO" Button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference().child("Booked_Appoinments").child(firebaseAuth.getUid()).setValue(null);
                                    new GlideToast.makeToast(Dashboard.this,"Appoinment Cancelled", GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST).show();

                                    findViewById(R.id.cancel_appoin_btn).setVisibility(View.GONE);



                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }
                    });
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void fetchdata(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Reporters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    totalrelations = (int) snapshot.getChildrenCount();
                    namesofreporters = new String[totalrelations];
                    relationofreporters = new String[totalrelations];
                    int c = 0;
                    for(DataSnapshot datas: snapshot.getChildren()){

                        // Toast.makeText(getApplicationContext(),datas.getKey().toString(),Toast.LENGTH_SHORT).show();
                        String nm = datas.child("Name").getValue().toString();
                        String rel = datas.child("Email").getValue().toString();
                        namesofreporters[c] = nm;
                        relationofreporters[c]=rel;
                        c++;


                    }
                    //Toast.makeText(getApplicationContext(),"Data fetched",Toast.LENGTH_SHORT).show();
                    checkalldata=1;
                    if(relationofreporters.length==0){
                       // Toast.makeText(getApplicationContext(),"Please add your reporters",Toast.LENGTH_SHORT).show();




                    }

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


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    void showvoicerecordingdialog(){
        path_save = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+fn+"_bullying_recording.3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(path_save);





        try{





            mediaRecorder.prepare();
            mediaRecorder.start();

        }
        catch (IOException e){
           // Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();


        }







        dialog = new Dialog(Dashboard.this );
        dialog.setContentView(R.layout.sound_recorder);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button stop_recording = dialog.findViewById(R.id.stop_recording_btn);
        stop_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mediaRecorder.stop();
                    mediaRecorder.release();
                   // Toast.makeText(getApplicationContext(),"File Location - "+path_save,Toast.LENGTH_SHORT).show();
                    new GlideToast.makeToast(Dashboard.this,"File Location - "+path_save, GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST).show();
                    dialog.cancel();

//                    startActivity(new Intent(Dashboard.this,Dashboard.class));
//                    overridePendingTransition(0,0);
//                    finish();

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }

               // dialog.dismiss();
            }
        });
        dialog.show();


    }

    void askforalltedpermissions(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                askforalltedpermissions();

            }
        };
        TedPermission.with(Dashboard.this).setPermissionListener(permissionListener).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.INTERNET)
                .check();
    }


    void update_date() throws ParseException {
        try{
            //appoint_check=1;
            SimpleDateFormat sdf
                    = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm");
            Date date = new Date();
            Date d1 = sdf.parse(start_date);
            String dnf = sdf.format(date);
            Date d2 = sdf.parse(dnf);
            long difference_In_Time
                    = d1.getTime() - d2.getTime();

            // Calucalte time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;
            if(difference_In_Minutes<0 || difference_In_Days<0 || difference_In_Hours<0){
                tlf.setText("Appoinment Time elasped");
                tlf.setTextColor(Color.parseColor("#ffcccb"));

            }
            else{
                tlf.setTextColor(Color.parseColor("#98FB98"));

                tlf.setText(Long.toString(difference_In_Days)+" D : "+Long.toString(difference_In_Hours)+" H : "+Long.toString(difference_In_Minutes)+" M");


            }



        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }



    }
    @Override
    protected void onStart() {
        alreadyopened=0;
        super.onStart();
        ActiveUsers();
    }

    void ActiveUsers(){
       FirebaseDatabase.getInstance().getReference().child("Active").child(FirebaseAuth.getInstance().getUid()).setValue("Active");
    }

    void RemoveUsers(){
        try{
            FirebaseDatabase.getInstance().getReference().child("Active").child(FirebaseAuth.getInstance().getUid()).setValue(null);

        }
        catch (Exception e){

        }


    }

    @Override
    protected void onDestroy() {
        alreadyopened=0;
        super.onDestroy();
        RemoveUsers();
    }

    @Override
    protected void onPause() {
        alreadyopened=0;
        RemoveUsers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        alreadyopened=0;
        super.onResume();
        ActiveUsers();
    }

    @Override
    protected void onStop() {
        alreadyopened=0;
        //RemoveUsers();
        super.onStop();
    }

    void showProgressbarinapp(){
        final ProgressDialog progress = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progress.setTitle("Fetching Data");
        progress.setMessage("This may take a while depending on your network connectivity.");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        new CountDownTimer(25000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                String uname =username.getText().toString().trim();

                if(uname.equalsIgnoreCase("Welcome")){

                    progress.show();

                }
                else{
                    progress.dismiss();

                }

                if(millisUntilFinished<15000){
                    progress.setMessage("Internet connection is slow. Trying again");

                }
                if(millisUntilFinished<10000){
                    progress.setMessage("Still tring....");

                }
                if(millisUntilFinished<=5000){
                    progress.setMessage("Closing app due to network failure within "+(millisUntilFinished/1000));

                }

            }

            @Override
            public void onFinish() {
                if(username.getText().toString().trim().equalsIgnoreCase("Welcome")){
                    finishAffinity();
                    System.exit(0);


                }
                else{

                }


            }
        }.start();

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        alreadyopened=0;
        RemoveUsers();
        try{
            mediaRecorder.stop();
            mediaRecorder.release();

        }
        catch (Exception e){

        }
        finishAffinity();
        System.exit(0);
    }







}
