import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {
//        String json = "{\"username\":\"sys_admin\",\"password\":\"uni_admin\"}";
//        HttpClient httpClient = HttpClient.newBuilder()
//                .build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:3000/admin/register-student"))
//                .header("content-type", "application/json")
//                .header("x-api-key", "548fa97b5accc12f63b0bd6dbecd5b6ebdec9db4")
//                .POST(HttpRequest.BodyPublishers.ofString(json))
//                .build();
//        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println("Response status code: " + response.statusCode());
//        System.out.println("Response headers: " + response.headers());
//        System.out.println("Response body: " + response.body());

//        String json = "{\"username\":\"sys_admin\",\"password\":\"uni_admin\"}";
//        HttpClient httpClient = HttpClient.newBuilder()
//                .build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:3000/login"))
//                .header("content-type", "application/json")
//                .header("x-api-key", "96e780d79ee870175d07b4c75bb1d74b01b4b3a8")
//                .POST(HttpRequest.BodyPublishers.ofString(json))
//                .build();
//        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println("Response status code: " + response.statusCode());
//        System.out.println("Response headers: " + response.headers());
//        System.out.println("Response body: " + response.body());
    }
}
