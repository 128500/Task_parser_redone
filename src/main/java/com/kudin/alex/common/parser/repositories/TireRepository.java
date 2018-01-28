package com.kudin.alex.common.parser.repositories;

import com.kudin.alex.common.parser.entities.Tire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by KUDIN ALEKSANDR on 25.01.2018.
 */

public interface TireRepository extends CrudRepository<Tire, Long> {
}
