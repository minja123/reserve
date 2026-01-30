package com.example.reserve.service;

import com.example.reserve.dto.ReservationRequest;
import com.example.reserve.entity.Reservation;
import com.example.reserve.exception.ReservationException;
import com.example.reserve.repository.PaymentRepository;
import com.example.reserve.repository.ReservationRepository;
import com.example.reserve.repository.TimeSlotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceUnitTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private PaymentRepository paymentRepository;


    @Test
    @DisplayName("예약 가능 시간이 없으면 예외가 발생한다.")
    void create_fail_no_timeslot() {
        // 가상의 id(anyLong)가 들어오면 무조건 0을 리턴하라고 "가정" 설정
        given(timeSlotRepository.holdIfAvailable(anyLong())).willReturn(0);

        ReservationRequest request = ReservationRequest.create("test@test.com", 1L);

        // 이제 실행 시 service 내부에서 위 메서드가 호출되면 0이 반환됨

        //when & then: 예외가 발생하는지 검증
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("이미 예약 중이거나");

        // then: reservationRepository save 메서드가 절대(never) 호출되지 않았는지 확인
        verify(reservationRepository, never()).save(any());
        // then: paymentRepository save 메서드가 절대(never) 호출되지 않았는지 확인
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("정상적인 경우 예약 번호를 반환한다.")
    void create_success() {
        //given
        Long fakeId = 100L;
        ReservationRequest request = ReservationRequest.create("admin@naver.com", 1L);

        // 가짜 객체 생성 및 ID 주입
        // Reflection으로 private 필드인 "id"에 fakeId(100L)를 강제로 주입
        Reservation reservation = Reservation.create(request.getUserEmail(), request.getTimeSlotId());
        ReflectionTestUtils.setField(reservation, "id", fakeId);

        // Mocking: 행위 정의
        // "리포지토리에 아무 Reservation이나 저장(save)하라고 시키면, 무조건 위에서 만든 100L짜리 반환
        given(timeSlotRepository.holdIfAvailable(request.getTimeSlotId())).willReturn(1);
        given(reservationRepository.save(any())).willReturn(reservation);

        // when
        // 서비스 로직 내부에서 reservationRepository.save()를 호출하면,
        // 실제 로직과 상관없이 무조건 ID가 100L인 객체가 튀어나옵니다.
        Long resultId = reservationService.create(request);

        System.err.println(resultId);
        System.err.println(fakeId);
        System.out.println("Mocking check: " + reservationRepository.save(new Reservation()));

        // 5.검증
        // 서비스가 'save'로부터 받은 객체의 ID를 그대로 리턴했다면, resultId는 100L이 됩니다.
        assertThat(resultId).isEqualTo(fakeId);

        // then: paymentRepository save 메서드가 절대(never) 호출되지 않았는지 확인
        verify(paymentRepository, times(1)).save(any());

    }
}
