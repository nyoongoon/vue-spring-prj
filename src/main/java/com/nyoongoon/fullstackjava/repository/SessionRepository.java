package com.nyoongoon.fullstackjava.repository;

import com.nyoongoon.fullstackjava.domain.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {

}
