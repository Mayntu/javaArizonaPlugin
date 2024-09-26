package arizona.tools.plugin.controllers;

import arizona.tools.plugin.dto.PaydayStats;
import arizona.tools.plugin.models.PaydayStatModel;
import arizona.tools.plugin.services.PaydayStatsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paydayStats")
public class PaydayStatsController {
    private final PaydayStatsService paydayStatsService;

    public PaydayStatsController(PaydayStatsService paydayStatsService) {
        this.paydayStatsService = paydayStatsService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<PaydayStatModel> getAll() {
        return paydayStatsService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{serverNumber}")
    public List<PaydayStatModel> getByServerNumber(@PathVariable int serverNumber) {
        return paydayStatsService.getByServerNumber(serverNumber);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void postStats(@Valid @RequestBody PaydayStats request) {
        paydayStatsService.save(request);
    }
}
