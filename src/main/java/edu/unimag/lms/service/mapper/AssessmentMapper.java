package edu.unimag.lms.service.mapper;

import edu.unimag.lms.api.dto.AssessmentDto.*;
import edu.unimag.lms.domine.Entities.Assessment;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "student",  expression = "java(studentRef(request.studentId()))")
    @Mapping(target = "course",   expression = "java(courseRef(request.courseId()))")
    Assessment toEntity(AssessmentCreateRequest request);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId",  source = "course.id")
    AssesmentResponse toResponse(Assessment assessment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course",  ignore = true)
    void updateFromRequest(AssesmentUpdateRequest request, @MappingTarget Assessment assessment);


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