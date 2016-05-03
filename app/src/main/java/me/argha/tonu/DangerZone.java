package me.argha.tonu;

import android.app.Dialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DangerZone extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final int MY_LOCATION_REQUEST_CODE = 3;
    private GoogleMap googleMap = null;
    public String tag = "TAG_";
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_danger_zone);

        setUpGoogleApiClient();
        initMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!googleApiClient.isConnected()){
            googleApiClient.connect();
        }
    }@Override
    protected void onResume() {
        super.onResume();
        if(!googleApiClient.isConnected()){
            googleApiClient.connect();
        }
    }

    private void initMap() {

        if (googleMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);


//            if (googleMap == null) {
//                Toast.makeText(MapActivity.this, "Failed to create google map", Toast.LENGTH_SHORT).show();
//                return;
//            }


        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        map.setMyLocationEnabled(true);
        LatLng dangerCoordinates= new LatLng(23.8103,90.4125);
        setUpMarkersAndMap(dangerCoordinates);
    }

    private void setUpMarkersAndMap(LatLng dangerCoordinates) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dangerCoordinates, 14));
        googleMap.addMarker(new MarkerOptions()
                .position(dangerCoordinates)
                .title("Hijacking")
                .draggable(false));
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            EditText editText;
            LatLng latLng;
            @Override
            public void onMapLongClick(LatLng latLng) {
//                    Dialog tempDialog;
                Dialog dialog = new Dialog(DangerZone.this);
                this.latLng=latLng;
                initDialog(dialog,latLng);
//                    tempDialog = dialog;
                dialog.show();

            }
            private void initDialog(final Dialog dialog, LatLng latLng) {
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Additionals");
                TextView dialogText = (TextView) dialog.findViewById(R.id.dialog_display_text);
                Button submit = (Button) dialog.findViewById(R.id.btn_1);
                editText= (EditText) dialog.findViewById(R.id.editText_dialog);
                dialogText.setText("Please provide detail");
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDangerDetail();
                        dialog.dismiss();
                    }
                });


            }

            private void getDangerDetail() {
                String str= editText.getText().toString();
                if(str==null || str.equals("")){
                    return;
                }
                googleMap.addMarker(new MarkerOptions()
                        .title(str)
                        .position(latLng)
                .draggable(false));
            }
        });
    }

    private void setUpGoogleApiClient() {
        googleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if(!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
}
