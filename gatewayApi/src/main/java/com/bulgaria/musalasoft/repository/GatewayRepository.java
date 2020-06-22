package com.bulgaria.musalasoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bulgaria.musalasoft.model.Gateway;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, String> {

	public Gateway findByIpv4Address(String ipv4Address);
}
