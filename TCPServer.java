import java.net.*;
import java.io.*;

public class TCPServer {
    public static void main(String args[]) {
        String port = args[0];
        receiveData(Integer.valueOf(port));
    }

    public static void receiveData(int port) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            //int port = 49152;
            serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port " + port);
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client Accepted, Client address is:" + socket.getInetAddress().getHostAddress()
                        + ":" + socket.getPort());

                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osr = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osr);

                Thread t = new ClientHandler(socket, bis, bw);
                t.start();
            }
        } catch (Exception e) {

        }
    }
}

class ClientHandler extends Thread {
    final Socket socket;
    final BufferedInputStream bis;
    final BufferedWriter bw;

    public ClientHandler(Socket socket, BufferedInputStream bis, BufferedWriter bw) {
        this.socket = socket;
        this.bis = bis;
        this.bw = bw;
    }

    public void run() {
        try {
            int i = 0;
            while (true) {
                String data = "";
                int s = bis.read();
                if (s == -1) {
                    break;
                }
                data += "" + (char) s;
                int len = bis.available();
                // System.out.println("Len got : " + len);
                if (len > 0) {
                    byte[] byteData = new byte[len];
                    bis.read(byteData);
                    data += new String(byteData);
                }
                System.out.println("Message " + i++ + " received from the client : " + data);

                bw.write(data);
                bw.flush();
            }
        } catch (Exception e) {
        }
    }
}