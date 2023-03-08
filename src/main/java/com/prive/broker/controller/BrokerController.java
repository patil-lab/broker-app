package com.prive.broker.controller;

import com.prive.broker.dto.request.BrokerCallbackRequest;
import com.prive.broker.dto.request.BrokerRequestDto;
import com.prive.broker.dto.response.BrokerServiceResult;
import com.prive.broker.service.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"${api-version}"},produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BrokerController {

	private BrokerService brokerService;

	@Autowired
	BrokerController(BrokerService brokerService){
		this.brokerService=brokerService;
	}

	@PostMapping("/create-order")
	public ResponseEntity<BrokerServiceResult<String>> createOrder(@RequestBody @Valid BrokerRequestDto brokerRequestDto){

		brokerService.createOrder(brokerRequestDto);
		return ResponseEntity.ok(BrokerServiceResult.succeed(HttpStatus.OK.toString()));
	}

	@PutMapping("/create-order")
	public ResponseEntity<BrokerServiceResult<String>> updateOrder(BrokerCallbackRequest brokerCallbackRequest){
		brokerService.updateStatusToOrderApp(brokerCallbackRequest);
		return ResponseEntity.ok(BrokerServiceResult.succeed(HttpStatus.OK.toString()));
	}
}
