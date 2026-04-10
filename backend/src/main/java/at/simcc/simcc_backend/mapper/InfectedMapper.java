package at.simcc.simcc_backend.mapper;

import at.simcc.simcc_backend.entities.Infected;
import at.simcc.simcc_backend.models.InfectedDto;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedNoIdDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Mapper(componentModel = "spring")
public interface InfectedMapper {

    Infected toEntity(InfectedDto infectedDto);

    InfectedDto toGenericDto(Infected infected);

    Infected toEntity(InfectedIdDto infectedIdDto);

    InfectedIdDto toDtoId(Infected infected);
}
