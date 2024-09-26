package arizona.tools.plugin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatNotValidException extends NotValidException {
    public FormatNotValidException() { super("format not valid exception"); }
    public FormatNotValidException(String message) { super(message); }
}
