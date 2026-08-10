package com.example.saastest.modules.plan.repository;

import com.example.saastest.modules.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByPlanId(String planId);
}
