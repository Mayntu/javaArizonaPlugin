package arizona.tools.plugin.services;

import arizona.tools.plugin.dto.PaydayStats;
import arizona.tools.plugin.mappers.PaydayStatsMapper;
import arizona.tools.plugin.models.PaydayStatModel;
import arizona.tools.plugin.repositories.PaydayStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaydayStatConsumer {
    private final PaydayStatRepository paydayStatRepository;
    private final PaydayStatsMapper paydayStatsMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaydayStatConsumer.class);
    private static final int MAX_PAYDAY_STATS = 20;

    public PaydayStatConsumer(PaydayStatRepository paydayStatRepository, PaydayStatsMapper paydayStatsMapper) {
        this.paydayStatRepository = paydayStatRepository;
        this.paydayStatsMapper = paydayStatsMapper;
    }

    @KafkaListener(topics = "PAYDAY_STATS")
    public void consumePaydayStats(PaydayStats paydayStats) {
        LOGGER.info(String.format("recieved message : %s", paydayStats));
        Optional<PaydayStatModel> paydayStatModelOptional = paydayStatRepository.findFirstByOrderByDatetimeAsc(paydayStats.getServerNumber());

        PaydayStatModel newPaydayStatModel = paydayStatsMapper.toModel(paydayStats);

        if (!paydayStatModelOptional.isPresent()) {
            LOGGER.info(String.format("adding new info : %s", newPaydayStatModel));
            paydayStatRepository.save(newPaydayStatModel);
            return;
        }

        if (paydayStatRepository.countByServerNumber(paydayStats.getServerNumber()) >= 20) {
            deleteOldestPaydayStat(paydayStats.getServerNumber());
        }

        PaydayStatModel paydayStatModel = paydayStatModelOptional.get();

        if (newPaydayStatModel.canOverwrite(paydayStatModel)) {
            LOGGER.info(String.format("overriding true : NEW = (%s) ||||| PREV = (%s)", newPaydayStatModel, paydayStatModel));
            paydayStatRepository.save(newPaydayStatModel);
        } else {
            LOGGER.info(String.format("overriding false : NEW = (%s) ||||| PREV = (%s)", newPaydayStatModel, paydayStatModel));
        }
    }

    private void deleteOldestPaydayStat(int serverNumber) {
        paydayStatRepository.findFirstByOrderByDatetimeDesc(serverNumber)
                .ifPresent(paydayStatRepository::delete);
    }
}
