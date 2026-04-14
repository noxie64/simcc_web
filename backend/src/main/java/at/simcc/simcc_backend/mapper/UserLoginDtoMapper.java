package at.simcc.simcc_backend.mapper;

import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.models.UserLoginDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 11:05
 */
@Mapper(componentModel = "spring")
public interface UserLoginDtoMapper {
    User toEntity(UserLoginDto customerDto);
    List<User> toEntity(List<UserLoginDto> customerDto);
    UserLoginDto toDto(User customer);
    List<UserLoginDto> toDto(List<User> customers);
}
