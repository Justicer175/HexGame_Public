package hr.java.game.hex_projekt_maksrezek.exception;

public class WrongPlayerName extends RuntimeException{
    public WrongPlayerName() {
    }

    public WrongPlayerName(String message) {
        super(message);
    }

    public WrongPlayerName(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPlayerName(Throwable cause) {
        super(cause);
    }

    public WrongPlayerName(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
