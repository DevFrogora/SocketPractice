
package TicTacToeClient;

import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        TicTacToeClient client = new TicTacToeClient(args[0]);
        new Thread(client).start();

    }
}

class TicTacToeClient implements Runnable {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Scanner in_keyboard;

    public TicTacToeClient(String serverAddress) throws Exception {

        socket = new Socket(serverAddress, 58909);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        in_keyboard = new Scanner(System.in);
    }

    @Override
    public void run() {
        System.out.println("I am in run " + in.hasNextLine());
        while (in.hasNextLine() || in_keyboard.hasNextLine()) {

            // || in_keyboard.hasNextLine() ? in : in_keyboard
            Scanner temp = in.hasNextLine() ? in : in_keyboard;
            String input = temp.nextLine();
            System.out.println("I am in while loop" + input);
            out.println(input);
            System.out.println("> waiting on while loop");

        }

    }

}
