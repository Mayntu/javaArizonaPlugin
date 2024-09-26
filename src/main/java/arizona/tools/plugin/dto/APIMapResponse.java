package arizona.tools.plugin.dto;

import arizona.tools.plugin.deserializers.NoAuctionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class APIMapResponse {
    private Houses houses;
    private Businesses businesses;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Houses {
        private List<Item> onAuction;
        private List<Item> hasOwner;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Businesses {
        private List<Item> onAuction;
        @JsonDeserialize(using = NoAuctionDeserializer.class)
        private List<Item> noAuction;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Item {
        private String owner;
        private int id;
    }
}
