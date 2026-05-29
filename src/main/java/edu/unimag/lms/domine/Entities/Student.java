package edu.unimag.lms.domine.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "create_at") private Instant createAt;
    @Column(name = "update_at") private Instant UpdateAt;

    @OneToMany(mappedBy = "student")
    private Set<Assessment> assessments;

    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments;

}
