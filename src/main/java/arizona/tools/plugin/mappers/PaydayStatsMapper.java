package arizona.tools.plugin.mappers;

import arizona.tools.plugin.dto.PaydayStats;
import arizona.tools.plugin.models.PaydayStatModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PaydayStatsMapper {
    PaydayStatsMapper INSTANCE = Mappers.getMapper(PaydayStatsMapper.class);

    // Mapping PaydayStatModel to PaydayStats DTO
    @Mapping(source = "datetime", target = "dateTime")
    PaydayStats toDto(PaydayStatModel paydayStatModel);

    // Mapping PaydayStats DTO back to PaydayStatModel
    @Mapping(source = "dateTime", target = "datetime")
    @Mapping(target = "id", expression = "java(getRandomUUID())")
    PaydayStatModel toModel(PaydayStats paydayStats);

    // Nested Property mapping for PaydayStatModel.Property -> PaydayStats.Property
    List<PaydayStats.Property> mapProperties(List<PaydayStatModel.Property> properties);
    List<PaydayStatModel.Property> mapToModelProperties(List<PaydayStats.Property> properties);


    default UUID getRandomUUID() {
        return UUID.randomUUID();
    }
}
