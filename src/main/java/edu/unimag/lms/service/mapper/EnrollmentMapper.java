package edu.unimag.lms.service.mapper;

import edu.unimag.lms.api.dto.EnrollmentDto.*;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Enrollment;
import edu.unimag.lms.domine.Entities.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "student",    expression = "java(studentRef(request.studentId()))")
    @Mapping(target = "course",     expression = "java(courseRef(request.courseId()))")
    Enrollment toEntity(EnrollmentCreateRequest request);

    @Mapping(target = "Id",        source = "id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId",  source = "course.id")
    EnrollmentResponse toResponse(Enrollment enrollment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student",    ignore = true)
    @Mapping(target = "course",     ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    void updateFromRequest(EnrollmentUpdateRequest request, @MappingTarget Enrollment enrollment);

    default Student studentRef(java.util.UUID id) {
        if (id == null) return null;
        Student s = new Student();
        s.setId(id);
        return s;
    }

    default Course courseRef(java.util.UUID id) {
        if (id == null) return null;
        Course c = new Course();
        c.setId(id);
        return c;
    }
}