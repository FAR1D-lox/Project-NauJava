package ru.taras_buriachenko.NauJava.repository.crud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Blacklist;
import ru.taras_buriachenko.NauJava.entity.BlacklistId;

@Repository
public interface BlacklistRepository extends CrudRepository<Blacklist, BlacklistId> { }
