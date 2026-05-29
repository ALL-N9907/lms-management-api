package edu.unimag.lms.service.mapper;

import edu.unimag.lms.api.dto.CourseDto.*;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Instructor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "createAt",   ignore = true)
    @Mapping(target = "updateAt",   ignore = true)
    @Mapping(target = "enrollments",ignore = true)
    @Mapping(target = "assessments",ignore = true)
    @Mapping(target = "lessons",    ignore = true)
    @Mapping(target = "instructor", expression = "java(instructorRef(request.instructorId()))")
    Course toEntity(CourseCreateRequest request);

    @Mapping(target = "instructorId",  source = "instructor.id")
    @Mapping(target = "Enrollments",   expression = "java(course.getEnrollments() != null ? course.getEnrollments().size() : 0)")
    @Mapping(target = "lessons",       expression = "java(course.getLessons() != null ? new java.util.ArrayList<>(course.getLessons()) : java.util.List.of())")
    @Mapping(target = "createdAt",     source = "createAt")
    CourseResponse toResponse(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createAt",   ignore = true)
    @Mapping(target = "enrollments",ignore = true)
    @Mapping(target = "assessments",ignore = true)
    @Mapping(target = "lessons",    ignore = true)
    @Mapping(target = "instructor", ignore = true)
    void updateFromRequest(CourseUpdateRequest request, @MappingTarget Course course);

    default Instructor instructorRef(java.util.UUID id) {
        if (id == null) return null;
        Instructor i = new Instructor();
        i.setId(id);
        return i;
    }
}