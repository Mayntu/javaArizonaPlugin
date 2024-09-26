package arizona.tools.plugin.controllers;

import arizona.tools.plugin.dto.CreateTokenRequest;
import arizona.tools.plugin.dto.CreateTokenResponse;
import arizona.tools.plugin.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateTokenResponse createToken(@RequestBody CreateTokenRequest createTokenRequest) {
        return tokenService.generateToken(createTokenRequest);
    }
}
