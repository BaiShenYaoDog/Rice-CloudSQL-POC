package cn.chengzhiya.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    private Tomcat webServer;

    public void startServer(int port) {
        new Thread(() -> {
            try {
                webServer = new Tomcat();

                Connector connector = new Connector("HTTP/1.1");
                connector.setPort(port);
                connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
                connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");

                webServer.getService().addConnector(connector);

                Context context = webServer.addContext("", null);
                Tomcat.addServlet(context, "httpServer", new HttpServer());
                context.addServletMappingDecoded("/", "httpServer");

                webServer.start();
                webServer.getServer().await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
