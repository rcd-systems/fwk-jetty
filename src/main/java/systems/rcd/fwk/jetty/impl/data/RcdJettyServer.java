package systems.rcd.fwk.jetty.impl.data;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.resource.Resource;

public class RcdJettyServer {

    final Server server;

    final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

    final GzipHandler gzipHandler = new GzipHandler();

    public RcdJettyServer(final int port) {
        server = new Server(port);
        gzipHandler.setHandler(contextHandlerCollection);
        server.setHandler(gzipHandler);
    }

    public RcdJettyServer addResourceHandler(final String contextPath, final URL baseUri)
            throws MalformedURLException, URISyntaxException {

        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true); // TODO Remove
        resourceHandler.setWelcomeFiles(new String[] { "index.html" });
        resourceHandler.setBaseResource(Resource.newResource(baseUri));

        final ContextHandler context = new ContextHandler(contextPath);
        context.setHandler(resourceHandler);
        contextHandlerCollection.addHandler(context);
        return this;
    }

    public RcdJettyServer start() throws Exception {
        server.start();
        return this;
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

}
