import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class manualGuessingGameShowFOURCLIENT {
    public static void main(String[] Args) {
        try {
            int guessCount = 1, guessNum, low=0 , high;
            Socket connection = new Socket("localhost", 8000);
            DataOutputStream toServer = new DataOutputStream(connection.getOutputStream()); //Connection setup to server
            DataInputStream fromServer = new DataInputStream(connection.getInputStream()); //Connection setup to recieve from server
            Scanner console = new Scanner(System.in);

            System.out.print("What is the max range to guess from? (0-???): ");
            high = console.nextInt();
            toServer.writeInt(high);

            System.out.println("Guess a number from ("+low+"-"+high+")");
            System.out.print("Guess " + guessCount + ": ");
            guessNum=console.nextInt();
            while(guessNum<low||guessNum>high){
                System.out.println("Outside of range! Try Again!");
                System.out.println("Guess a number from ("+low+"-"+high+")");
                System.out.print("Guess " + guessCount + ": ");
                guessNum=console.nextInt();
            }
            guessCount++;
            toServer.writeInt(guessNum);
            toServer.flush();
            String result=fromServer.readUTF();
            System.out.println(result+"\n");

            while(result.charAt(result.length()-2)!='!'){
                if(result.charAt(result.length()-2)=='W')
                    low = guessNum;
                else
                    high=guessNum;
                System.out.println("Guess a number from ("+low+"-"+high+")");
                System.out.print("Guess " + guessCount + ": ");
                guessCount++;
                guessNum=console.nextInt();
                while(guessNum<low||guessNum>high){
                    System.out.println("Outside of range! Try Again!");
                    System.out.println("Guess a number from ("+low+"-"+high+")");
                    System.out.print("Guess " + guessCount + ": ");
                    guessNum=console.nextInt();
                }
                toServer.writeInt(guessNum);
                result=fromServer.readUTF();
                System.out.println(result+"\n");
            }

            System.out.println("Guesses Taken: "+guessCount);
        } catch (IOException e) {
        }
        ;
    }
}
