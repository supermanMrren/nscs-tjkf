package com.boco.nscs.config;

import com.boco.nscs.soap.INscsPort;
import com.boco.nscs.soap.impl.NscsPortImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;
    @Autowired
    private INscsPort nscsPort;
    @Bean
    public Endpoint endpoint() {
        //单个 Endpoint
        //EndpointImpl endpoint = new EndpointImpl(bus, new NscsPortImpl());
        EndpointImpl endpoint = new EndpointImpl(bus, nscsPort);
        endpoint.publish("/Nscs/tjkf");
        return endpoint;
    }
}
