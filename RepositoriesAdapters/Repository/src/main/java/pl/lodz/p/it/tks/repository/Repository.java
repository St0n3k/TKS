package pl.lodz.p.it.tks.repository;

import jakarta.annotation.sql.DataSourceDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataSourceDefinition(
        name = "java:global/jdbc/guesthouse",
        className = "org.postgresql.ds.PGSimpleDataSource",
        serverName = "db",
        portNumber = 5432,
        databaseName = "pas",
        user = "pas",
        password = "pas"
)
public interface Repository<T> {
    T add(T item);

    void remove(T item);

    Optional<T> getById(UUID id);

    Optional<T> update(T item);

    List<T> getAll();
}
