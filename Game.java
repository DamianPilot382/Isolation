import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Damian Ugalde
 * @date 2020-05-03
 * @version 1.0
 *
 * Project 4 - Isolation
 * CS 4200 - Artificial Intelligence
 * California State Polytechnic University, Pomona
 * Computer Science Department
 *
 * Instructor: Dominick A. Atanasio
 *
 */
public class Game {

    private Scanner in;

    private IsolationBoard board;
    private Agent agent;

    //For keeping track on which player is which and what player is next.
    private Player computer;
    private Player opponent;
    private Player next;

    //Time alloted for the computer to make a move.
    private int time;

    //Keep track of the moves done
    private LinkedList<Move> moves;

    /**
     * Default constructor. Initializes the necessary values.
     * This will NOT start the game.
     */
    public Game(){
        this.in = new Scanner(System.in);
        this.board = new IsolationBoard();
        this.next = Player.X;
        this.moves = new LinkedList<Move>();
    }

    /**
     * Asks the player for the computer timeout and which player will start the game.
     * Afterwards, it will start the main game loop.
     */
    public void start(){

        this.time = getTimeout();
        this.computer = getComputerPlayer();
        this.opponent = Player.opponent(this.computer);

        this.agent = new Agent(this.time, computer);

        this.gameLoop();

    }

    /**
     * Main game loop. Keeps control on what player is next and asks the respective player for their move.
     */
    private void gameLoop(){

        //Loop until a winner is found.
        while(true){

            //Check if the game is done.
            this.checkGameOver();

            Move move = null;

            //Computer's turn to make a move
            if(computer == this.next){
                System.out.println("Computer is thinking...");

                //Call the search engine and make the move.
                move = agent.search(board);
                board.move(computer, move);

                this.moves.add(move);

                //Print the board
                this.printBoard();
                System.out.println("Computer's move is: " + move);

            }else{ //Opponent's turn

                //Get the move from the player and perform it.
                move = getMove();
                board.move(opponent, move);
                this.moves.add(move);

                this.printBoard();
            }

            //Flip the turns
            this.next = Player.opponent(this.next);
        }

    }

    /**
     * Checks to see if the game is over.
     * If so, the correct message is printed and the program exits.
     */
    private void checkGameOver() {

        //If the next player has a terminal state,
        //It means the player who has to move next has lost the game.
        if(board.isTerminal(next)){

            //Print the final board and the appropriate win message.
            this.printBoard();
            System.out.println((next == computer)? "Player wins!" : "Computer wins!");

            //Exit the game.
            System.exit(0);
        }

    }

    /**
     * Asks the user who will start the game.
     * @return The Player value assigned to the computer.
     */
    private Player getComputerPlayer() {

        char next = ' ';

        //Keep looping until an appropriate response has been given.
        while(true){

            //Ask the user
            System.out.print("Who goes first, C for computer, O for opponent: ");

            //Get the user input
            next = in.next().toUpperCase().charAt(0);
            in.nextLine();

            //If the user wants the computer to start
            if(next == 'C'){
                System.out.println("The computer will start the game.");
                this.printBoard();
                return Player.X;
            }else if(next == 'O'){ //If the user wants the opponent to start
                System.out.println("The opponent will start the game.");
                this.printBoard();
                return Player.O;
            }

            //If this point is reached, the user gave a bad input.
            //Prompt again.
            System.out.println("Invalid input. Try again.");

        }
    }

    /**
     * Prints the board with the current state of the board, and the moves by each player.
     * It will adapt to print the opponent or the computer's moves first, depending on who started the game.
     */
    private void printBoard(){

        //Get the string representation of the board.
        String[] boardString = this.board.getPrintableBoard();

        //Get an iterator for the moves done by the players.
        Iterator<Move> movesIterator = this.moves.iterator();

        //Print the numbers for the board and the appropriate moves header, depending on who started the game.
        System.out.print(boardString[0] + "\t");
        System.out.println(this.computer == Player.X? "Computer vs. Opponent": "Opponent vs. Computer");

        //Keep looping while there are board rows or moves to print, whichever is greater.
        for(int counter = 1; counter < boardString.length || movesIterator.hasNext(); counter++){

            //If there is more board, print it. Otherwise, print tab spaces for the moves.
            if(counter < boardString.length){
                System.out.print(boardString[counter]);
            }else{
                System.out.print("\t\t");
            }

            //If there are more moves to print
            if(movesIterator.hasNext()){

                //Print a number and the next move.
                System.out.print("\t   " + (counter) + ". " + movesIterator.next());

                //Since there may be 2 moves in the same line, check if there are more moves and print them.
                if(movesIterator.hasNext()){
                    System.out.print("\t" + movesIterator.next());
                }
            }

            System.out.println();
        }

    }

    /**
     * Gets the amount of time that the computer is alloted per move to perform its calculations.
     * @return
     */
    private int getTimeout(){

        int timeout = -1;

        //Keep looping until the user provides the appropriate information.
        while(true){

            //Promp the user.
            System.out.print("What is the computer timeout to make a move? Enter in whole number seconds: ");


            try{
                //Get the input and try to convert it to an integer.
                timeout = Integer.parseInt(in.next());
                in.nextLine();

                //If the number is greater than 0, return it.
                if(timeout > 0)
                    return timeout;

            }catch(NumberFormatException e){
                //The user provided input that wasn't a number.
                //Empty the scanner and prompt again.
                in.nextLine();
            }

            //If this point is reached, the user provided wrong input.
            //Prompt and try again.
            System.out.println("Invalid input. Please try again.");

        }

    }

    /**
     * Gets the next move that the user wants to perform.
     * @return move inputted by the user.
     */
    private Move getMove(){

        //Keep looping until the user provides a valid move.
        while(true){

            //Prompt the user
            System.out.print("Enter opponent's move: ");

            //Get the user input
            String input = in.next().toUpperCase().trim();
            in.nextLine();

            //Validate that the input is valid.
            Move move = validateMove(input);

            //If valid, return it. If invalid, prompt the user and try again.
            if(move != null)
                return move;
            else
                System.out.println("Invalid input. Please try again.");
                
        }
    }

    /**
     * Validates that the move is valid with the current board.
     * @param input String that was passed by the user.
     * @return a Move object if the move is valid. Null otherwise.
     */
    private Move validateMove(String input){

        //If the move is not length 2 (example: A1, C3, D6)
        //It is invalid. Return.
        if(input.length() != 2)
            return null;

        //Get the row and column and convert them from characters to int representations.
        int row = (int)(input.charAt(0)) - 65;
        int col = (int)(input.charAt(1)) - 49;

        //Check that the move is valid inside the board.
        if(row < 0 || row > 7)
            return null;

        if(col < 0 || col > 7)
            return null;

        
        //Create a new Move object with the row and column specified.
        Move move = new Move(row, col);

        //Check that the user can perform the move with the current board configuration.
        //If it is valid, return the move. If not, return Null.
        return (this.board.isMoveValid(move, this.next))? move: null;

    }

}