package by.imsha.domain.dto.mapper;

import by.imsha.domain.Coordinate;
import by.imsha.domain.dto.LocationInfo;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author Alena Misan
 */
@Mapper
public interface LocationInfoMapper {
    LocationInfoMapper MAPPER = Mappers.getMapper(LocationInfoMapper.class);

    LocationInfo toLocationInfo(Coordinate coordinate);



}
