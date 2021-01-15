import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ClientHandler implements Runnable {

    private SocketChannel socket;
    private int clientID;
    private DataInputStream din;
    private DataOutputStream dout;

    public ClientHandler(SocketChannel socket, int clientID) {
        this.socket = socket;
        this.clientID = clientID;
    }

    // Handle client connection
    @Override
    public void run() {
        try {
            try {
                // Path to server storage directory
                final String HOME_DIRECTORY = "C:/Users/Karol/Desktop/WMiI INF/Semestr IV/JAVA/FileBrowser/ServerStorage";

                // Get client input/output stream for data input/output
                din = new DataInputStream(socket.socket().getInputStream());
                dout = new DataOutputStream(socket.socket().getOutputStream());

                System.out.println("Client " + clientID + ": Connected");

                // Sending list of subdirectories
                File file = new File(HOME_DIRECTORY);
                sendDirectory(file);
                sendFileSizeList(file);

                while(true) {
                    String content = din.readUTF();
                    System.out.println("Client " + clientID + ": " + content);

                    if(content.equals("exit")) {
                        System.out.println("Client " + clientID + ": Disconnected");
                        break;
                    }
                    else if(content.contains("size")) {
                        File f = new File(content.substring(5));
                        sendFileSize(f);
                    }
                    else {
                        file = new File(content);

                        if(file.exists()) {
                            if(file.isDirectory()) {
                                if(content.equals("C:\\Users\\Karol\\Desktop\\WMiI INF\\Semestr IV\\JAVA\\FileBrowser")) {
                                    sendDirectory(new File(HOME_DIRECTORY));
                                    sendFileSizeList(new File(HOME_DIRECTORY));
                                }
                                else {
                                    sendDirectory(file);
                                    sendFileSizeList(file);
                                }
                            }
                            else if(file.isFile()) {
                                System.out.println("Client " + clientID + ": Downloading " + file.getName());
                                sendFileSize(file);
                                sendFile(file);
                            }
                        } else {
                            sendDirectory(file.getParentFile());
                        }
                    }
                }
            } finally {
                // Close streams and sockets
                din.close();
                dout.close();
                socket.close();
            }
        } catch (IOException e) {
            // Exception handler
            e.printStackTrace();
        }
    }

    // Send file object to client
    private void sendDirectory(File file) {
        try {
            // Create object output stream
            ObjectOutputStream out = new ObjectOutputStream(dout);

            // Send home directory file object
            out.writeObject(file);
            out.flush();

        } catch(IOException e) {
            // Exception handler
            e.printStackTrace();
        }
    }

    // Send a file to client
    private void sendFile(File file) {
        try {
            FileChannel fileChannel = FileChannel.open(file.toPath());
            ByteBuffer buffer = ByteBuffer.allocate(4096);

            while((fileChannel.read(buffer)) > 0) {
                buffer.flip();
                socket.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            // Exception handler
            e.printStackTrace();
        }
    }

    // Send file size
    private void sendFileSize(File file) {
        try {
            dout.writeInt((int)file.length());
            dout.flush();
        } catch (IOException e) {
            // Exception handler
            e.printStackTrace();
        }

    }

    // London changes
    private int[] getFileSizeList(File file) {
        File[] files = file.listFiles();
        int[] fileSizes = new int[files.length];

        for(int i = 0; i < files.length; i++) {
            if(files[i].isFile()) {
                fileSizes[i] = (int)files[i].length();
            }
            else {
                fileSizes[i] = 0;
            }
        }

        return fileSizes;
    }

    private void sendFileSizeList(File file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(dout);

        int[] fileSizes = getFileSizeList(file);

        out.writeObject(fileSizes);
        out.flush();
    }
}
