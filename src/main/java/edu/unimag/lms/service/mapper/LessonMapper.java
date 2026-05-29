package edu.unimag.lms.service.mapper;

import edu.unimag.lms.api.dto.LessonDto.*;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Lesson;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "id",     ignore = true)
    @Mapping(target = "course", expression = "java(courseRef(request.courseId()))")
    Lesson toEntity(LessonCreateRequest request);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toResponse(Lesson lesson);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "course", ignore = true)   // se resuelve en el service
    void updateFromRequest(LessonUpdateRequest request, @MappingTarget Lesson lesson);

    default Course courseRef(java.util.UUID id) {
        if (id == null) return null;
        Course c = new Course();
        c.setId(id);
        return c;
    }
}