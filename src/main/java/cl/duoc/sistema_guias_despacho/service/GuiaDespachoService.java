package cl.duoc.sistema_guias_despacho.service;

import cl.duoc.sistema_guias_despacho.model.GuiaDespacho;
import cl.duoc.sistema_guias_despacho.repository.GuiaDespachoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GuiaDespachoService {

    @Autowired
    private GuiaDespachoRepository repository;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${app.queue.normal}")
    private String queueNormal;

    @Value("${app.queue.error}")
    private String queueError;

    // 1. Crear guia asíncrona enviando a la cola
    public GuiaDespacho crearGuia(GuiaDespacho guia) {
        try {
            System.out.println("Enviando guía a la Cola 1...");
            rabbitTemplate.convertAndSend(queueNormal, guia);
        } catch (Exception e) {
            System.err.println("Error de envío. Redirigiendo a Cola 2 (Errores): " + e.getMessage());
            rabbitTemplate.convertAndSend(queueError, guia);
        }
        return guia; 
    }

    // 2. Subir archivo a AWS S3 y enlazarlo a la guía
    public GuiaDespacho subirDocumentoS3(Long idGuia, MultipartFile archivo) throws IOException {
        Optional<GuiaDespacho> guiaOpt = repository.findById(idGuia);
        if (guiaOpt.isPresent()) {
            GuiaDespacho guia = guiaOpt.get();
            
            // Generar un nombre único para el archivo en el bucket
            String fileName = "guia-" + idGuia + "-" + archivo.getOriginalFilename();
            
            // Preparar la subida a Amazon
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            
            // Ejecutar la subida del archivo
            s3Client.putObject(putOb, RequestBody.fromInputStream(archivo.getInputStream(), archivo.getSize()));
            
            // Armar la URL y guardarla en la base de datos
            String urlS3 = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            guia.setUrlArchivoS3(urlS3);
            
            return repository.save(guia);
        }
        return null;
    }

    // 3. Consultar por transportista y fecha
    public List<GuiaDespacho> buscarPorTransportistaYFecha(String transportista, LocalDate fecha) {
        return repository.findByTransportistaAndFecha(transportista, fecha);
    }

    // 4. Modificar o actualizar guía
    public GuiaDespacho actualizarGuia(Long id, GuiaDespacho detallesNuevos) {
        Optional<GuiaDespacho> guiaExistente = repository.findById(id);
        if (guiaExistente.isPresent()) {
            GuiaDespacho guia = guiaExistente.get();
            guia.setTransportista(detallesNuevos.getTransportista());
            guia.setFecha(detallesNuevos.getFecha());
            guia.setDetallesPedido(detallesNuevos.getDetallesPedido());
            return repository.save(guia);
        }
        return null;
    }

    // 5. Eliminar guía
    public void eliminarGuia(Long id) {
        repository.deleteById(id);
    }
    
    // 6. Descargar guía
    public String obtenerUrlDescarga(Long id) {
        Optional<GuiaDespacho> guia = repository.findById(id);
        return guia.map(GuiaDespacho::getUrlArchivoS3).orElse("Guía no encontrada o sin documento adjunto");
    }
} 