package com.jfilter.soap;

import com.jfilter.components.DynamicFilterProvider;
import com.jfilter.components.FilterProvider;
import com.jfilter.filter.FilterFields;
import com.jfilter.request.RequestSession;
import com.jfilter.soap.SoapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static com.jfilter.filter.FilterFields.EMPTY_FIELDS;

@Component
public class SoapFilter {

    private FilterProvider filterProvider;
    private DynamicFilterProvider dynamicFilterProvider;
    private HttpServletRequest httpServletRequest;

    @Autowired(required = false)
    public void setFilterProvider(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }

    @Autowired(required = false)
    public void setDynamicFilterProvider(DynamicFilterProvider dynamicFilterProvider) {
        this.dynamicFilterProvider = dynamicFilterProvider;
    }

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        MethodEndpoint methodEndpoint = (MethodEndpoint) endpoint;
        Method method = methodEndpoint.getMethod();
        MethodParameter methodParameter = new MethodParameter(method, 0);
        RequestSession requestSession = new RequestSession(httpServletRequest);
        final Node object = SoapUtils.extractObject(messageContext);

        FilterFields filterFields = EMPTY_FIELDS.get();

        //Retrieve filterable fields from static filters
        filterProvider.getOptionalFilter(methodParameter)
                .ifPresent(filter -> filterFields.appendToMap(filter.getFields(object, requestSession)));

        //Retrieve filterable fields from dynamic filters
        filterFields.appendToMap(dynamicFilterProvider.getFields(methodParameter, requestSession));
        filter(object, filterFields);

        return true;
    }

    public Node filter(Node objectNode, FilterFields filterFields) {
        NodeList fields = objectNode.getChildNodes();
        String mainNodeName = objectNode.getLocalName();
        for (int n = 0; n < fields.getLength(); n++) {
            Node field = fields.item(n);
            String fieldName = field.getLocalName();

            if (field.getChildNodes().getLength() > 0)
                filter(field, filterFields);

            filterFields.getFieldsMap().forEach((k, v) -> {

                //Try to filter fields in specified classes
                if (!void.class.equals(k)) {
                    //Get class name from XmlType annotation
                    String className = SoapUtils.getClassNodeName(k);
                    if (mainNodeName.equals(className)) {
                        v.stream().filter(f -> f.equals(fieldName))
                                .findFirst()
                                .ifPresent(f -> objectNode.removeChild(field));
                    }
                } else {
                    //Try to filter fields in all classes
                    v.stream().filter(f -> f.equals(fieldName))
                            .findFirst()
                            .ifPresent(f -> objectNode.removeChild(field));
                }

            });
        }

        return objectNode;
    }
}
