package com.prive.broker.repo;

import com.prive.broker.entity.BrokerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BrokerRepo extends CrudRepository<BrokerEntity,Long> {

	Optional<BrokerEntity> findByOrderId(String orderId);

	Optional<BrokerEntity> findByRequestId(String requestid);
}
