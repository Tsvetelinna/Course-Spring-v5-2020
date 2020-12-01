package course.spring.exceptions;

public class ForbiddenAccessEntityException extends RuntimeException {
    public ForbiddenAccessEntityException() {
    }

    public ForbiddenAccessEntityException(String message) {
        super(message);
    }

    public ForbiddenAccessEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenAccessEntityException(Throwable cause) {
        super(cause);
    }
}
