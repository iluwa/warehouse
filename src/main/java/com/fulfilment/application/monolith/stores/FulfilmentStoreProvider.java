package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.fulfilment.domain.usecases.StoreProvider;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class FulfilmentStoreProvider implements StoreProvider {
  @Override
  public Optional<FulfilmentStore> getStoreByName(String name) {
    return Store.<Store>find("name", name).firstResultOptional()
            .map(store -> new FulfilmentStore(store.name));
  }
}
