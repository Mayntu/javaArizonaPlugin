package arizona.tools.plugin.controllers;

import arizona.tools.plugin.dto.*;
import arizona.tools.plugin.services.PluginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PluginController {
    private final PluginService pluginService;

    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getCaptchas")
    public GetCaptchasResponse getCaptchas(@Valid @RequestBody GetCaptchasRequest request) {
        return pluginService.getCaptchas(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/convertTimeInMoscow")
    public ConvertTimeInMoscowResponse convertTimeInMoscow(@Valid @RequestBody ConvertTimeInMoscowRequest request) {
        return pluginService.convertTimeInMoscow(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/calcTax")
    public CalcTaxResponse calcTax(@Valid @RequestBody CalcTaxRequest request) {
        return pluginService.calcTax(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/searchProperty")
    public SearchPropertyResponse searchProperty(@Valid @RequestBody SearchPropertyRequest request) {
        return pluginService.searchProperty(request);
    }
}
