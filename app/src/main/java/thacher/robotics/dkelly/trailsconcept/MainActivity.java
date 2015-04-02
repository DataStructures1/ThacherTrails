package thacher.robotics.dkelly.trailsconcept;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(34.465418, -119.180875), 14)));
        new MapSetupTask().execute(map);
    }
}
