package cl.duoc.EFT_S9_GRUPO11.controller;

import cl.duoc.EFT_S9_GRUPO11.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoController {

    @Autowired
    private S3Service s3Service;

    // Endpoint: POST http://localhost:8080/api/archivos/subir
    @PostMapping("/subir")
    public ResponseEntity<Map<String, String>> subirMaterial(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Llamamos al servicio para subir a AWS
            String url = s3Service.subirArchivo(file);
            
            response.put("status", "SUCCESS");
            response.put("message", "Archivo subido correctamente a la nube.");
            response.put("url", url); // Esta es la URL que luego asociaremos a un Curso
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error al comunicarse con AWS S3: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}