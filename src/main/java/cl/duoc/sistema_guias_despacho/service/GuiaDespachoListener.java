package cl.duoc.sistema_guias_despacho.service;

import cl.duoc.sistema_guias_despacho.model.GuiaDespacho;
import cl.duoc.sistema_guias_despacho.repository.GuiaDespachoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuiaDespachoListener {

    @Autowired
    private GuiaDespachoRepository repository;

    // Este método está siempre escuchando la Cola 1 (Normal)
    @RabbitListener(queues = "${app.queue.normal}")
    public void consumirGuiaNormal(GuiaDespacho guia) {
        System.out.println("📥 Mensaje recibido desde la Cola 1. Guardando en Oracle Cloud...");
        
        // Aquí es donde la magia ocurre y la guía pasa de la cola a la base de datos
        repository.save(guia);
        
        System.out.println("✅ Guía guardada exitosamente en la base de datos.");
    }

    // Este método escucha la Cola 2 por si el Productor reportó errores
    @RabbitListener(queues = "${app.queue.error}")
    public void consumirGuiaError(GuiaDespacho guia) {
        System.err.println("⚠️ Mensaje recibido en la Cola de Errores. Requiere revisión manual.");
        // Aquí puedes agregar lógica adicional a futuro si la pauta lo pide
    }
}