package com.sflpro.cafe.repository;

import com.sflpro.cafe.entity.Table;
import com.sflpro.cafe.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableRepository extends CrudRepository<Table, Long>
{

	List<Table> findByWaiter(User waiter);
}
