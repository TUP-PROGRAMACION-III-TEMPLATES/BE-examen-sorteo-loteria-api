package ar.edu.utn.frc.tup.lciii.configs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringDocConfigTest {

    @LocalServerPort
    private int port;

    @Test
    void getDocumentation() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + this.port + "/v3/api-docs");
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.statusCode() >= 200 && response.statusCode() < 300);
        assertNotNull(response.body());

        Path specs = Paths.get("docs/specs");
        Files.createDirectories(specs);
        Files.writeString(specs.resolve("swagger.json"), response.body());
    }
}
