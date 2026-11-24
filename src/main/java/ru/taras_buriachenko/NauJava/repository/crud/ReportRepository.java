package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Report;

// exported = false чтобы Spring Data REST не занял /reports
// иначе ReportController нужен будет другой путь
@RepositoryRestResource(exported = false)
@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
}