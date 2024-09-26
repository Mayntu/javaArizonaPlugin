package arizona.tools.plugin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaydayStats {
    @NotNull(message = "server number must be presented")
    private int serverNumber;
    private List<Property> properties;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateTime;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Property {
        private int id;
        private int paydayCount;
    }
}
