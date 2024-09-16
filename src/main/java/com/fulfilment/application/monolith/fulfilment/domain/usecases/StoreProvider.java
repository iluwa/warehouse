package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import java.util.Optional;

public interface StoreProvider {
  Optional<FulfilmentStore> getStoreByName(String name);

  record FulfilmentStore(String name) {
  }
}
