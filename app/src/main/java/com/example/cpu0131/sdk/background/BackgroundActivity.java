package com.example.cpu0131.sdk.background;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cpu0131.sdk.R;
import com.example.cpu0131.sdk.SdkExample;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;



@SdkExample(description = R.string.example_background_description)
public class BackgroundActivity extends AppCompatActivity {

    IALocationManager mManager;
    LocationStore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        mManager = IALocationManager.create(this);
        mStore = LocationStore.obtain(this);
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(mReceiver,new IntentFilter("location-update"));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mManager.destroy();
        super.onDestroy();
    }

    PendingIntent getPendingIntent() {
        return PendingIntent.getService(this, 0, new Intent(this, LocationStoreService.class), 0);
    }

    public void onStart(View v) {
        mManager.requestLocationUpdates(IALocationRequest.create(), getPendingIntent());
    }

    public void onStop(View v) {
        mManager.removeLocationUpdates(getPendingIntent());
    }

    public void onShare(View v) {
        mStore.share();
    }

    public void onReset(View v) {
        mStore.reset();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("location")) {
                IALocation location = intent.getParcelableExtra("location");
                ((TextView) findViewById(R.id.text_location)).setText(
                        getString(R.string.text_background_received, location));
            }
        }
    };

}
