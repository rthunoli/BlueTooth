package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;
import java.util.UUID;

public class Client extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private ListView devicesListView;
    private static final String TAG = "Client";
    private static final UUID uuid = UUID.fromString("19efeb7c-623d-4442-9325-15115cb0b362");

    private IntentFilter filter;
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothSocket socket;

    //Create a BroadcastReceiver for ACTION_FOUND//
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //Whenever a remote Bluetooth device is found...//
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                //….retrieve the BluetoothDevice object and its EXTRA_DEVICE field, which contains information about the device’s characteristics and capabilities//
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //You’ll usually want to display information about any devices you discover, so here I’m adding each device’s name and address to an ArrayAdapter,
                //which I’d eventually incorporate into a ListView//
                if(device.getName() != null) {
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };

    private class AsyncTask extends android.os.AsyncTask<BluetoothDevice, Void, BluetoothSocket>
    {
        BluetoothSocket socket = null;
        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            try {
                socket = bluetoothDevices[0].createRfcommSocketToServiceRecord(uuid);
                socket.connect();
            }
            catch (java.io.IOException ioe)
            {
                Log.e(TAG,ioe.toString());
            }
            return socket;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        devicesListView = findViewById(R.id.devices_list_view);

        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String device =(String) adapterView.getItemAtPosition(i);
                AsyncTask at = new AsyncTask();
                BluetoothDevice bt = bluetoothAdapter.getRemoteDevice(device.split("\n")[1]);;
                //socket = at.execute(bt);
                //Toast.makeText(Client.this, Client.this.selectedBTDevice, Toast.LENGTH_LONG).show();
            }
        });

        showPairedDevices();
    }

    @Override
    protected void onDestroy() {
        try {
            this.unregisterReceiver(broadcastReceiver);
        }
        catch (Exception e){}

        super.onDestroy();
    }

    private void showPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // If there’s 1 or more paired devices...//
        if (pairedDevices.size() > 0) {
            //...then loop through these devices//
            for (BluetoothDevice device : pairedDevices) {
                //Retrieve each device’s public identifier and MAC address. Add each device’s name and address to an ArrayAdapter, ready to incorporate into a
                //ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            devicesListView.setAdapter(mArrayAdapter);
        }
    }


    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    public void discover(View view) {
        if (bluetoothAdapter.isDiscovering()) {
            // Bluetooth is already in modo discovery mode, we cancel to restart it again
            bluetoothAdapter.cancelDiscovery();
        }

        //Checking BT permissions
        checkBTPermissions();

        //Register for the ACTION_FOUND broadcast//
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);
        mArrayAdapter.clear();

        if (bluetoothAdapter.startDiscovery()) {
            //If discovery has started, then display the following toast....//
            Toast.makeText(getApplicationContext(), "Discovering other bluetooth devices...",
                    Toast.LENGTH_SHORT).show();
        } else {
            //If discovery hasn’t started, then display this alternative toast//
            Toast.makeText(getApplicationContext(), "Something went wrong! Discovery has failed to start.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
