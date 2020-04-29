import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        startGame(in);
    }

    public static void startGame(Scanner in){

        int timeout = getTimeout(in);
        char next = getWhoGoesFirst(in);
       
    }

    public static char getWhoGoesFirst(Scanner in){

        char next = ' ';

        while(true){

            System.out.print("Who goes first, C for computer, O for opponent: ");

            next = in.next().toUpperCase().charAt(0);
            in.nextLine();

            if(next == 'C' || next == 'O')
                break;

            System.out.println("Invalid input. Try again.");

        }

        if(next == 'C')
            System.out.println("The computer will start the game.");
        else
            System.out.println("The opponent will start the game.");

        return next;
    }

    public static int getTimeout(Scanner in){

        int timeout = -1;

        while(true){
            System.out.print("What is the computer timeout to make a move? Enter in whole number seconds: ");
            try{
                timeout = Integer.parseInt(in.next());
                in.nextLine();
                if(timeout > 0)
                    break;

            }catch(Exception e){
                
            }

            System.out.println("Invalid input. Please try again.");

        }

        return timeout;
    }



    public static boolean validateMove(String input){

        if(input.length() != 2)
            return false;

        int row = (int)(input.charAt(0));
        int col = (int)(input.charAt(1));

        System.out.println(row + " " + col);

        if(row < 65 || row > 73)
            return false;

        if(col < 1 || col > 8)
            return false;

        return true;

    }

}