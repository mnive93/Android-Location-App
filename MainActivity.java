package com.example.app;



import java.util.List;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class MainActivity extends MapActivity {
    public static final String TAG = "MapActivity";
    private MapView mapView;
    private LocationManager locationManager;
    Geocoder geocoder;
    Location location;
    LocationListener locationListener;
    CountDownTimer locationtimer;
    MapController mapController;
    MapOverlay mapOverlay = new MapOverlay();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
       // initComponents();
        mapView = (MapView) findViewById(R.id.map_container);
        
        mapView.setBuiltInZoomControls(true);
       // mapView.setSatellite(true);
     //   mapView.setTraffic(true);
       mapView.setStreetView(true);
        mapController = mapView.getController();
        mapController.setZoom(16);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(MainActivity.this,
                    "Location Manager Not Available", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null)
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Toast.makeText(MainActivity.this,
                    "Location Are" + lat + ":" + lng, Toast.LENGTH_SHORT)
                    .show();
            GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
            mapController.animateTo(point, new Message());
            mapOverlay.setPointToDraw(point);
            List<Overlay> listOfOverlays = mapView.getOverlays();
            listOfOverlays.clear();
            listOfOverlays.add(mapOverlay);
        }
        locationListener = new LocationListener() {
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            }

            public void onProviderEnabled(String arg0) {
            }

            public void onProviderDisabled(String arg0) {
            }

            public void onLocationChanged(Location l) {
                location = l;
                locationManager.removeUpdates(this);
                if (l.getLatitude() == 0 || l.getLongitude() == 0) {
                } else {
                    double lat = l.getLatitude();
                    double lng = l.getLongitude();
                    Toast.makeText(MainActivity.this,
                            "Location Are" + lat + ":" + lng,
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 10f, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener);
        locationtimer = new CountDownTimer(30000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (location != null)
                    locationtimer.cancel();
            }

            @Override
            public void onFinish() {
                if (location == null) {
                }
            }
        };
        locationtimer.start();
    }

    public MapView getMapView() {
        return this.mapView;
    }
    protected boolean isRouteDisplayed() {
        return false;
    }

    class MapOverlay extends Overlay {
        private GeoPoint pointToDraw;

        public void setPointToDraw(GeoPoint point) {
            pointToDraw = point;
        }

        public GeoPoint getPointToDraw() {
            return pointToDraw;
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
                long when) {
            super.draw(canvas, mapView, shadow);

            Point screenPts = new Point();
            mapView.getProjection().toPixels(pointToDraw, screenPts);

            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    R.drawable.mark);
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 24, null);
            return true;
        }
    
    
    }
}
