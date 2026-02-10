package com.example.reserve.exception;

import com.example.reserve.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationException.class)
    public ApiResponse<Void> handleReservationException(ReservationException e) {
        log.warn("비지니스 로직 예외 발생: {}", e.getMessage());
        return ApiResponse.fail(400, e.getMessage()); // 400 Bad Request
    }

    //DB 제약 조건 위반 (중복 예약 등) 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<Void> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("데이터 제약 조건 위반: ", e); // 에러 로그와 함께 스택 트레이스 출력
        return ApiResponse.fail(409, "이미 처리 중이거나 중복된 요청입니다.");
    }

    //그 외 예상치 못한 서버 내부 에러
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleAllException(Exception e) {
        log.error("서버 내부 심각한 오류 발생: ", e); // 최상위 예외는 반드시 상세 로그 필요
        return ApiResponse.fail(500, "서부 내부 오류가 발생했습니다.");
    }
}
