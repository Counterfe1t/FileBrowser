import java.io.IOException;

public class Main {

    private static String address = "192.168.100.203";
    private static int port = 1337;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        ConnectWindow connectWindow = new ConnectWindow();
        while(connectWindow.getAddress() == null && connectWindow.getPort() == -1) {
            System.out.println("Waiting");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        address = connectWindow.getAddress();
        port = connectWindow.getPort();

        System.out.println("Address: " + address);
        System.out.println("Port: " + port);

        if((address.equals("127.0.0.1") || address.equals("localhost")) && port == 1337) {
            System.out.println("Connected");
        } else {
            new DialogWindow(null, 1);
            System.out.println("Invalid input");
            System.exit(-1);
        }

        new MainWindow(new FileBrowserClient(address, port));

    }

}