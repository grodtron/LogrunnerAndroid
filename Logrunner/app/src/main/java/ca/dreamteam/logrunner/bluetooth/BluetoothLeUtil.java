package ca.dreamteam.logrunner.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 25/02/2015.
 */
public class BluetoothLeUtil {
    private BluetoothLeUtil(){
        throw new RuntimeException("It's a static class, why would you do this?");
    }

    public final static int REQUEST_ENABLE_BT = 0xB1007007;

    /**
     * Enable bluetooth and get the adapter object
     * @return A BluetoothAdapter instance
     */
    public static BluetoothAdapter getLeAdapter(Activity _this){
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothLeManager =
                (BluetoothManager) _this.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter bluetoothLeAdapter = bluetoothLeManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothLeAdapter == null || !bluetoothLeAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            _this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return null;
        }else {
            return bluetoothLeAdapter;
        }
    }

    public final static Object properties [][] = new Object [][] {
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_BROADCAST,         "is broadcastable."},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS,    "has extended properties"},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_INDICATE, 	      "supports indication"},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_NOTIFY, 	          "supports notification"},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_READ, 	          "is readable."},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, 	  "supports write with signature"},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_WRITE, 	          "can be written."},
        new Object[]{ BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "can be written without response."},
    };

    public static List<String> getReadableCharacteristicProperties(BluetoothGattCharacteristic c){

        List<String> properties = new ArrayList<String>();

        int cProps = c.getProperties();

        for(Object[] property : BluetoothLeUtil.properties){
            if( (cProps & ((Integer)property[0])) != 0 ){
                properties.add((String)property[1]);
            }
        }

        return properties;
    }

}