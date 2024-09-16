package com.fulfilment.application.monolith.fulfilment.domain.models;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class FulfilmentRepository implements PanacheRepository<Fulfilment> {
}
