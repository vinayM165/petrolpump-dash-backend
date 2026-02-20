package com.fuel.repository;

import com.fuel.model.FuelPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelPurchaseRepository extends JpaRepository<FuelPurchase, Long> {
}
