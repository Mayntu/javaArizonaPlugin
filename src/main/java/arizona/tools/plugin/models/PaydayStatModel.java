package arizona.tools.plugin.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "payday_stats")
public class PaydayStatModel {
    @Id
    private UUID id;
    private int serverNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm:ss")
    private Date datetime;
    private List<Property> properties;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Property {
        private int id;
        private int paydayCount;
    }

    public boolean canOverwrite(PaydayStatModel other) {
        if (this.getProperties().size() != other.getProperties().size()) {
            return true;
        }

        for (int i = 0; i < this.getProperties().size(); i++) {
            Property property1 = this.getProperties().get(i);
            Property property2 = other.getProperties().get(i);
            if (property1.getId() != property2.getId() || property1.getPaydayCount() != property2.getPaydayCount()) {
                return true;
            }
        }
        return false;
    }
}
