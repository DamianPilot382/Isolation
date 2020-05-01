import java.util.Scanner;

public class Game {

    private Scanner in;

    private IsolationBoard board;
    private Agent agent;

    private Player computer;
    private Player opponent;
    private Player next;

    private int time;

    public Game(){
        this.in = new Scanner(System.in);
        this.board = new IsolationBoard();
        this.next = Player.X;
    }

    public void start(){

        this.time = getTimeout();
        this.computer = getComputerPlayer();
        this.opponent = Player.opponent(this.computer);

        this.agent = new Agent(this.time, computer);

        Player next = Player.X;

        while(true){

            if(board.isTerminal(next))
                if(next == computer)
                    System.out.println("Player wins!");
                else
                    System.out.println("Computer wins!");

            if(computer == next){
                System.out.println(board);
                Move move = agent.search(board);
                board.move(computer, move);
            }else{
                board.move(opponent, getMove());
            }

            next = Player.opponent(next);
        }

    }

    private Player getComputerPlayer(){

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

    private int getTimeout(){

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

    private Move getMove(){
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

    private Move validateMove(String input){

        if(input.length() != 2)
            return null;

        int row = (int)(input.charAt(0)) - 65;
        int col = (int)(input.charAt(1)) - 49;

        if(row < 0 || row > 7)
            return null;

        if(col < 0 || col > 7)
            return null;

        Move move = new Move(row, col);

        if(this.board.isMoveValid(move, this.next))
            return move;

        return null;

    }

}