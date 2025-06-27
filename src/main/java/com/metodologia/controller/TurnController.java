package com.metodologia.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metodologia.controller.request.CreateTurnDTO;
import com.metodologia.controller.request.PaymentCardDTO;
import com.metodologia.models.EState;
import com.metodologia.models.PaymentCard;
import com.metodologia.models.Servicio;
import com.metodologia.models.ServicioSolicitado;
import com.metodologia.models.ServicioSolicitadoDetalle;
import com.metodologia.models.UserEntity;
import com.metodologia.repositories.PaymentCardRepository;
import com.metodologia.repositories.ServicioRepository;
import com.metodologia.repositories.ServicioSolicitadoRepository;
import com.metodologia.repositories.UserRepository;
import com.metodologia.service.EmailService;

import jakarta.validation.Valid;

@RestController
public class TurnController {

    @Autowired
    private ServicioSolicitadoRepository servicioSolicitadoRepo;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaymentCardRepository pagoTarjetaRepo;

    @PutMapping("/turno/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");

        ServicioSolicitado turno = servicioSolicitadoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        try {
            EState estadoEnum = EState.valueOf(nuevoEstado.toUpperCase());
            turno.setEstado(estadoEnum);
            servicioSolicitadoRepo.save(turno);
        return ResponseEntity.ok("Estado actualizado correctamente");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Estado inválido. Debe ser: PENDIENTE, EN_PROCESO, FINALIZADO, etc.");
    }
    }

    @PostMapping("/solicitarTurno")
    public String createTurn(@Valid @RequestBody CreateTurnDTO createTurnDTO){
       UserEntity solicitante = userRepository.findById(createTurnDTO.getSolicitanteId())
            .orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));

        UserEntity profesional = userRepository.findById(createTurnDTO.getProfesionalId())
            .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        List<Servicio> servicios = (List<Servicio>) servicioRepository.findAllById(createTurnDTO.getServicioIds());

        // Creamos el turno vacío primero para poder enlazarlo en los detalles
        ServicioSolicitado servicioSolicitado = ServicioSolicitado.builder()
            .solicitante(solicitante)
            .profesional(profesional)
            .fecha(createTurnDTO.getFecha())
            .hora(createTurnDTO.getHora())
            .estado(EState.PENDIENTE_PAGO) // o el estado que corresponda
            .build();

        // Crear los detalles a partir de los servicios
        List<ServicioSolicitadoDetalle> detalles = servicios.stream()
            .map(servicio -> ServicioSolicitadoDetalle.builder()
                .servicio(servicio)
                .servicioSolicitado(servicioSolicitado)
                .build())
            .toList();

        // Asignar los detalles al turno
        servicioSolicitado.setDetalles(detalles);

        // Guardar el turno (gracias al cascade = ALL, los detalles se guardan automáticamente)
        servicioSolicitadoRepo.save(servicioSolicitado);
        String emailDestino = solicitante.getUsername(); // O profesional.getEmail()
        String subject = "Confirmación de Turno";
        String body = "Hola " + solicitante.getNombre() + ",\n\n" +
              "Tu turno ha sido solicitado con éxito.\n" +
              "Profesional: " + profesional.getUsername() + "\n" +
              "Fecha: " + createTurnDTO.getFecha() + "\n" +
              "Hora: " + createTurnDTO.getHora() + "\n" +
              "Servicios: " + servicios.stream().map(Servicio::getNombre).collect(Collectors.joining(", ")) + "\n" +
              "RECUERDE QUE SU TURNO SE ENCUENTRA PENDIENTE DE PAGO, FAVOR DE ABONAR AL MOMENTO DE ASISTIR. \n\n" +            
              "Gracias por usar nuestra plataforma."
              ;

        emailService.sendTurnoEmail(emailDestino, subject, body);
        return "Se ha creado el turno";
    }

    @PostMapping("/solicitarTurnoConPago")
