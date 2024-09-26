package arizona.tools.plugin.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerNotRespondingException extends InternalServerErrorException {
    public ServerNotRespondingException() { super("server not responding exception"); }
    public ServerNotRespondingException(String message) { super(message); }
}
