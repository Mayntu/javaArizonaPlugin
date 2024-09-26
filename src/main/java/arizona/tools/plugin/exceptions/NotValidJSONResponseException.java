package arizona.tools.plugin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotValidJSONResponseException extends InternalServerErrorException {
    public NotValidJSONResponseException() {
        super("not valid json response exception");
    }
    public NotValidJSONResponseException(String message) {
        super(message);
    }
}
