package thacher.robotics.dkelly.trailsconcept;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MapSetupTask extends AsyncTask<GoogleMap, Void, List<MapSetupTask.LatLngCont>> {

    JedisPoolConfig con;
    JedisPool pool;
    GoogleMap map;

    protected class LatLngCont {

        private final String title;
        private final LatLng latlong;

        LatLngCont(Double lat, Double lon, String ti) {
            this.title = ti;
            this.latlong = new LatLng(lat, lon);
        }

        protected LatLng getLatlong() {
            return latlong;
        }

        protected String getTitle() {
            return title;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        con = new JedisPoolConfig();
        con.setJmxEnabled(false);
        pool = new JedisPool(con, "pub-redis-10130.us-east-1-4.2.ec2.garantiadata.com" , 10130, 1000, "golden3trout");
    }

    @Override
    protected List<LatLngCont> doInBackground(GoogleMap... params) {
        ArrayList<LatLngCont> temp = new ArrayList<>();
        Jedis jedis = pool.getResource();
        map = params[0];

        for (String value : jedis.lrange("trails.phelps", 0, -1)) {
            String[] tokens = value.substring(1, value.length() - 2).split(",");
            temp.add(new LatLngCont(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), tokens[0]));
        }

        jedis.close();
        return temp;
    }

    @Override
    protected void onPostExecute(List<LatLngCont> latLngs) {
        super.onPostExecute(latLngs);

        PolylineOptions line = new PolylineOptions();
        line.geodesic(true);
        line.color(Color.RED);

        for(LatLngCont i: latLngs)
            line.add(i.getLatlong());

        map.addPolyline(line);
        pool.destroy();
    }
}
