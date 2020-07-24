package com.jfilter.soap.config;

import com.jfilter.soap.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

@Component
public class SoapEndpointInterceptor implements EndpointInterceptor {

    private ResponseHandler responseHandler;

    @Autowired
    public void setJFilterSoapFilter(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) {
        responseHandler.handleResponse(messageContext, endpoint);
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) {

    }


}
