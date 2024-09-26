package arizona.tools.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchPropertyResponse {
    private Map<String, UserInfo> result;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserInfo {
        private int housesCount;
        private List<Integer> housesIds;
        private int businessesCount;
        private List<Integer> businessesIds;
    }
}
