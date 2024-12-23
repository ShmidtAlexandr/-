package org.example.kurs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Date;

//TODO: 1. createTicket - Нет реализации цифровой подписи. UUID - не цифровая подпись
//TODO: 2. Тикет содержит неверную информацию о лицензии (см. Задание 4)

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "server_date")
    private LocalDateTime serverDate;

    @Column(name = "ticket_lifetime")
    private int ticketLifetime;

    @Column(name = "activation_date")
    private Date activationDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "digital_signature")
    private String digitalSignature;

    // Метод для создания тикета
    public static Ticket createTicket(Long userId, boolean isBlocked, Date expirationDate) {
        Ticket ticket = new Ticket();
        ticket.setServerDate(LocalDateTime.now());
        ticket.setTicketLifetime(5);
        ticket.setActivationDate(new Date());
        ticket.setExpirationDate(expirationDate);
        ticket.setUserId(userId);

        ticket.setBlocked(isBlocked);
        ticket.setDigitalSignature(UUID.randomUUID().toString());

        return ticket;
    }


}
