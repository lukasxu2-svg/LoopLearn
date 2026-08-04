package com.example.saastest.modules.plan.controller;

import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService service;

    public PlanController(PlanService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Plan> getPlans() {
        return service.getPlans();
    }

    @GetMapping("/{id}")
    public Plan getPlanById(@PathVariable Long id) {
        return service.getPlanById(id);
    }


}
