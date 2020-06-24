package com.example.demo;

import com.example.demo.entities.Engagement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngagementRepository extends CrudRepository<Engagement,Long> {
}
