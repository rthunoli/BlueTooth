package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.UUID;

public class Server extends AppCompatActivity {
    private static final String TAG = "Server";
    private static final UUID uuid = UUID.fromString("19efeb7c-623d-4442-9325-15115cb0b362");
    private static final String serviceName = "RajServer";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket bluetoothServerSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void start(View view) {
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(serviceName, uuid);
            Server.AsyncTask at = new Server.AsyncTask();
            at.execute(bluetoothServerSocket);
        }
        catch (java.io.IOException ioe)
        {
            Log.e(TAG,ioe.toString());
        }
    }


    private class AsyncTask extends android.os.AsyncTask<BluetoothServerSocket, Void, BluetoothSocket>
    {
        BluetoothSocket  bts = null;
        @Override
        protected BluetoothSocket doInBackground(BluetoothServerSocket... bluetoothServerSockets) {
            try {
                bts = bluetoothServerSockets[0].accept();
            }
            catch (java.io.IOException ioe)
            {
                Log.e(TAG,ioe.toString());
            }
            return bts;
        }


    }

    public void makeDiscoverable(View view) {

        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        //Specify how long the device will be discoverable for, in seconds.//
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 400);
        startActivity(discoveryIntent);
    }

}
