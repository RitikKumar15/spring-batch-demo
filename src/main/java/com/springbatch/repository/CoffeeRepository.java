package com.springbatch.repository;

import com.springbatch.entity.CoffeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CoffeeRepository extends JpaRepository<CoffeeEntity, Long> {
}
