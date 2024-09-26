package arizona.tools.plugin.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "tokens")
public class TokenModel {
    @Id
    private UUID id;
    private boolean isActivated;
    private String hwid;
    private boolean isOk;
    private Date createdTime;
    private long liveTime;
}
