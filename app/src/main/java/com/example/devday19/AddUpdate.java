package com.example.devday19;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddUpdate extends AppCompatActivity {
    private static final int REQUEST_FINE_LOCATION = 100;
    Spinner quantity;
    Button place, location;
    LocationManager locationManager;
    LocationListener listener;
    String address;
    ImageButton send;
    EditText Input;
    TextView locTxt;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);
        location = findViewById(R.id.location);
        locTxt = findViewById(R.id.loc_txt);
        place = findViewById(R.id.place);
        send = findViewById(R.id.send);
        Input = findViewById(R.id.input);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        quantity = findViewById(R.id.quantity_spinner);
        ArrayList<String> heads = new ArrayList<>();
        heads.add("Road Block");
        heads.add("Road Construction");
        heads.add("Accident");
        heads.add("PSL");
        heads.add("VIP Movement");

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.quantity,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantity.setAdapter(adapter);*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, heads);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantity.setAdapter(adapter);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Tag", location + "");
                updateLocationInfo(location);
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
        };
        locationUpdater();
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i("Tag", location + "");
                        locationUpdater();
                        updateLocationInfo(location);
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
                };
            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sending(Input.getText().toString(),locTxt.getText().toString(),quantity.getSelectedItem().toString());

            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("AddUpdate");
        //Offline
        mDatabaseReference.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sending(Input.getText().toString(),locTxt.getText().toString(),quantity.getSelectedItem().toString());

            }
        });
    }

    private void Sending(final String details, final String loc, final String type) {

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int code =1;
                int size = (int) dataSnapshot.getChildrenCount();
                int finalcode = code+size;
                final String Code = String.valueOf(finalcode);

                String useremail = mAuth.getCurrentUser().getEmail();


                mDatabaseReference.child(Code).child("email").setValue(useremail);
                mDatabaseReference.child(Code).child("Location").setValue(loc);
                mDatabaseReference.child(Code).child("Time").setValue(getTime());
                mDatabaseReference.child(Code).child("Details").setValue(details);
                mDatabaseReference.child(Code).child("type").setValue(type).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddUpdate.this, "Update has been Uploaded", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void updateLocationInfo(Location location) {
        double lat = location.getLatitude();
        double log = location.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, log, 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                address = "";
                if (listAddresses.get(0).getAddressLine(0) != null) {
                    address += listAddresses.get(0).getAddressLine(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        locTxt.setText(address);

    }

    private void locationUpdater() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, listener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    updateLocationInfo(lastKnownLocation);
                    locationManager.removeUpdates(listener);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationUpdater();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                }
            }
        }
    }

    public static String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.d("date", "watermark: " + formattedDate);
        return formattedDate;
    }
}