package com.example.reserve.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long reservationId;
    private String decision;

}
