package com.nyoongoon.fullstackjava.repository;

import com.nyoongoon.fullstackjava.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
