package com.jfilter.soap;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Node;

import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.dom.DOMSource;

public class SoapUtils {

    public static Node extractObject(MessageContext messageContext) {
        SaajSoapMessage msg = (SaajSoapMessage) messageContext.getResponse();
        SoapEnvelope soapEnvelope = msg.getEnvelope();
        SoapBody soapBody = soapEnvelope.getBody();
        DOMSource result = (DOMSource) soapBody.getPayloadSource();
        return result.getNode();
    }

    public static String getClassNodeName(Class<?> clazz) {
        XmlType xmlType = clazz.getDeclaredAnnotation(XmlType.class);
        return xmlType != null ? xmlType.name() : null;
    }
}
