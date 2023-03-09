package com.prive.broker.dto.request;

import com.prive.broker.entity.BrokerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrokerRequestDto {


	@NotEmpty
	@NotNull
	private  String orderId;

	@NotEmpty
	@NotNull
	private String requestId;

	private String callbackUrl;

	public BrokerEntity toBrokerEntity(){
		return BrokerEntity.builder().orderId(this.orderId).requestId(this.requestId).callbackUrl(this.callbackUrl).build();
	}
}
