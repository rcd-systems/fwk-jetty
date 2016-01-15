package systems.rcd.fwk.jetty.impl;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;

public class RcdJettyService {

    Server server;

    ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

    public RcdJettyService addResourceHandler(final String contextPath, final URL baseUri)
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

    public RcdJettyService start() throws Exception {
        server = new Server(8080);
        final GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setHandler(contextHandlerCollection);
        server.setHandler(gzipHandler);
        server.start();
        return this;
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static void main(final String[] args) throws Exception {
        Log.getLogger(ResourceHandler.class)
        .setDebugEnabled(true);
        new RcdJettyService().addResourceHandler("/res", RcdJettyService.class.getClassLoader()
                .getResource("/test"))
                .start()
                .join();
    }
}
