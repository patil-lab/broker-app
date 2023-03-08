package com.prive.broker.dto.request;


import com.prive.broker.constants.OrderStatus;
import com.prive.broker.constants.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrokerCallbackRequest {

	private RequestStatus requestStatus;

	private String reqId;

	private OrderStatus orderStatus;

	private String orderId;

}
