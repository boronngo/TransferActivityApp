package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by boro on 14/11/19.
 */
public class BluetoothClient {

    private BluetoothSocket socket;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;

    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothClient() {

        adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        device = devices.iterator().next();
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Bluetoothで接続して成功したらtrueを返す
     * @return 接続結果
     */

    public boolean connect() {
        try {
            socket.connect();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void send(double[] doubles) {
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            for(double d: doubles) {
                os.writeDouble(d);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
