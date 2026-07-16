package cl.duoc.EFT_S9_GRUPO11.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionRequest implements Serializable {
    
    private Long estudianteId;
    private Long cursoId;
    private String emailEstudiante;

}