package org.example.sysedu.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class GenericMapper {
    private static ModelMapper modelMapper = new ModelMapper();

    public static <D, T> D toDTO(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static <D, T> T toEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public static <D, T> List<D> toListDTO(List<T> entityList, Class<D> dtoClass) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public static <D, T> List<T> toListEntity(List<D> dtoList, Class<T> entityClass) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, entityClass))
                .collect(Collectors.toList());
    }
}
