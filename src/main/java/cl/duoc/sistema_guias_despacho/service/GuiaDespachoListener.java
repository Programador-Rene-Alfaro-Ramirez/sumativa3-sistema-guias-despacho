package cl.duoc.sistema_guias_despacho.service;

import cl.duoc.sistema_guias_despacho.model.GuiaDespacho;
import cl.duoc.sistema_guias_despacho.repository.GuiaDespachoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuiaDespachoListener {

    @Autowired
    private GuiaDespachoRepository repository;

    // Consume de la Cola 1 y guarda en la base de datos
    @RabbitListener(queues = "${app.queue.normal}")
    public void procesarGuia(GuiaDespacho guia) {
        System.out.println("Mensaje recibido en Cola 1. Guardando en Oracle Cloud...");
        try {
            repository.save(guia);
            System.out.println("Registro insertado correctamente en la nueva tabla.");
        } catch (Exception e) {
            System.err.println("Fallo al insertar en base de datos: " + e.getMessage());
        }
    }
}