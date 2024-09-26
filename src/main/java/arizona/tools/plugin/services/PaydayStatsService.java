package arizona.tools.plugin.services;

import arizona.tools.plugin.dto.KafkaTopics;
import arizona.tools.plugin.dto.PaydayStats;
import arizona.tools.plugin.models.PaydayStatModel;
import arizona.tools.plugin.repositories.PaydayStatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

// KafkaProducer
@Service
public class PaydayStatsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PaydayStatsService.class);

    private final KafkaTemplate<String, PaydayStats> kafkaTemplate;
    private final PaydayStatRepository paydayStatRepository;

    public PaydayStatsService(KafkaTemplate<String, PaydayStats> kafkaTemplate, PaydayStatRepository paydayStatRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.paydayStatRepository = paydayStatRepository;
        save(new PaydayStats(10, List.of(new PaydayStats.Property(7, 5)), new Date()));
    }

    public List<PaydayStatModel> getByServerNumber(int serverNumber) {
        return paydayStatRepository.findByServerNumber(serverNumber);
    }

    public List<PaydayStatModel> getAll() {
        return paydayStatRepository.findAll();
    }

    public void save(PaydayStats savePaydayRequest) {
        LOGGER.debug(String.format("saving message %s", savePaydayRequest.toString()));
        kafkaTemplate.send(KafkaTopics.PAYDAY_STATS.name(), savePaydayRequest);
    }
}
