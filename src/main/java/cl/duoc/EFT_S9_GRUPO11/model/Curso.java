package cl.duoc.EFT_S9_GRUPO11.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CURSOS")
@Data
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "instructor_id")
    private Long instructorId;

    @Column(name = "material_url", length = 500)
    private String materialUrl; 
}