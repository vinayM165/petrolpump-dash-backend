package com.fuel.repository;

import com.fuel.model.DailyReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyReadingRepository extends JpaRepository<DailyReading, Long> {

    List<DailyReading> findByReadingDate(LocalDate readingDate);

    List<DailyReading> findByShift(String shift);

    List<DailyReading> findByReadingDateAndShift(LocalDate readingDate, String shift);
}
