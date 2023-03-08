package com.prive.broker.service;

import com.prive.broker.config.RestTemplateConfig;
import com.prive.broker.constants.BrokerServiceError;
import com.prive.broker.constants.CallBackType;
import com.prive.broker.dto.request.BrokerCallbackRequest;
import com.prive.broker.dto.request.BrokerRequestDto;
import com.prive.broker.entity.BrokerEntity;
import com.prive.broker.exception.BrokerServiceException;
import com.prive.broker.repo.BrokerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class BrokerService {

	private final RestTemplate restTemplate;

	private BrokerRepo brokerRepo;

	@Value("${spring.application.name}")
	private String serviceName;


	@Autowired
	public BrokerService(@Qualifier(RestTemplateConfig.DEFAULT_REST_TEMPLATE) RestTemplate restTemplate, BrokerRepo brokerRepo){
		this.restTemplate=restTemplate;
		this.brokerRepo=brokerRepo;
	}

	public void createOrder(BrokerRequestDto brokerRequestDto){
		brokerRepo.save(brokerRequestDto.toBrokerEntity());
	}

	public void updateStatusToOrderApp(BrokerCallbackRequest brokerCallbackRequest){

		CallBackType callBackType=checkPairValidity(brokerCallbackRequest);

		BrokerCallbackRequest brokerRequest;
		BrokerEntity brokerEntity;

		if(CallBackType.ORDER.equals(callBackType)){
			Optional<BrokerEntity> entityOptional=brokerRepo.findByOrderId(brokerCallbackRequest.getOrderId());
			if(entityOptional.isEmpty()){
				throw new BrokerServiceException(serviceName, BrokerServiceError.NO_RECORD_FOUND.getMessage(),BrokerServiceError.NO_RECORD_FOUND.getErrorCode());
			}else {
				brokerEntity=entityOptional.get();
				brokerCallbackRequest.setOrderStatus(brokerCallbackRequest.getOrderStatus());
				brokerRepo.save(brokerEntity);
				brokerRequest=BrokerCallbackRequest.builder().orderId(brokerEntity.getOrderId()).orderStatus(brokerCallbackRequest.getOrderStatus()).build();
			}

		}else if(CallBackType.REQUEST.equals(callBackType)){

			Optional<BrokerEntity> entityOptional=brokerRepo.findByRequestId(brokerCallbackRequest.getReqId());
			if(entityOptional.isEmpty()){
				throw new BrokerServiceException(serviceName, BrokerServiceError.NO_RECORD_FOUND.getMessage(),BrokerServiceError.NO_RECORD_FOUND.getErrorCode());
			}else {
				brokerEntity=entityOptional.get();
				brokerCallbackRequest.setRequestStatus(brokerCallbackRequest.getRequestStatus());
				brokerRepo.save(brokerEntity);
				brokerRequest=BrokerCallbackRequest.builder().reqId(brokerEntity.getRequestId()).requestStatus(brokerCallbackRequest.getRequestStatus()).build();
			}

		}else {
			throw new BrokerServiceException(serviceName,BrokerServiceError.NO_RECORD_FOUND.getMessage(),BrokerServiceError.NO_RECORD_FOUND.getErrorCode());
		}


		String url=String.format("%s/broker-callback",brokerEntity.getCallbackUrl());
		UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(url);
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<BrokerCallbackRequest> httpEntity=new HttpEntity<>(brokerRequest,headers);


		restTemplate.exchange(builder.toUriString(), HttpMethod.POST,httpEntity,String.class);

	}


	private CallBackType checkPairValidity(BrokerCallbackRequest brokerCallbackRequest) {
		if(brokerCallbackRequest.getOrderId()!=null){
			if(brokerCallbackRequest.getOrderStatus()!=null){
				return CallBackType.ORDER;
			}
		} else if (brokerCallbackRequest.getReqId() != null) {

			if(brokerCallbackRequest.getRequestStatus()!=null)
				return CallBackType.REQUEST;
		}
		return CallBackType.NONE;
	}
}
