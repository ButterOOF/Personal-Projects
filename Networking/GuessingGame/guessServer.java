import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class guessServer {
    Lock lock = new ReentrantLock();

    public static void main(String[] Args) {
        new guessServer();
    }




    public guessServer() {
        try {
            int clientAmount=1;
            ServerSocket server = new ServerSocket(8000);
            while (true) {
                Socket socket = server.accept(); //Waits for connection from a cilent
                System.out.println("Client "+clientAmount+" connected");
                new Thread(new guessThread(socket)).start(); 
            }
        } catch (IOException ex) {
        }
    }

    public class guessThread implements Runnable {
        private Socket threadSocket;
        private int winningNumber;
        public guessThread(Socket s){
            threadSocket=s;
        }

        public void run() {
            try {
                boolean won=false;

                DataInputStream fromClient = new DataInputStream(threadSocket.getInputStream()); //Range from cilent
                winningNumber=(int)(Math.random()*((fromClient.readInt()+1)));
                while(!won) {
                    DataOutputStream toClient = new DataOutputStream(threadSocket.getOutputStream()); //Sets up a connection fomr server to the silent
                    int guess = fromClient.readInt(); //Guess given from cilent
                    String result="";
                    if(guess<winningNumber){
                        result+= "TOO LOW!";
                    }else if(guess>winningNumber){
                        result+= "TOO HIGH!";
                    }else {
                        won=true;
                        result+= "YOU WIN! ";
                    }
                    toClient.writeUTF(result); //sends back the result of high or low
                    toClient.flush(); //Cut connection
                }
                
            } catch (IOException e) {
            }
        }
    }


}// guessServer
