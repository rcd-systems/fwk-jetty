package systems.rcd.fwk.jetty.impl.data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RcdJettyHandler {

    public void handle(String target, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
