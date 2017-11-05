package db;

public class ErrorException extends RuntimeException {
    public PrintMessages p;
    
    public ErrorException(int flag) {
        p = new PrintMessages(flag);
    }
    
    public ErrorException(int flag, String str) {
        p = new PrintMessages(flag, str);
    }
}