package com.java.store.mapper;

public interface BaseMapper<D,E> {
    E DtoToEntity(D d);
    D EntityToDto(E e);
}
