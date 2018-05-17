package com.hyk.app.socketdemo.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpService {

    private InetAddress mInetAddress;

    //端口号
    private int mPort = 7777;

    private DatagramSocket mSocket;


    private Scanner mScanner;

    private UdpService() {
        try {
            mInetAddress = InetAddress.getLocalHost();
            mSocket = new DatagramSocket(mPort, mInetAddress);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");
    }


    public void start() {
        while (true) {
            byte[] buf = new byte[1024];
            DatagramPacket received = new DatagramPacket(buf, buf.length);
            try {
                mSocket.receive(received);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = received.getAddress();

            int port = received.getPort();

            String clientMsg = new String(received.getData(), 0, received.getLength());

            System.out.println("address =" + address + ",port = " + port + ": msg = " + clientMsg);

            String returnMsg = mScanner.next();

            byte[] returnMsgBytes = returnMsg.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(returnMsgBytes, returnMsgBytes.length,
                    received.getSocketAddress());

            try {
                mSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) {
        new UdpService().start();
    }


}
