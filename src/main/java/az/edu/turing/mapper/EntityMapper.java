package az.edu.turing.mapper;

public interface EntityMapper<T, E> {
    T toEntity(E e);

    E toDto(T t);
}
