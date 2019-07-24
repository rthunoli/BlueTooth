package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    private static final int ENABLE_BT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //Display a toast notifying the user that their device doesn’t support Bluetooth//
            Toast.makeText(getApplicationContext(),"This device doesn’t support Bluetooth",Toast.LENGTH_LONG).show();
        }
        else {
            //If BluetoothAdapter doesn’t return null, then the device does support Bluetooth//
            Toast.makeText(getApplicationContext(), "This device does support Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    private void showPairedDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // Construct the data source
        ArrayList<String> devices = new ArrayList<>();
        // Create the adapter to convert the array to views
        //UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);


        ArrayAdapter<String>  mArrayAdapter = new ArrayAdapter<>(this, R.layout.activity_main);
        // If there’s 1 or more paired devices...//
        if (pairedDevices.size() > 0) {

            //...then loop through these devices//
            for (BluetoothDevice device : pairedDevices) {
                //Retrieve each device’s public identifier and MAC address. Add each device’s name and address to an ArrayAdapter, ready to incorporate into a
                //ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Check what request we’re responding to//
        if (requestCode == ENABLE_BT_REQUEST_CODE) {

            //If the request was successful…//
            if (resultCode == Activity.RESULT_OK) {

                //...then display the following toast.//
                Toast.makeText(getApplicationContext(), "Bluetooth has been enabled",
                        Toast.LENGTH_SHORT).show();

                showPairedDevices();
            }

            //If the request was unsuccessful...//
            if(resultCode == RESULT_CANCELED){

                //...then display this alternative toast.//
                Toast.makeText(getApplicationContext(), "An error occurred while attempting to enable Bluetooth",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void turnBTon(View view) {

        if (!bluetoothAdapter.isEnabled()) {
            //Create an intent with the ACTION_REQUEST_ENABLE action, which we’ll use to display our system Activity//
            android.content.Intent enableIntent = new android.content.Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            //Pass this intent to startActivityForResult(). ENABLE_BT_REQUEST_CODE is a locally defined integer that must be greater than 0,
            //for example private static final int ENABLE_BT_REQUEST_CODE = 1//
            startActivityForResult(enableIntent, ENABLE_BT_REQUEST_CODE);
            Toast.makeText(getApplicationContext(), "Enabling Bluetooth!", Toast.LENGTH_LONG).show();
        }
    }
}
