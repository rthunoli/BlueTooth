package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private ListView devicesListView;
    private static final int ENABLE_BT_REQUEST_CODE = 1;
    //private static final String TAG = "MainActivity";
    //public static final UUID uuid = UUID.fromString("19efeb7c-623d-4442-9325-15115cb0b362");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //Display a toast notifying the user that their device doesn’t support Bluetooth//
            Toast.makeText(getApplicationContext(), "This device doesn’t support Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            //If BluetoothAdapter doesn’t return null, then the device does support Bluetooth//
            Toast.makeText(getApplicationContext(), "This device does support Bluetooth", Toast.LENGTH_LONG).show();
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
            }

            //If the request was unsuccessful...//
            if (resultCode == RESULT_CANCELED) {

                //...then display this alternative toast.//
                Toast.makeText(getApplicationContext(), "An error occurred while attempting to enable Bluetooth",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void turnBTon(View view) {

        if (!bluetoothAdapter.isEnabled()) {
            //Create an intent with the ACTION_REQUEST_ENABLE action, which we’ll use to display our system Activity//
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            //Pass this intent to startActivityForResult(). ENABLE_BT_REQUEST_CODE is a locally defined integer that must be greater than 0,
            //for example private static final int ENABLE_BT_REQUEST_CODE = 1//
            startActivityForResult(enableIntent, ENABLE_BT_REQUEST_CODE);
            Toast.makeText(getApplicationContext(), "Enabling Bluetooth!", Toast.LENGTH_LONG).show();
        }
    }


    public void client(View view) {
        Intent intent = new Intent(this,Client.class);
        startActivity(intent);
        //new asyncTask().execute("a",null,"b");
    }

    public void server(View view) {
        Intent intent = new Intent(this,Server.class);
        startActivity(intent);
    }
}
