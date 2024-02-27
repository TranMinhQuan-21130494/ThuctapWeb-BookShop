package com.example.bookshopwebapplication.service.transferObject;

public interface ITransfer<D, E> {
    D toDto(E entity);

    E toEntity(D dto);
}
