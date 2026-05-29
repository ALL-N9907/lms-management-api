package edu.unimag.lms.service.mapper;

import edu.unimag.lms.api.dto.StudentDto.*;
import edu.unimag.lms.domine.Entities.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "createAt",    ignore = true)
    @Mapping(target = "updateAt",    ignore = true)
    @Mapping(target = "assessments", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Student toEntity(StudentCreateRequest request);

    @Mapping(target = "createdAt",  source = "createAt")
    @Mapping(target = "updatedAt",  source = "updateAt")
    StudentResponse toResponse(Student student);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createAt",    ignore = true)
    @Mapping(target = "updateAt",    ignore = true)
    @Mapping(target = "assessments", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "fullName",    ignore = true)
    void updateFromRequest(StudentUpdateRequest request, @MappingTarget Student student);
}