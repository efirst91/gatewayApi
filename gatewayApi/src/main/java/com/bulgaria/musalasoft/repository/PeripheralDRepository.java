package com.bulgaria.musalasoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bulgaria.musalasoft.model.PeripheralD;

@Repository
public interface PeripheralDRepository extends JpaRepository<PeripheralD, Integer> {

	List<PeripheralD> findByGatewayNotNull();

	List<PeripheralD> findByGatewayNull();
}
