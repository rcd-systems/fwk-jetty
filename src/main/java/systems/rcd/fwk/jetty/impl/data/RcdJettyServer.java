package systems.rcd.fwk.jetty.impl.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.resource.Resource;

import systems.rcd.fwk.core.log.RcdLogService;

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

        addHandler(contextPath, resourceHandler);
        return this;
    }

    public RcdJettyServer addHandler(final String contextPath, final RcdJettyHandler handler) {
        addHandler(contextPath, new AbstractHandler() {
            @Override
            public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws IOException, ServletException {
                try {
                    handler.handle(target, request, response);
                } catch (final Exception e) {
                    RcdLogService.error("Http request handle error for path '", e);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } finally {
                    baseRequest.setHandled(true);
                }
            }
        });

        return this;
    }

    private void addHandler(final String contextPath, final Handler handler) {
        final ContextHandler context = new ContextHandler(contextPath);
        context.setHandler(handler);
        contextHandlerCollection.addHandler(context);
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

    public URI getUri() {
        return server.getURI();
    }

}
