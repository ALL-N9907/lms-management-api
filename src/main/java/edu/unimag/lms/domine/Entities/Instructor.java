package edu.unimag.lms.domine.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name ="full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(name = "create_at") private Instant createAt;
    @Column(name = "update_at") private Instant updateAt;

     @OneToOne(mappedBy = "instructor")
     private Instructor_Profile profile;

     @OneToMany(mappedBy = "instructor")
    private Set<Course> courses;
}
