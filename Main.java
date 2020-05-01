import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        startGame(in);
    }

    public static void startGame(Scanner in){

        int timeout = getTimeout(in);
        Player computer = getComputerPlayer(in);

        IsolationBoard board = new IsolationBoard();

        Agent agent = new Agent(timeout, computer);

        Player next = Player.X;

        while(true){

            if(board.isTerminal(next))
                if(next == computer)
                    System.out.println("Player wins!");
                else
                    System.out.println("Computer wins!");

            Move move;

            if(computer == next){
                System.out.println(board);
                move = agent.search(board);
                board.move(computer, move);
            }else{
                move = getMove(in);
                board.move(Player.opponent(computer), move);
            }

            next = Player.opponent(next);
        }

    }

    public static Player getComputerPlayer(Scanner in){

        char next = ' ';

        while(true){

            System.out.print("Who goes first, C for computer, O for opponent: ");

            next = in.next().toUpperCase().charAt(0);
            in.nextLine();

            if(next == 'C' || next == 'O')
                break;

            System.out.println("Invalid input. Try again.");

        }

        if(next == 'C'){
            System.out.println("The computer will start the game.");
            return Player.X;
        }else{
            System.out.println("The opponent will start the game.");
            return Player.O;
        }
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

    public static Move getMove(Scanner in){
        while(true){

            System.out.print("Enter opponent's move: ");

            String input = in.next().toUpperCase();

            Move move = validateMove(input);
            if(move != null)
                return move;
            else
                System.out.println("Invalid input. Please try again.");
                
        }
    }

    public static Move validateMove(String input){

        if(input.length() != 2)
            return null;

        int row = (int)(input.charAt(0));
        int col = (int)(input.charAt(1));

        System.out.println(row + " " + col);

        if(row < 65 || row > 73)
            return null;

        if(col < 1 || col > 8)
            return null;

        return new Move(65 - row, col - 1);

    }

}