public ResponseEntity<String> createTurnWithPayment(@Valid @RequestBody PaymentCardDTO dto) {
    UserEntity solicitante = userRepository.findById(dto.getSolicitanteId())
        .orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));

    UserEntity profesional = userRepository.findById(dto.getProfesionalId())
        .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

    List<Servicio> servicios = (List<Servicio>) servicioRepository.findAllById(dto.getServicioIds());

    // Crear turno (vacío primero para tener ID)
    ServicioSolicitado servicioSolicitado = ServicioSolicitado.builder()
        .solicitante(solicitante)
        .profesional(profesional)
        .fecha(dto.getFecha())
        .hora(dto.getHora())
        .estado(EState.PENDIENTE)
        .build();

    List<ServicioSolicitadoDetalle> detalles = servicios.stream()
        .map(servicio -> ServicioSolicitadoDetalle.builder()
            .servicio(servicio)
            .servicioSolicitado(servicioSolicitado)
            .build())
        .toList();

    servicioSolicitado.setDetalles(detalles);

    servicioSolicitadoRepo.save(servicioSolicitado); // Guarda turno y detalles

    // Crear pago y asociar al turno
    PaymentCard pagoTarjeta = PaymentCard.builder()
        .cardholderName(dto.getCardholderName())
        .cardNumber(dto.getCardNumber())
        .expirationMonth(dto.getExpirationMonth())
        .expirationYear(dto.getExpirationYear())
        .securityCode(dto.getSecurityCode())
        .documentType(dto.getDocumentType())
        .documentNumber(dto.getDocumentNumber())
        .payerEmail(dto.getPayerEmail())
        .amount(dto.getAmount())
        .servicioSolicitado(servicioSolicitado) // referencia al turno
        .build();

    pagoTarjetaRepo.save(pagoTarjeta);

    String emailDestino = solicitante.getUsername();
    String subject = "Confirmación de Turno con Pago";
    String body = "Hola " + solicitante.getNombre() + ",\n\n" +
          "Tu turno ha sido solicitado con éxito.\n" +
          "Profesional: " + profesional.getUsername() + "\n" +
          "Fecha: " + dto.getFecha() + "\n" +
          "Hora: " + dto.getHora() + "\n" +
          "Servicios: " + servicios.stream().map(Servicio::getNombre).collect(Collectors.joining(", ")) + "\n\n" +
          "Monto pagado: $" + dto.getAmount() + "\n\n" +
            "RECUERDE QUE SU TURNO YA SE ENCUENTRA ABONADO, NO DEBERA ABONAR NINGUN MONTO AL MOMENTO DE ASISTIR. \n\n" +          
          "Gracias por usar nuestra plataforma.";

    emailService.sendTurnoEmail(emailDestino, subject, body);

    return ResponseEntity.ok("Turno y pago registrados correctamente");
}

    @DeleteMapping("/deleteTurn/{turnID}")
    public String deleteTurn(@PathVariable("turnID") Long turnID){
        servicioSolicitadoRepo.deleteById(turnID);
        return "Se ha borrado el turno con id: " + turnID;
    }

    @GetMapping("/listTurnFinalizado")
    public List<ServicioSolicitado> getFinalizados() {
        return servicioSolicitadoRepo.findByEstado(EState.FINALIZADO);
    }

    @GetMapping("/listTurnEnProceso")
    public List<ServicioSolicitado> getEnProceso() {
        return servicioSolicitadoRepo.findByEstado(EState.EN_PROCESO);
    }

    @GetMapping("/listTurnPendiente")
    public List<ServicioSolicitado> getPendiente() {
        return servicioSolicitadoRepo.findByEstado(EState.PENDIENTE);
    }

    @GetMapping("/listTurnCancelado")
    public List<ServicioSolicitado> getCancelado() {
        return servicioSolicitadoRepo.findByEstado(EState.CANCELADO);
    }

    @GetMapping("/listTurnPendientePago")
    public List<ServicioSolicitado> getPendientePago() {
        return servicioSolicitadoRepo.findByEstado(EState.PENDIENTE_PAGO);
    }

    @GetMapping("/turn/{turnID}")
    public Optional<ServicioSolicitado> getByID(@PathVariable("turnID") Long turnID){
        return servicioSolicitadoRepo.findById(turnID);
    }

    @GetMapping("/listTurnFinalizado/{profesionalId}")
public List<ServicioSolicitado> getFinalizados(@PathVariable Long profesionalId) {
    return servicioSolicitadoRepo.findByEstadoAndProfesionalId(EState.FINALIZADO, profesionalId);
}

@GetMapping("/listTurnEnProceso/{profesionalId}")
public List<ServicioSolicitado> getEnProceso(@PathVariable Long profesionalId) {
    return servicioSolicitadoRepo.findByEstadoAndProfesionalId(EState.EN_PROCESO, profesionalId);
}

@GetMapping("/listTurnPendiente/{profesionalId}")
public List<ServicioSolicitado> getPendiente(@PathVariable Long profesionalId) {
    return servicioSolicitadoRepo.findByEstadoAndProfesionalId(EState.PENDIENTE, profesionalId);
}

@GetMapping("/listTurnCancelado/{profesionalId}")
public List<ServicioSolicitado> getCancelado(@PathVariable Long profesionalId) {
    return servicioSolicitadoRepo.findByEstadoAndProfesionalId(EState.CANCELADO, profesionalId);
}

@GetMapping("/listTurnPendientePago/{profesionalId}")
public List<ServicioSolicitado> getPendientePago(@PathVariable Long profesionalId) {
    return servicioSolicitadoRepo.findByEstadoAndProfesionalId(EState.PENDIENTE_PAGO, profesionalId);
}

}
