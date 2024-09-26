package arizona.tools.plugin.services;

import arizona.tools.plugin.dto.CreateTokenRequest;
import arizona.tools.plugin.dto.CreateTokenResponse;
import arizona.tools.plugin.exceptions.TokenPassNotValidException;
import arizona.tools.plugin.models.TokenModel;
import arizona.tools.plugin.repositories.TokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${token.secret.pass}")
    private String tokenPass;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateTokenResponse generateToken(CreateTokenRequest createTokenRequest) {
        checkSecret(createTokenRequest.getSecret());

        TokenModel tokenModel = new TokenModel(UUID.randomUUID(), false, null, true, new Date(), 60L * 1000);
        tokenRepository.save(tokenModel);
        return new CreateTokenResponse(tokenModel.getId().toString());
    }

    public void checkSecret(String secret) {
        if (!passwordEncoder.matches(secret, tokenPass)) {
            throw new TokenPassNotValidException();
        }
    }

    public boolean isTokenValid(String token, String hwid) {
        if (token.isBlank() || hwid.isBlank()) {
            return false;
        }

        UUID tokenUUID;

        try {
            tokenUUID = UUID.fromString(token);
        } catch (Exception exception) {
            return false;
        }

        TokenModel tokenModel = tokenRepository.findById(tokenUUID).orElse(null);
        if (tokenModel == null) {
            return false;
        }

        if (tokenModel.isActivated()) {
            return tokenModel.getHwid().equals(hwid) && tokenModel.isOk() && !isTokenExpired(tokenModel);
        }
        tokenModel.setHwid(hwid);
        tokenModel.setActivated(true);
        tokenRepository.save(tokenModel);
        return true;
    }

    public boolean isTokenExpired(TokenModel tokenModel) {
        return new Date(tokenModel.getCreatedTime().getTime() + tokenModel.getLiveTime()).before(new Date());
    }
}
