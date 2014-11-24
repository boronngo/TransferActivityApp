package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by boro on 14/11/18.
 */
public class BluetoothServerThread extends Thread {

    private BluetoothServerSocket serverSocket;
    private BluetoothAdapter adapter;

    private Context context;
    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String myNumber;

    public BluetoothServerThread(Context context, String myNumber, BluetoothAdapter adapter) {

        this.context = context;
        this.adapter = adapter;
        this.myNumber = myNumber;

        BluetoothServerSocket tmpSocket = null;

        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord("BluetoothSample03", uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {

        BluetoothSocket receivedSocket = null;

        while (true) {

            try {
                receivedSocket = serverSocket.accept();
            } catch (IOException e) {
                break;
            }

            if (receivedSocket != null) {
                try {
                    DataInputStream is = new DataInputStream(receivedSocket.getInputStream());
                    System.out.println(is.readDouble());
                    System.out.println(is.readDouble());
                    System.out.println(is.readDouble());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
