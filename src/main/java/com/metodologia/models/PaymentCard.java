package com.metodologia.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "card_payment")
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardholderName;        // Nombre en la tarjeta
    private String cardNumber;            // Número de tarjeta (últimos 4, si se guarda)
    private String expirationMonth;       // Mes de vencimiento (MM)
    private String expirationYear;        // Año de vencimiento (YY o YYYY)
    private String securityCode;          // CVV
    private String documentType;          // DNI, Cédula, etc.
    private String documentNumber;
    private String payerEmail;
    private Double amount;                // Monto a cobrar

    @OneToOne
    @JoinColumn(name = "servicio_solicitado_id", nullable = false)
    private ServicioSolicitado servicioSolicitado;
}
