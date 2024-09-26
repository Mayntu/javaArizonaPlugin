package arizona.tools.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalcTaxResponse {
    private int hours, days, daysLeftHours;
    private String dateWhenExpire;
}
