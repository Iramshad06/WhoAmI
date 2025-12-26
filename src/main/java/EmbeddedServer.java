import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EmbeddedServer {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        String port = System.getenv("PORT");
        int serverPort = port != null ? Integer.parseInt(port) : 8080;
        tomcat.setPort(serverPort);
        tomcat.getConnector();
        
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context ctx = tomcat.addWebapp("", webappDir);
        
        System.out.println("========================================");
        System.out.println("      WhoAmI Server Starting...        ");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Server will be available at:");
        System.out.println("  http://localhost:" + serverPort);
        System.out.println();
        System.out.println("Press Ctrl+C to stop the server");
        System.out.println("========================================");
        
        tomcat.start();
        tomcat.getServer().await();
    }
}
