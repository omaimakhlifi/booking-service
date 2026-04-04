package com.hestia.booking_service.repository;

import com.hestia.booking_service.core.model.UserSys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSysRepository extends JpaRepository<UserSys,Long> {
}
