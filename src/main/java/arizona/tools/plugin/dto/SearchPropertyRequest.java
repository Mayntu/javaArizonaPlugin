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
public class SearchPropertyRequest {
    @NotNull(message = "start must present")
    private int start;
    @NotNull(message = "end must present")
    private int end;
    @NotNull(message = "serverNumber must present")
    private int serverNumber;
}
