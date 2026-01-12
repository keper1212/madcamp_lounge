package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.Party;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyRepository extends JpaRepository<Party, Long> {
    @Modifying
    @Query("""
        update Party p
        set p.status = 'CLOSED'
        where p.status <> 'CLOSED'
          and p.appointmentTime is not null
          and p.appointmentTime < :now
        """)
    int closeExpired(@Param("now") LocalDateTime now);
}
