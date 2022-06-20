package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Employee;

public interface EmployeeRep extends CrudRepository<Employee, Integer> {
}
