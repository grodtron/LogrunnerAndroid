package ca.dreamteam.logrunner.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import ca.dreamteam.logrunner.R;

/**
 * This activity should Scan for Bluetooth LE devices, and allow the user to make a connection to
 * one.
 *
 * Once the connection is made, it should start LeService and pass it the connection object
 */
public class BluetoothLeScanActivity extends Activity {

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private ListView mLeDeviceList;
    private Button mLeScanButton;
    private ProgressBar mLeProgressBar;

    private ArrayAdapter<BluetoothDevice> mLeDeviceListAdapter;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
        new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLeDeviceListAdapter.add(device);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);

        mBluetoothAdapter = BluetoothLeUtil.getLeAdapter(this);

        mLeDeviceList = (ListView) findViewById(R.id.deviceListView);
        mLeDeviceListAdapter = new ArrayAdapter<BluetoothDevice>(this, R.layout.device_list_text_view);
        mLeDeviceList.setAdapter(mLeDeviceListAdapter);

        mLeProgressBar = (ProgressBar) findViewById(R.id.scanProgressBar);

        mLeScanButton = (Button) findViewById(R.id.startScanButton);
        mLeScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothLeScanActivity.this.scanLeDevice();
            }
        });
    }

    private void scanLeDevice() {
        mLeScanButton.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(SCAN_PERIOD, 200) {
            @Override
            public void onTick(long l) {
                mLeProgressBar.setProgress( 100 - (int) ((100*l) / SCAN_PERIOD) );
            }

            @Override
            public void onFinish() {
                mLeProgressBar.setProgress( 0 );
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mLeScanButton.setEnabled(true);
            }
        };

        mBluetoothAdapter.startLeScan(mLeScanCallback);
        timer.start();
    }


}
