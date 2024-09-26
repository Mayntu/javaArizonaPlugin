package arizona.tools.plugin.repositories;

import arizona.tools.plugin.models.PaydayStatModel;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaydayStatRepository extends MongoRepository<PaydayStatModel, UUID> {
    @Aggregation(pipeline = {
            "{ '$match' : { 'serverNumber' : ?0 } }",
            "{ '$sort' : { 'datetime' : 1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<PaydayStatModel> findFirstByOrderByDatetimeAsc(int serverNumber);

    @Aggregation(pipeline = {
            "{ '$match' : { 'serverNumber' : ?0 } }",
            "{ '$sort' : { 'datetime' : -1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<PaydayStatModel> findFirstByOrderByDatetimeDesc(int serverNumber);

    long countByServerNumber(int serverNumber);

    List<PaydayStatModel> findByServerNumber(int serverNumber);
}
