package com.hyk.app.socketdemo.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpClient {

    private String mServerIp = "127.0.0.1";//192.168.0.126
    private InetAddress mServerAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;
    private Scanner mScanner;

    UdpClient() {
        try {
            mSocket = new DatagramSocket();
            mServerAddress = InetAddress.getByName(mServerIp);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");

    }

    public void start() {
        while (true) {
            String clientMsg = mScanner.next();
            byte[] clientMsgBytes = clientMsg.getBytes();
            DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                    clientMsgBytes.length, mServerAddress, mServerPort);
            try {
                mSocket.send(clientPacket);

                byte[] buf = new byte[1024];
                DatagramPacket serverMsgPacket = new DatagramPacket(buf, buf.length);
                mSocket.receive(serverMsgPacket);

                String serverMsg = new String(serverMsgPacket.getData(), 0, serverMsgPacket.getLength());

                System.out.println("msg = " + serverMsg);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        new UdpClient().start();
    }


}
