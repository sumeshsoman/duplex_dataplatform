package com.duplex.demo.repo;

import com.duplex.demo.model.CSVHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CSVRepository extends CrudRepository<CSVHistory, Long> {}
