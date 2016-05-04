package me.argha.tonu;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import me.argha.tonu.app.EndPoints;
import me.argha.tonu.helpers.MyPreferenceManager;
import me.argha.tonu.utils.Util;

public class DangerZone extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int MY_LOCATION_REQUEST_CODE = 3;
    private GoogleMap googleMap = null;
    public String tag = "TAG_";
    private GoogleApiClient googleApiClient;
    MyPreferenceManager preferenceManager;
    Context context;
    Set<String> dangers;
    static AsyncHttpClient asyncHttpClient;
    RequestParams params;
    ArrayList<LatLng> dangerData;
    HeatmapTileProvider heatmapTileProvider;
    TileOverlay overlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_danger_zone);


        setUpGoogleApiClient();

        init();


    }

    private void init() {
        preferenceManager= new MyPreferenceManager(this);
        context=this;
        asyncHttpClient= new AsyncHttpClient();
        updateDangerHistory();
        if(dangers==null){
            dangers= preferenceManager.pref.getStringSet("dangerHistory",new
                    HashSet<String>());
        }
        if(dangerData==null || dangerData.size()==0){
            dangerData= new ArrayList<LatLng>();
            for(String loc: dangers){
                LatLng latLng= Util.getLatLon(loc);
                dangerData.add(latLng);
            }
        }
        setData();
    }

    private void updateDangerHistory() {

        asyncHttpClient.get(EndPoints.GETDANGERZONES, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("error") == false) {
                        Set<String> dangers=saveAllDangerZones(response.getJSONArray
                                ("danger_zones"));

                        preferenceManager.editor.putStringSet("dangerHistory",dangers);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private Set<String> saveAllDangerZones(JSONArray danger_zones) throws JSONException {
        Set<String> dangers= preferenceManager.pref.getStringSet("dangerHistory",new HashSet<String>());

        for(int i=0; i<danger_zones.length(); i++){
            String location= danger_zones.getString(i);
            dangers.add(location);
        }
        return dangers;
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
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
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

    private void setData() {
//        dangerData.add(new LatLng(23.802092176340878,90.40835995227098));
        if(dangerData.size()<=0)
            return;
        if(heatmapTileProvider==null){

            // Create a heat map tile provider, passing it the latlngs of the police stations.
            heatmapTileProvider = new HeatmapTileProvider.Builder()
                    .data(dangerData)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            overlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider
                    (heatmapTileProvider));
//            overlay.clearTileCache();
        }
        else{
            heatmapTileProvider.setData(dangerData);
            heatmapTileProvider.setRadius(40);
            overlay.clearTileCache();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        map.setMyLocationEnabled(true);
//        LatLng dangerCoordinates= new LatLng(23.8103,90.4125);
        setUpMarkersAndMap();
    }

    private void setUpMarkersAndMap() {
        LatLng myLocation=getMyLocation();
        Log.e("DANGERZOME", myLocation.latitude+","+myLocation.longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
//        googleMap.addMarker(new MarkerOptions()
//                .position(dangerCoordinates)
//                .title("Hijacking")
//                .draggable(false));
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
                        setDangerDetail();
                        dialog.dismiss();
                    }
                });


            }

            private void setDangerDetail() {
                String str= editText.getText().toString();
                if(str==null || str.equals("")){
                    return;
                }
                Marker danger= googleMap.addMarker(new MarkerOptions()
                        .title(str)
                        .position(latLng)
                        .draggable(false));

                params= new RequestParams();
                //TODO change sbs to dynamic
                params.put("user_id","sbs");
                String location= Util.getLatLonString(danger.getPosition());
                Log.e("DANGERZONE","location: "+location);
                params.put("location",location);
                asyncHttpClient.post(EndPoints.ADDDANGER,params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            Log.e("DANGERZONE",response.toString(4));
                            if(response.getBoolean("error")==false){
                                Util.showToast(context,"Danger added to database");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private LatLng getMyLocation() {
        Location myLoc= LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);
        LatLng loc=null;
        if (myLoc != null) {
            double latitude = myLoc.getLatitude();
            double longitude = myLoc.getLongitude();
            loc= new LatLng(latitude, longitude);
        }
        Log.e("DANGERZONE","myLoc is null");
        return loc;
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
        initMap();
        setUpMarkersAndMap();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
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
