import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;

public class Game {

    private Scanner in;

    private IsolationBoard board;
    private Agent agent;

    private Player computer;
    private Player opponent;
    private Player next;

    private int time;

    private LinkedList<Move> moves;

    public Game(){
        this.in = new Scanner(System.in);
        this.board = new IsolationBoard();
        this.next = Player.X;
        this.moves = new LinkedList<Move>();
    }

    public void start(){

        this.time = getTimeout();
        this.computer = getComputerPlayer();
        this.opponent = Player.opponent(this.computer);

        this.agent = new Agent(this.time, computer);

        this.gameLoop();

    }

    private void gameLoop(){
        while(true){

            if(board.isTerminal(next)){
                this.printBoard();
                if(next == computer){
                    System.out.println("Player wins!");
                    return;
                }else{
                    System.out.println("Computer wins!");
                    return;
                }
            }

            Move move = null;

            if(computer == this.next){
                System.out.println("Computer is thinking...");
                move = agent.search(board);
                board.move(computer, move);
                this.moves.add(move);

                this.printBoard();
                System.out.println("Computer's move is: " + move);
            }else{
                move = getMove();
                board.move(opponent, move);
                this.moves.add(move);
                this.printBoard();
            }

            this.next = Player.opponent(this.next);
        }
    }

    private Player getComputerPlayer(){

        char next = ' ';

        while(true){

            System.out.print("Who goes first, C for computer, O for opponent: ");

            next = in.next().toUpperCase().charAt(0);
            in.nextLine();

            if(next == 'C'){
                System.out.println("The computer will start the game.");
                this.printBoard();
                return Player.X;
            }else if(next == 'O'){
                System.out.println("The opponent will start the game.");
                this.printBoard();
                return Player.O;
            }

            System.out.println("Invalid input. Try again.");

        }
    }

    private void printBoard(){
        String[] boardString = this.board.getPrintableBoard();
        Iterator<Move> movesIterator = this.moves.iterator();

        System.out.print(boardString[0] + "\t");
        System.out.println(this.computer == Player.X? "Computer vs. Opponent": "Opponent vs. Computer");

        for(int counter = 1; counter < boardString.length || movesIterator.hasNext(); counter++){
            if(counter < boardString.length){
                System.out.print(boardString[counter]);
            }else{
                System.out.print("\t\t");
            }

            if(movesIterator.hasNext()){
                System.out.print("\t   " + (counter) + ". " + movesIterator.next());

                if(movesIterator.hasNext()){
                    System.out.print("   " + movesIterator.next());
                }
            }

            System.out.println();
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
                    return timeout;

            }catch(Exception e){
                in.nextLine();
            }

            System.out.println("Invalid input. Please try again.");

        }

    }

    private Move getMove(){
        while(true){

            System.out.print("Enter opponent's move: ");

            String input = in.next().toUpperCase();
            in.nextLine();

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