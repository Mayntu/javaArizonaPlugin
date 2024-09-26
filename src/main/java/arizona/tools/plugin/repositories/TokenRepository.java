package arizona.tools.plugin.repositories;

import arizona.tools.plugin.models.TokenModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TokenRepository extends MongoRepository<TokenModel, UUID> {
}
