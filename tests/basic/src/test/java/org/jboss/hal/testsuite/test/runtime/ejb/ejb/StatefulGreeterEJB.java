package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.Stateful;

@Stateful
public class StatefulGreeterEJB implements RemoteEJBInterface {

    @Override
    public String invoke() {
        return "Hello from stateful greeter";
    }
}
