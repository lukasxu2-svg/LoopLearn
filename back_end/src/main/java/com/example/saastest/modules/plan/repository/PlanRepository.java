package com.example.saastest.modules.plan.repository;

import com.example.saastest.modules.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
