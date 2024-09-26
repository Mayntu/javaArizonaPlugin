package arizona.tools.plugin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenPassNotValidException extends NotValidException {
    public TokenPassNotValidException() {
        super("token pass not valid exception");
    }

    public TokenPassNotValidException(String message) {
        super(message);
    }
}
