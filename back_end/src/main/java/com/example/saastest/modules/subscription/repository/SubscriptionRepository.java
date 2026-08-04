package com.example.saastest.modules.subscription.repository;

import com.example.saastest.modules.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByBenutzer_Id(Long id);
}
