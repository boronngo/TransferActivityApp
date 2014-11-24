package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by boro on 14/11/19.
 */
public class BluetoothClientThread extends Thread {

    private BluetoothSocket socket;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private Context context;

    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String myNumber;

    public BluetoothClientThread(Context context, String myNumber, BluetoothDevice device, BluetoothAdapter adapter) {

        this.context = context;
        this.device = device;
        this.adapter = adapter;

        BluetoothSocket tmpSocket = null;
        try {
            tmpSocket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        socket = tmpSocket;
    }

    public void run() {

        if (adapter.isDiscovering()) {
            adapter.cancelDiscovery();
        }

        try {
            socket.connect();
            InputStream is = socket.getInputStream();
            System.out.println("InputStream開きます。");
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }


    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
