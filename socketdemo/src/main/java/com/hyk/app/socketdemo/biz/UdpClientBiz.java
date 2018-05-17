package com.hyk.app.socketdemo.biz;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpClientBiz {

    private String mServerIp = "10.0.2.2";//192.168.0.126
    private InetAddress mServerAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;

    public UdpClientBiz() {
        try {
            mSocket = new DatagramSocket();
            mServerAddress = InetAddress.getByName(mServerIp);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

   public interface OnMsgReturnedListener {
        void onMsgReturned(String msg);

        void onError(Exception ex);
    }

    public void sendMsg(final String msg, final OnMsgReturnedListener listener) {

        new Thread() {
            @Override
            public void run() {
                byte[] clientMsgBytes = msg.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                        clientMsgBytes.length, mServerAddress, mServerPort);
                try {
                    mSocket.send(clientPacket);

                    byte[] buf = new byte[1024];
                    DatagramPacket serverMsgPacket = new DatagramPacket(buf, buf.length);
                    mSocket.receive(serverMsgPacket);

                    String serverMsg = new String(serverMsgPacket.getData(), 0, serverMsgPacket.getLength());
                    listener.onMsgReturned(serverMsg);

                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }.start();


    }

    public void onDestroy() {
        if (mSocket != null) {
            mSocket.close();
        }
    }
}
