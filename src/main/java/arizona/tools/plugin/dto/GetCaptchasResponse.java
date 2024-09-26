package arizona.tools.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetCaptchasResponse {
    private List<String> captchas;
}
