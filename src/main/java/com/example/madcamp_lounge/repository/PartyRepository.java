package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
