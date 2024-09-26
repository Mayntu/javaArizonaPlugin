package arizona.tools.plugin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetCaptchasRequest {
    @NotNull
    @Min(value = 1, message = "count must be at least 1")
    private int count;
    @NotNull
    @Min(value = 1, message = "zero chance must be at least 1")
    private int zeroChance;
}
