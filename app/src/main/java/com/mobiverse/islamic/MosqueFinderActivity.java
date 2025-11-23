package com.mobiverse.islamic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobiverse.MobiVerse.R;

import java.util.ArrayList;
import java.util.List;

public class MosqueFinderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MosqueFinder";
    private static final int LOCATION_PERMISSION_REQUEST = 101;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    private RecyclerView rvMosques;
    private MosqueAdapter mosqueAdapter;
    private List<Mosque> mosqueList;

    // Prayer times TextViews
    private TextView tvFajr, tvDhuhr, tvAsr, tvMaghrib, tvIsha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque_finder);

        // Setup toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize UI
        initializeViews();
        setupMap();
        setupRecyclerView();
        checkLocationPermission();
        loadPrayerTimes();

        // FAB listeners
        findViewById(R.id.fab_my_location).setOnClickListener(v -> moveToCurrentLocation());
        findViewById(R.id.fab_refresh).setOnClickListener(v -> refreshMosques());
        findViewById(R.id.fab_ai_assistant).setOnClickListener(v -> openAIAssistant());
        findViewById(R.id.btn_qibla).setOnClickListener(v -> openQiblaCompass());
    }

    private void initializeViews() {
        tvFajr = findViewById(R.id.tv_fajr);
        tvDhuhr = findViewById(R.id.tv_dhuhr);
        tvAsr = findViewById(R.id.tv_asr);
        tvMaghrib = findViewById(R.id.tv_maghrib);
        tvIsha = findViewById(R.id.tv_isha);
        rvMosques = findViewById(R.id.rv_mosques);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupRecyclerView() {
        mosqueList = new ArrayList<>();
        mosqueAdapter = new MosqueAdapter(mosqueList, this::onMosqueNavigate, this::onMosqueFavorite);
        rvMosques.setLayoutManager(new LinearLayoutManager(this));
        rvMosques.setAdapter(mosqueAdapter);
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLocation = location;
                            updateMapLocation();
                            fetchNearbyMosques(location.getLatitude(), location.getLongitude());
                        }
                    });
        }
    }

    private void updateMapLocation() {
        if (mMap != null && currentLocation != null) {
            LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f));
            mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
        }
    }

    private void fetchNearbyMosques(double lat, double lng) {
        // TODO: Call Google Places API or your backend
        // For now, mock data
        mosqueList.clear();
        mosqueList.add(new Mosque("Central Mosque", "123 Main St", lat + 0.01, lng + 0.01, 0.8, 12));
        mosqueList.add(new Mosque("Masjid Al-Noor", "456 Oak Ave", lat - 0.02, lng + 0.02, 1.5, 20));
        mosqueList.add(new Mosque("Islamic Center", "789 Pine Rd", lat + 0.03, lng - 0.01, 2.1, 28));
        
        mosqueAdapter.notifyDataSetChanged();
        addMosqueMarkersToMap();
    }

    private void addMosqueMarkersToMap() {
        if (mMap != null) {
            for (Mosque mosque : mosqueList) {
                LatLng position = new LatLng(mosque.getLatitude(), mosque.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(mosque.getName()));
            }
        }
    }

    private void loadPrayerTimes() {
        // TODO: Use Aladhan API or local calculation
        // Mock data for now
        tvFajr.setText("05:15");
        tvDhuhr.setText("12:30");
        tvAsr.setText("15:45");
        tvMaghrib.setText("18:20");
        tvIsha.setText("19:45");
    }

    private void moveToCurrentLocation() {
        getCurrentLocation();
    }

    private void refreshMosques() {
        if (currentLocation != null) {
            fetchNearbyMosques(currentLocation.getLatitude(), currentLocation.getLongitude());
            Toast.makeText(this, "Refreshing nearby mosques...", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAIAssistant() {
        // TODO: Open AI chat bottom sheet with mosque context
        Toast.makeText(this, "AI Assistant: Ask about mosques, prayer times, Qibla direction", Toast.LENGTH_SHORT).show();
    }

    private void openQiblaCompass() {
        // TODO: Launch Qibla compass activity
        Toast.makeText(this, "Opening Qibla Compass...", Toast.LENGTH_SHORT).show();
    }

    private void onMosqueNavigate(Mosque mosque) {
        // Open Google Maps navigation
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mosque.getLatitude() + "," + mosque.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void onMosqueFavorite(Mosque mosque) {
        // TODO: Save to Firestore favorites
        Toast.makeText(this, mosque.getName() + " added to favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        updateMapLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Mosque data class
    public static class Mosque {
        private String name, address;
        private double latitude, longitude, distanceKm;
        private int walkingMinutes;

        public Mosque(String name, String address, double latitude, double longitude, double distanceKm, int walkingMinutes) {
            this.name = name;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distanceKm = distanceKm;
            this.walkingMinutes = walkingMinutes;
        }

        // Getters
        public String getName() { return name; }
        public String getAddress() { return address; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public double getDistanceKm() { return distanceKm; }
        public int getWalkingMinutes() { return walkingMinutes; }
    }
}
