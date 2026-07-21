package com.example.saastest.modules.plan.service;

import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    private final PlanRepository repository;

    public PlanService(PlanRepository repository) {
        this.repository = repository;
    }

    public List<Plan> getPlans() {
        return repository.findAll();
    }
    
}
