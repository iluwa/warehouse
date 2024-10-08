# Notes

## What's done

- Implement `LocationGateway.resolveByIdentifier`
- `LegacyStoreManagerGateway` moved outside of transaction scope to guarantee that it's called  
  with committed data only
- Implement Warehouse Creation, Replacement and Archive logic with tests
- Fulfilment
```txt
Add a possibility to attach products to a warehouse. 
Add `Fulfilment` entity which represents an association for a specific product from a warehouse to a store. 
```

## Overall thoughts and assumptions

- I never worked with Quarkus and its ecosystem before, 
  so I tried to use the same methods/annotations that are used in this test task template
- Since all the entities are placed in separate packages and there is no FKs between tables,
  I made an assumption that they should be independent.
  This structure adds a possibility to split such domains into separate services, in the meantime
  it adds a lot of complexity for keeping consistency in the database. 
  If they were entities in one package bound to each other via FKs, then 
  it'd easier perform validations like if an entity exists by a key.
- It's not clear whether it's necessary to implement API for the extra task about fulfilment.
  So I implemented only core part in the domain. To create an API I'd make an open-api spec file with CRUD endpoints,
  and do the same thing as in `WarehouseResourceImpl`.
- According to the API model, there is impossible to know whether a warehouse is archived. Looking at the
  existing test in the `WarehouseEndpointIT` I assumed that `get-all` method should return only active warehouses.
  One of the improvements is to make the API more flexible by adding `archivedAt` in the API model and adding it
  inside `get-all` endpoint as an input parameter.
- Most of the fields in the classes are public. In this case the class 
  doesn't control any modification. Was it done for simplicity?

## Further improvements
- Improve the architecture of the `fulfilment` package to the `warehouse` style - separate domain and infrastructure, 
  remove dependency on the warehouse usecase and use a port/adapter instead
- Implement a proper way to clean db between tests or run a new db instance for every test
- Rewrite tests so they only use data generated by them
- Rewrite tests so they don't depend on another packages but use mocks
- If the idea is to keep packages separated, then would be nice to have something similar  
  to [Spring Modulith](https://spring.io/projects/spring-modulith) but in Quarkus ecosystem 
- Add clockProvider class to use it inside every `LocalDateTime.now(clockPorivder.gerClock())`. 
  In that case it's possible to manipulate time in tests and test any date-time fields and logic.
- Add an error object in the API spec and implement it in the code. That's way the error text will be
  shown in the response
- Add more tests for the warehouse resource to cover all the exception-mappers
- The `create` endpoint returns 200 instead of 201 as it stated in the open-api spec. 
  This should be fixed on a generator level
