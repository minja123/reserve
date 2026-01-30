package com.example.reserve.repository;

import com.example.reserve.entity.TimeSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByResourceId(Long id);

    //비관적 락 방식
    //조회 후 부터 LOCK 반환 전까지 사용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t
            from TimeSlot t
            where t.id = :id
            """)
    Optional<TimeSlot> findByIdWithLock(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            update TimeSlot t
            set t.status = 'HOLD'
            where t.id = :id
            and t.status = 'AVAILABLE'
            """)
    int holdIfAvailable(@Param("id") Long id);

    @Modifying(clearAutomatically = true) //영속성 컨텍스트 초기화
    @Query("""
            update TimeSlot t
            set status = 'RESERVED'
            where id = :id
              and status = 'HOLD'
            """)
    int reserve(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            update TimeSlot t set t.status = 'AVAILABLE' where t.id = :id and t.status = 'HOLD'
            """)
    int release(@Param("id") Long id);
}
