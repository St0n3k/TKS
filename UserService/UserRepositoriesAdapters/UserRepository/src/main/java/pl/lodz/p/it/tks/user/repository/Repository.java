package pl.lodz.p.it.tks.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface Repository<T> {
    T add(T item);

    void remove(T item);

    Optional<T> getById(UUID id);

    Optional<T> update(T item);

    List<T> getAll();
}
