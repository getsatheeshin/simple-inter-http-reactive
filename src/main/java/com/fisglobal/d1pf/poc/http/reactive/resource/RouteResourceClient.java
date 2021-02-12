package com.fisglobal.d1pf.poc.http.reactive.resource;

import com.fisglobal.d1pf.poc.http.reactive.model.EchoMO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/innerhttp/reactive")
@RegisterRestClient
public interface RouteResourceClient {
    @POST
    @Path("/route/msg")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    EchoMO route(EchoMO mo);
}
