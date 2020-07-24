package com.jfilter.soap.config;

import com.jfilter.components.FilterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;

import java.util.List;

@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ConfigurerAdapter extends WsConfigurerAdapter {

    private SoapEndpointInterceptor soapEndpointInterceptor;
    private FilterConfiguration filterConfiguration;

    @Autowired(required = false)
    public void setFilterConfiguration(FilterConfiguration filterConfiguration) {
        this.filterConfiguration = filterConfiguration;
    }

    @Autowired
    public ConfigurerAdapter(SoapEndpointInterceptor soapEndpointInterceptor) {
        this.soapEndpointInterceptor = soapEndpointInterceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        if (filterConfiguration != null && filterConfiguration.isEnabled())
            interceptors.add(soapEndpointInterceptor);
    }
}