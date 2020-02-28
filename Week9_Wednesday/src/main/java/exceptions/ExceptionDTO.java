package exceptions;

public class ExceptionDTO {

    public ExceptionDTO(int code, String description) {
        this.code = code;
        if (code == 500) {
            this.message = "Server problem. We apologize";
        } else {
            this.message = description;
        }
    }
    private int code;
    private String message;
}