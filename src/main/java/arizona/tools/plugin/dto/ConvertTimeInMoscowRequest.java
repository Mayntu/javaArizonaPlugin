package arizona.tools.plugin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConvertTimeInMoscowRequest {
    @NotNull(message = "time must present")
    private Object time;
    @NotNull(message = "time must present")
    private Boolean isNumber;
}
