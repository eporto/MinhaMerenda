package com.tac.marcos.minhamerenda;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity  {
    ArrayList<Location> localizacoes;

    Double latitude;
    Double longitude;
    TextView text;
    int temLocalizacao =0;

    public ArrayList<Location> ordenar(ArrayList<Location> loc, Location l) {
        ArrayList<Location> local = new ArrayList<Location>();

        int i, j, k;
        Location[] array = new Location[loc.size()];
        for(i = 0; i<loc.size(); i++) {
            array[i] = loc.get(i);
            Log.i("mensagem", ""+loc.get(i));

        }


        for ( i = array.length; i >= 1; i--) {
            for ( j = 1; j < i; j++) {
                if (array[j - 1].distanceTo(l) > array[j].distanceTo(l)) {
                    Location aux = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = aux;
                }
            }
        }

        for(i =0; i< array.length; i++){

            local.add(array[i]);
        }







        return local;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ArrayList<Location> lo = new ArrayList<Location>();

            Double lati = location.getLatitude();
            Double lon = location.getLongitude();

            latitude = lati;
            longitude = lon;





            Location t1 = new Location("");//provider name is unnecessary
            t1.setLatitude(-30.0277);
            t1.setLongitude(-51.2287);

            Location t2 = new Location("");//provider name is unnecessary
            t2.setLatitude(-22.9035);
            t2.setLongitude(-43.2096);

            Location t3 = new Location("");//provider name is unnecessary
            t3.setLatitude(-3.71839);
            t3.setLongitude(-38.5434);


            lo.add(t1);
            lo.add(t2);
            lo.add(t3);
// adicionando no arraylist localizacoes, para demonstração, esse array deve receber
            //lista de latitudes e longitudes


// Array devolve latitudes e longitudes ordenadas
            ArrayList<Location> loOrdenadas = ordenar(lo, location);

            localizacoes = lo;
            temLocalizacao = 1;

            text.setText(""+    loOrdenadas.get(1).getLatitude());



        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }



    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_layout);
        ArrayList<Location> localizacoes = new ArrayList<Location>();


        text = (TextView)findViewById(R.id.textView);

        try {

            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,mLocationListener);




        } catch (SecurityException e) {
        }





    }






        /*Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=escola municipal");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

*/

    // Search for restaurants in San Francisco















    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
