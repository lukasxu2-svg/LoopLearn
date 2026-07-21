package com.example.saastest.modules.plan.controller;

import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.service.PlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {
    private final PlanService service;

    public PlanController(PlanService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Plan> getPlans() {
        return service.getPlans();
    }

   
}
