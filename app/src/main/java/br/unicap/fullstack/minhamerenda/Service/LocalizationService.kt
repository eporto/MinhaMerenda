package br.unicap.fullstack.minhamerenda.Service

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener


class LocalizationService(val context:Context) {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLastLocation: Location? = null ;
    private var locationRequest : LocationRequest ? = null;
    private var lastLocation : Location ? = null;
    private var currentLocation : Location? = null;

    companion object {
        val TAG: String = LocalizationService.javaClass.simpleName //MainActivity.class.getSimpleName();
        val REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE: Int = 33
        val REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE : Int = 34
        val REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE : Int = 35
        val MIN_UPDATE_INTERVAL: Long = 30 * 1000; // 1  minute is the minimum Android recommends, but we use 30 seconds
    }

    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);

        checkForLocationRequest();
        checkForLocationSettings();
        checkGoogleApiAvailability()
    }

/*
        this.btnLocation.setOnClickListener{_ ->
            this.callLastKnownLocation(null);
        }

        this.btnSql.setOnClickListener{_ ->
            this.callCurrentLocation(null);
        }
*/
   private fun checkGoogleApiAvailability() {
        var googleApiAvailability : GoogleApiAvailability  = GoogleApiAvailability.getInstance();
        var result : Int = googleApiAvailability.isGooglePlayServicesAvailable(this.context);

        if (result != ConnectionResult.SUCCESS && result != ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            //Toast.makeText(this, "Are you running in Emulator ? try a real device.", Toast.LENGTH_SHORT).show();
            Log.d("[DEBUG]: ","Are you running in Emulator ? try a real device.");
        }
    }


    fun callLastKnownLocation(callback:(location:Location?) -> Unit) {
        try {
            if(checkPermission()) {
                mFusedLocationClient?.getLastLocation()
                    ?.addOnCompleteListener(this.context as Activity, {  task ->
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            var result : String = "Last known Location Latitude is " +
                                    mLastLocation?.getLatitude() + "\n" +
                                    "Last known longitude Longitude is " + mLastLocation?.getLongitude();

                            Log.d("[DEBUG]",result);
                            callback(mLastLocation)
                        } else {
                            //showSnackbar("No Last known location found. Try current location..!");
                            Log.d("[DEBUG]:", "No last known location found. Try current location !")
                        }
                    });
            }
        } catch (ex: Exception ) {
            ex.printStackTrace();
        }
    }

    //@SuppressWarnings("MissingPermission")
    fun callCurrentLocation(callback:(location:Location?) -> Unit) {
        try {
            if(checkPermission()) {
                mFusedLocationClient?.requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        currentLocation = locationResult.getLastLocation() as Location;

                        var result: String = "Current Location Latitude is " +
                                currentLocation?.getLatitude() + "\n" +
                                "Current location Longitude is " + currentLocation?.getLongitude();

                        Log.d("[DEBUG]", result);
                        callback(currentLocation)
                    }
                }, Looper.myLooper());
            }
        } catch (ex:Exception) {
            ex.printStackTrace();
            Log.d("[DEBUG]: ",ex.message);
        }
    }

    //API 23+
    private fun checkPermission() : Boolean {
        if(android.os.Build.VERSION.SDK_INT >= 23) {
            if (
            this.context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    this.context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                //requestPermissions(REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE); //Precisa de API 23+
                Log.d("[DEBUG]", "ERRO NA VERIFICAÇÃO DA PERMISSÃO CURRENTLOCATION LASTLOCATION");
                return false
            }
        }
        return true
    }



/*
    @SuppressWarnings("MissingPermission")
    private fun getLastLocation(callback:() -> Unit) {
        mFusedLocationClient?.getLastLocation()
                ?.addOnCompleteListener(this.context as Activity, {  task ->
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastLocation = task.getResult();

                        var result : String = "Last known Location Latitude is " +
                                mLastLocation?.getLatitude() + "\n" +
                                "Last known longitude Longitude is " + mLastLocation?.getLongitude();

                        Log.d("[DEBUG]",result);
                        callback()
                    } else {
                        //showSnackbar("No Last known location found. Try current location..!");
                        Log.d("[DEBUG]:", "No last known location found. Try current location !")
                    }
                });
    }*/

    private fun startLocationPermissionRequest(requestCode:Int) {
        ActivityCompat.requestPermissions(this.context as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), requestCode);
    }

    private fun requestPermissions(requestCode:Int) {
        val shouldProvideRationale: Boolean = ActivityCompat.shouldShowRequestPermissionRationale(this.context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            /*showSnackbar("Permission is must to find the location", "Ok",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest(requestCode);
                        }
                    });*/

            startLocationPermissionRequest(requestCode);
        } else {
            startLocationPermissionRequest(requestCode);
        }
    }

    private fun checkForLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest?.setInterval(MIN_UPDATE_INTERVAL);
        locationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    //Check for location settings.
    //Checka o gps
    private fun checkForLocationSettings() {
        try {
            var builder:LocationSettingsRequest.Builder  = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!);
            builder.addLocationRequest(locationRequest!!);
            var settingsClient : SettingsClient = LocationServices.getSettingsClient(this.context);

            settingsClient.checkLocationSettings(builder.build())
                    .addOnSuccessListener(this.context as Activity, { locationSettingsResponse ->
                        //Setting is success...
                        //Toast.makeText(this, "Enabled the Location successfully. Now you can press the buttons..", Toast.LENGTH_SHORT).show();
                        Log.d("[DEBUG]: ", "Enabled the Location successfully. Now you can press the buttons..")
                    })

                    //Mostra um dialogview para ativar permissão
                    .addOnFailureListener(this.context as Activity, object:OnFailureListener {
                        override fun onFailure(e:Exception) {
                            var statusCode : Int = (e as ApiException).statusCode;
                            if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    var rae : ResolvableApiException = e as ResolvableApiException;
                                    rae.startResolutionForResult(context, REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE);
                                } catch (sie:IntentSender.SendIntentException ) {
                                    sie.printStackTrace();
                                }
                            }
                            else if(statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                                //Toast.makeText(this@SqlActivity, "Setting change is not available.Try in another device.", Toast.LENGTH_LONG).show();
                                Log.d("[DEBUG]: ", "Setting change is not available.Try in another device.");
                            }
                        }
                    });
        } catch (ex:Exception ) {
            ex.printStackTrace();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults: Array<Int>) {
        if (requestCode == REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                //getLastLocation();
            }
        }

        if (requestCode == REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //callCurrentLocation();
            }
        }
    }

}