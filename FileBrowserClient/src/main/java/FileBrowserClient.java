import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileBrowserClient {

    public SocketChannel socket;
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int SERVER_PORT_NUMBER = 1337;
    private DataInputStream din;
    private DataOutputStream dout;

    public FileBrowserClient(String address, int port) {
        try {
            // Create new socket channel
            this.socket = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(address, port);
            this.socket.connect(socketAddress);

            din = new DataInputStream(socket.socket().getInputStream());
            dout = new DataOutputStream(socket.socket().getOutputStream());
        } catch (IOException e) {
            // Exception handler
            e.printStackTrace();
        }
    }

    // Send path to specific file
    public void sendMSG(String msg) {
        try {
            dout.writeUTF(msg);
            dout.flush();
        } catch (IOException e) {
            // Exception handler
            e.printStackTrace();
        }
    }

    // Receive list of subdirectories
    public File getDirectory() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.socket().getInputStream());
        return (File)in.readObject();
    }

    // Receive size of the given file
    public int getFileSize() throws IOException {
        return din.readInt();
    }

    // Receive file vie an array of bytes
    public void receiveFile(String fileName, Path path, int bytesToRead) throws IOException {
        //Path path = Paths.get(filePath + fileName);
        path = Paths.get(path + "\\" + fileName);
        System.out.println(path);
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        int bytesRead = 0;
        FileChannel fc = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)
        );

        while((bytesRead += socket.read(buffer)) > 0) {
            buffer.flip();
            fc.write(buffer);
            buffer.clear();
            if(bytesRead >= bytesToRead) {
                break;
            }
        }

        fc.close();
    }

    // London changes
    public int[] getFileSizeList() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.socket().getInputStream());
        return (int[])in.readObject();
    }
}
