package arizona.tools.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalcTaxRequest {
    private boolean calcInMskTime;
    private int nalogNow;
    private int nalogInHour;
    private Property property;
    private boolean insurance;
    private int time;
    private int timeOffset;
}
