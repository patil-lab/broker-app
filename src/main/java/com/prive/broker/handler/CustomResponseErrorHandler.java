package com.prive.broker.handler;

import com.prive.broker.exception.BrokerServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {
	private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();


	//private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return errorHandler.hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) {

		String theString = responseBodyToStr(response);
		theString = StringUtils.substring(theString, 1, theString.length() - 1);
		//SmsEdgeErrorResponse smsedgeFailResponse = gson.fromJson(theString, SmsEdgeErrorResponse.class);
		throw new BrokerServiceException("","",1);
	}

	private String responseBodyToStr(ClientHttpResponse response) {
		return new String(getResponseBody(response), StandardCharsets.UTF_8);
	}

	private byte[] getResponseBody(ClientHttpResponse response) {
		try {
			return FileCopyUtils.copyToByteArray(response.getBody());
		} catch (IOException ioException) {

			return new byte[0];
		}
	}
}
