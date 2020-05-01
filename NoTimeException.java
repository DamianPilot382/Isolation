

public class NoTimeException extends Throwable {

    public NoTimeException(){
        super("The timer has expired. The computer must make a move now.");
    }

}