package cl.duoc.EFT_S9_GRUPO11.repository;

import cl.duoc.EFT_S9_GRUPO11.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
}