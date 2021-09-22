
package TicTacToeServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(58901)) {
            System.out.println("Tic Tac Toe Server is Running...");
            var pool = Executors.newFixedThreadPool(20);
            // while (true) {
            Game game = new Game();
            pool.execute(game.new Player(listener.accept(), 'X'));
            pool.execute(game.new Player(listener.accept(), 'O'));
            // }
        }
    }
}

class Game {
    Player currentPlayer;

    public synchronized void move(Player player) {
        if (player != currentPlayer) {
            System.out.println("Not your turn");
        } else if (player.opponent == null) {
            System.out.println("You don't have an opponent yet");
        } else {
            currentPlayer = currentPlayer.opponent;
        }

    }

    class Player implements Runnable {
        char mark;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, char mark) {

            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {

            try {
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("\t" + mark + " joined ! ");
            if (mark == 'X') {
                currentPlayer = this;
                System.out.println(this + " : " + currentPlayer);
                output.println("MESSAGE Waiting for opponent to connect");
            } else {
                if (currentPlayer == null) {
                    System.out.println(this + " : " + currentPlayer);

                    System.out.println("Current is null sorry");
                } else {

                    opponent = currentPlayer;
                    opponent.opponent = this;
                    opponent.output.println("MESSAGE Your move");
                }
            }
            while (input.hasNextLine()) {
                String temp = input.nextLine();
                if (opponent != null) {
                    opponent.output.println(mark + " : " + temp);
                }
                System.out.println(mark + " : " + temp);
                move(this);
            }
        }

    }
}