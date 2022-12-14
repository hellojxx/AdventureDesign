package kr.ac.cnu.computer.avddddd;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.view.View;
import android.os.Looper;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;


import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import android.provider.Settings;
import noman.googleplaces.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import noman.*;
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener{
    private GoogleMap mMap;
    private Marker currentMarker=null;

    private static final String TAG="googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE=2001;
    private static final int UPDATE_INTERVAL_MS=1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS=500;
    private static final int PERMISSIONS_REQUEST_CODE=100;
    boolean needRequest=false;
    String[] REQUIRED_PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION};
    Location mCurrentLocation;
    LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private View mLayout;
    List<Marker> previous_marker=null;
    String targetUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=" +
            "AIzaSyCD6jNi6yonzHPBcABOur8nwkIQ0uSe6Mc&location=";
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_map);
        previous_marker=new ArrayList<Marker>();
        mLayout=findViewById(R.id.layout_map);
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mFusedLocationClient=LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceInformation(currentPosition);
                String urlStr = targetUrl+""+currentPosition.latitude+","+currentPosition.longitude+
                        "&radius=1000&language=ko&type=restaurant";
                PlaceThread pt = new PlaceThread(urlStr);
                pt.start();
                try {
                    pt.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(MapActivity.this, "주변가게를 불러왔습니다.", Toast.LENGTH_LONG).show();
                json = pt.sb.toString();
            }
        });
        RelativeLayout search_bar=findViewById(R.id.search_bar);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this, MarketList.class);
                if(json!=null){
                    intent.putExtra("json", json);
                }
                startActivity(intent);
            }
        });
    }

    private void startLocationUpdates() {
        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else{
            int hasFineLocationPermission=ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission=ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if(hasFineLocationPermission!=PackageManager.PERMISSION_GRANTED||
            hasCoarseLocationPermission!=PackageManager.PERMISSION_GRANTED){
                return;
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                            location.getLongitude()), 17));;
                    currentPosition=new LatLng(location.getLatitude(), location.getLongitude());
                }
            });
            if(checkPermission())
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap=googleMap;
        setDefaultLocation();
        int hasFineLocationPermission=ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission=ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission==PackageManager.PERMISSION_GRANTED&&
        hasCoarseLocationPermission==PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            }else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull @NotNull LatLng latLng) {
            }
        });
    }
    /*LocationCallback locationCallback=new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locationList=locationResult.getLocations();

            if(locationList.size()>0){
                location=locationList.get(locationList.size()-1);
                currentPosition=new LatLng(location.getLatitude(), location.getLongitude());

                // 카메라 움직이기
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(),
                                location.getLongitude()), 17));

                mCurrentLocation=location;
            }
        }
    };
*/
    public String getCurrentAddress(LatLng latLng){
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses=geocoder.getFromLocation(latLng.latitude,
                    latLng.longitude,
                    1);
        }catch (IOException ioException){
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            return "";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            return "";
        }
        if(addresses==null|| addresses.size()==0){
            return "";
        }else {
            Address address=addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }
    public boolean checkLocationServicesStatus(){
        LocationManager locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet){
        if(currentMarker!=null) currentMarker.remove();

        LatLng currentLatLng=new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker=mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);
    }
    public void setDefaultLocation(){
        LatLng DEFAULT_LOCATION=new LatLng(37.56, 125.97);
        String markerTitle="위치정보 가져올 수 없음";
        String markerSnippet="위치 퍼미션과 GPS 활성 요부 확인하세요";

        if(currentMarker!=null) currentMarker.remove();
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker=mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);
    }
    private boolean checkPermission(){
        int hasFineLocationPermission=ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission=ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if(hasFineLocationPermission==PackageManager.PERMISSION_GRANTED&&
        hasCoarseLocationPermission==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE&&
                grantResults.length==REQUIRED_PERMISSIONS.length) {
            boolean check_result=true;
            for(int result:grantResults){
                if(result!=PackageManager.PERMISSION_GRANTED){
                    check_result=false;
                    break;
                }
            }
            if(check_result){
                startLocationUpdates();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])){
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }else{
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야합니다.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
        +"위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent
                        =new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case  GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()){
                    if(checkLocationServicesStatus()){
                        needRequest=true;
                        return;
                    }
                }
                break;
        }
    }
    @Override
    public void onPlacesFailure(PlacesException e) {
    }
    @Override
    public void onPlacesStart() {
    }
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(noman.googleplaces.Place place: places){
                    LatLng latLng=new LatLng(place.getLatitude(), place.getLongitude());

                    String markerSnippet=getCurrentAddress(latLng);
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);
                    Marker item=mMap.addMarker(markerOptions);
                    previous_marker.add(item);
                }
                HashSet<Marker> hashSet=new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);
            }
        });

    }
    @Override
    public void onPlacesFinished() {
    }
    public void showPlaceInformation(LatLng location){
        mMap.clear();
        if(previous_marker!=null){
            previous_marker.clear();
            new NRPlaces.Builder()
                    .listener(MapActivity.this)
                    .key("AIzaSyCD6jNi6yonzHPBcABOur8nwkIQ0uSe6Mc")
                    .latlng(location.latitude, location.longitude)
                    .radius(1000)
                    .type(PlaceType.RESTAURANT)
                    .language("ko", "KR")
                    .build().execute();
        }
    }
}