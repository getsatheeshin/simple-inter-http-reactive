package com.fisglobal.d1pf.poc.http.reactive.resource;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.fisglobal.d1pf.poc.http.reactive.model.EchoMO;
import com.fisglobal.d1pf.poc.http.reactive.svc.SimpleService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.concurrent.ThreadLocalRandom;

@Path("/innerhttp/reactive")
public class SimpleInterReactiveHttpResource {

    private static final Logger LOG = Logger.getLogger(SimpleInterReactiveHttpResource.class);

    @Inject
    SimpleService service;

    @Inject
    Vertx vertx;

    @Inject
    @RestClient
    RouteResourceClient routeResourceClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("echo/{name}")
    public Uni<EchoMO> echo(@PathParam("name") String name) {
        LOG.info("Echo "+name);
        final String local = name;
        return Uni.createFrom().emitter(em ->{
            em.complete(service.echo(local));
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("chain/{name}")
    public Uni<EchoMO> chain(@PathParam("name") String name) {
        LOG.info("Chain "+name);
        final String local = name;
        return Uni.createFrom()
                .deferred(() -> {
                    return Uni.createFrom().item(service.echo(local));
                })
                .onItem()
                .transform(echoMO -> {
                    EchoMO mo = routeResourceClient.route(echoMO);
                    return mo;
                });
//        return routeResourceClient.route(mo);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("route/msg")
    public Uni<EchoMO> echoroute(EchoMO mo) {
        LOG.info("Echoroute "+mo.getMsg());
        final EchoMO local = mo;
        return Uni.createFrom().emitter(em ->{
            em.complete(service.routeEcho(local));
        });
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("ping")
    public Uni<String> ping() {
        return Uni.createFrom().item("d1platform-innerhttp-reactive-ping::"+ ThreadLocalRandom.current().nextLong(0,Long.MAX_VALUE));
    }
}