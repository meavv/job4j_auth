package ru.job4j.controller;

import netscape.javascript.JSObject;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Employee;
import ru.job4j.domain.Person;
import ru.job4j.repository.EmployeeRep;
import ru.job4j.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRep employees;
    private final PersonRepository persons;

    public EmployeeController(final  EmployeeRep employees, final PersonRepository persons) {
        this.employees = employees;
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        this.employees.findAll().forEach(list::add);
        return list;
    }

    @PostMapping("/")
    public ResponseEntity<Employee> create(@RequestBody String s) throws JSONException {
        Employee employee = new Employee();
        JSONObject jsonObject = new JSONObject(s);
        employee = employee.of(
                jsonObject.get("name").toString(),
                jsonObject.get("surname").toString(),
                Long.parseLong(jsonObject.get("inn").toString()),
                jsonObject.get("dateOfEmployment").toString()
        );

        List<Person> personList = new ArrayList<>();
        var array = jsonObject.getJSONArray("persons");
        for (int i = 0; i < array.length(); i++) {
            personList.add(persons.findById(array.getInt(i)).orElseThrow());
        }

        employee.setPersonList(personList);
        return new ResponseEntity<>(employees.save(employee), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Employee employee) {
        this.employees.save(employee);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Employee employee = new Employee();
        employee.setId(id);
        this.employees.delete(employee);
        return ResponseEntity.ok().build();
    }
}
