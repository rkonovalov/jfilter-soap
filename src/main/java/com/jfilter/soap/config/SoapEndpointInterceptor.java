package com.jfilter.soap.config;

import com.jfilter.soap.SoapFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class SoapEndpointInterceptor implements EndpointInterceptor {

    private SoapFilter soapFilter;

    @Autowired
    public void setJFilterSoapFilter(SoapFilter soapFilter) {
        this.soapFilter = soapFilter;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        soapFilter.handleResponse(messageContext, endpoint);
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {

    }


}
