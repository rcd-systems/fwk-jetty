package systems.rcd.fwk.jetty.impl;

import systems.rcd.fwk.jetty.impl.data.RcdJettyServer;

public class RcdJettyService {

    public static RcdJettyServer createServer(final int port) {
        return new RcdJettyServer(port);
    }
}
