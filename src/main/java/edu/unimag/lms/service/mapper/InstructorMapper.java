package edu.unimag.lms.service.mapper;

import edu.unimag.lms.api.dto.InstructorDto.*;
import edu.unimag.lms.domine.Entities.Instructor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "profile",  ignore = true)
    @Mapping(target = "courses",  ignore = true)
    Instructor toEntity(InstructorCreateRequest request);

    @Mapping(target = "createdAt", source = "createAt")
    InstructorResponse toResponse(Instructor instructor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "profile",  ignore = true)
    @Mapping(target = "courses",  ignore = true)
    @Mapping(target = "fullName", ignore = true)
    void updateFromRequest(InstructorUpdateRequest request, @MappingTarget Instructor instructor);
}