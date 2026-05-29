package edu.unimag.lms.domine.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "instructor_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Instructor_Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    private Instructor instructor;

    @Column(name= "phone", nullable = false)
    private String phone;

    @Column(name= "bio", nullable = false)
    private String bio;

}
