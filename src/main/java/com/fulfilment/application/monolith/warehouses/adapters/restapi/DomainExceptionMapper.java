package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.usecases.DomainExceptions;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class DomainExceptionMapper {
  @Provider
  public static class DomainIsNotFoundExceptionMapper
          implements ExceptionMapper<DomainExceptions.DomainIsNotFoundException> {
    @Override
    public Response toResponse(DomainExceptions.DomainIsNotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @Provider
  public static class BusinessCodeAlreadyExistsExceptionMapper
          implements ExceptionMapper<DomainExceptions.BusinessCodeAlreadyExistsException> {
    @Override
    public Response toResponse(DomainExceptions.BusinessCodeAlreadyExistsException e) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  @Provider
  public static class LocationNotExistExceptionMapper
          implements ExceptionMapper<DomainExceptions.LocationNotExistException> {
    @Override
    public Response toResponse(DomainExceptions.LocationNotExistException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @Provider
  public static class NoMoreWarehousesAtLocationAllowedExceptionMapper
          implements ExceptionMapper<DomainExceptions.NoMoreWarehousesAtLocationAllowedException> {
    @Override
    public Response toResponse(DomainExceptions.NoMoreWarehousesAtLocationAllowedException e) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  @Provider
  public static class WarehouseAlreadyArchivedExceptionMapper
          implements ExceptionMapper<DomainExceptions.WarehouseAlreadyArchivedException> {
    @Override
    public Response toResponse(DomainExceptions.WarehouseAlreadyArchivedException e) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  @Provider
  public static class WarehouseStockIsDifferentExceptionMapper
          implements ExceptionMapper<DomainExceptions.WarehouseStockIsDifferentException> {
    @Override
    public Response toResponse(DomainExceptions.WarehouseStockIsDifferentException e) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  @Provider
  public static class WarehouseLocationIsDifferentExceptionMapper
          implements ExceptionMapper<DomainExceptions.WarehouseLocationIsDifferentException> {
    @Override
    public Response toResponse(DomainExceptions.WarehouseLocationIsDifferentException e) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }
}
