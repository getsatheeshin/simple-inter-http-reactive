package com.fisglobal.d1pf.poc.http.reactive.svc;

import com.fisglobal.d1pf.poc.http.reactive.model.EchoMO;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;

@Traced
@ApplicationScoped
public class SimpleService {

    public EchoMO echo(String name) {
        EchoMO mo = new EchoMO();
        mo.setMsg("d1platform-innerhttp-reactive-direct ==> " + name);
        return mo;
    }

    public EchoMO routeEcho(EchoMO mo) {
        mo.setRoutemsg("d1platform-innerhttp-reactive-routed ==> " + mo.getMsg());
        return mo;
    }

}
