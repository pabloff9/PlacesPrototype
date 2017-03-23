package com.example.minhaufg.placesprototype;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class PlacesActivity extends AppCompatActivity {

    private MapView mapView;
    private BottomSheetBehavior<NestedScrollView> bottomSheetBehaviour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        MapboxAccountManager.start(this, getString(R.string.access_token));

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                MarkerOptions markerOptions = new MarkerOptions().position(
                        new LatLng(-16.602027, -49.261928));


                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                        Log.d("MAPBOX", "Marker clicado");

                        CameraPosition position = new CameraPosition.Builder()
                                .target(marker.getPosition())
                                .zoom(17)
                                .bearing(180)
                                .build();

                        marker.hideInfoWindow();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500, new MapboxMap.CancelableCallback() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onFinish() {
                                bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        });


                        return false;
                    }
                });


               mapboxMap.addMarker(markerOptions);

            }
        });

        NestedScrollView bottomView = ((NestedScrollView) findViewById(R.id.bottom_sheet));
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomView);
        bottomSheetBehaviour.setHideable(true);
        bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehaviour.setPeekHeight(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
