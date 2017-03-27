package com.example.minhaufg.placesprototype;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

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
                                bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviour.setPeekHeight((int)convertDpToPixel(160, this));

        bottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.findViewById(R.id.expanded_picture_and_name).setVisibility(View.VISIBLE);
                    bottomSheet.findViewById(R.id.single_line_picture_and_name).setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.findViewById(R.id.expanded_picture_and_name).setVisibility(View.GONE);
                    bottomSheet.findViewById(R.id.single_line_picture_and_name).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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
