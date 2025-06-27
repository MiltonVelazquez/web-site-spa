package com.metodologia.controller.request;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCardDTO {
    // Datos turno
    private Long solicitanteId;
    private Long profesionalId;
    private List<Long> servicioIds;
    private String fecha;
    private String hora;
    private String estado; // por ejemplo PENDIENTE_PAGO

    // Datos tarjeta
    private String cardholderName;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String securityCode;
    private String documentType;
    private String documentNumber;
    private String payerEmail;
    private Double amount;
}
