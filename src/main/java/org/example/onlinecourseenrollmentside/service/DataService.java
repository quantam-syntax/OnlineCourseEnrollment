package org.example.onlinecourseenrollmentside.service;

import java.util.List;
import java.util.Optional;

public interface DataService<T> {

    boolean add(T item);

    boolean delete(int id);

    List<T> getAll();

    Optional<T> findById(int id);
}
