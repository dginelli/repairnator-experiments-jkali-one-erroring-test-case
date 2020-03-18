package by.imsha.domain.dto.mapper;

import by.imsha.domain.City;
import by.imsha.domain.dto.CityInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author Alena Misan
 */
@Mapper
public interface CityMapper {
    CityMapper MAPPER = Mappers.getMapper(CityMapper.class);

    @Mappings({
            @Mapping(source = "name", target = "name")
    })
    City toCity(CityInfo cityInfo);

    void updateCityFromDTO(CityInfo cityInfo, @MappingTarget City city);
}
