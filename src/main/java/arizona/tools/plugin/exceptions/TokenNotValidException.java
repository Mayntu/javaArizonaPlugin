package arizona.tools.plugin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenNotValidException extends NotValidException {
    public TokenNotValidException() {
        super("token not valid exception");
    }

    public TokenNotValidException(String message) {
        super(message);
    }
}
