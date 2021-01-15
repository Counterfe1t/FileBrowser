import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileBrowserServer {

    public static void main(String args[]) {
        // Start running multithreaded FileBrowser server
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(1337));
            ExecutorService executor = Executors.newFixedThreadPool(5);
            int clientID;

            System.out.println("Server is running and waiting for clients...");

            // Create separate thread to handle each client connection
            while(true) {
                SocketChannel socket = server.accept();
                clientID = socket.socket().getPort();
                ClientHandler client = new ClientHandler(socket, clientID);
                executor.execute(client);
            }
        } catch (IOException e) {
            // Exception handler
            e.printStackTrace();
        }
    }

}