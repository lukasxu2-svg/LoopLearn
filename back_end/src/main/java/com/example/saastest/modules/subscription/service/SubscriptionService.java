package com.example.saastest.modules.subscription.service;

import com.example.saastest.modules.subscription.dto.request.CreateSubscriptionRequestBody;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    private final SubscriptionRepository repo;

    public SubscriptionService(SubscriptionRepository repo) {
        this.repo = repo;
    }

    public Subscription getCurrentSubscription(Long benutzerId) {
        return repo.findByBenutzer_Id(benutzerId).orElse(null);
    }

    public void deleteCurrentSubscription(Long subId) {
        repo.deleteById(subId);
    }


    public Subscription createSubscription(CreateSubscriptionRequestBody requestBody) {
        Subscription subscription = new Subscription(requestBody.periodEnd(), requestBody.periodStart(), requestBody.subStatus(), requestBody.cost(), requestBody.subType(), requestBody.benutzer());

        return repo.save(subscription);
    }
}
