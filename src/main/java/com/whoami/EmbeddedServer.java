package com.whoami;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import java.io.IOException;
import java.net.URISyntaxException;
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
        
        try {
            Path tempWebapp = extractWebappToTemp();
            Context ctx = tomcat.addWebapp("", tempWebapp.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract webapp", e);
        }
        
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
    
    private static Path extractWebappToTemp() throws IOException {
        Path tempDir = Files.createTempDirectory("whoami-webapp");
        copyFromClasspath("webapp", tempDir);
        return tempDir;
    }
    
    private static void copyFromClasspath(String resourcePath, Path target) throws IOException {
        Path source = Paths.get("src/main/webapp");
        if (Files.exists(source)) {
            copyDirectory(source, target);
        } else {
            // For JAR deployment, extract from classpath
            extractFromJar(resourcePath, target);
        }
    }
    
    private static void extractFromJar(String resourcePath, Path target) throws IOException {
        ClassLoader cl = EmbeddedServer.class.getClassLoader();
        // Get all resources under the path
        // This is simplified; in a real implementation, use a library or recursive listing
        java.net.URL resource = cl.getResource(resourcePath);
        if (resource != null) {
            if (resource.getProtocol().equals("jar")) {
                // Handle JAR resources - this requires more complex code
                // For now, throw exception
                throw new IOException("JAR resource extraction not implemented");
            } else {
                // File system
                try {
                    Path sourcePath = Paths.get(resource.toURI());
                    if (Files.isDirectory(sourcePath)) {
                        copyDirectory(sourcePath, target);
                    }
                } catch (URISyntaxException e) {
                    throw new IOException("Invalid URI for resource: " + resource, e);
                }
            }
        } else {
            throw new IOException("Resource not found: " + resourcePath);
        }
    }
    
    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walk(source).forEach(path -> {
            try {
                Path relative = source.relativize(path);
                Path dest = target.resolve(relative);
                if (Files.isDirectory(path)) {
                    Files.createDirectories(dest);
                } else {
                    Files.copy(path, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
