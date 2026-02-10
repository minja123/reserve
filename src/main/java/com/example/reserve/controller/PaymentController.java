package com.example.reserve.controller;

import com.example.reserve.dto.ApiResponse;
import com.example.reserve.dto.PaymentRequest;
import com.example.reserve.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/process")
    public ApiResponse<?> processPayment(@RequestBody PaymentRequest request) {
        paymentService.processPayment(request);
        return ApiResponse.success(null);
    }
}
