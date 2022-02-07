import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UniversityAPI {
    private static final String apiUrl = "http://localhost:3000";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static String apiKey = "";
    public static boolean isLoggedIn() { return apiKey.length() > 0; }

    private static HttpResponse<String> post(String path, String body, String... headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + path))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body));
            if (headers.length > 1) builder.headers(headers);
            return httpClient.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpResponse<String> get(String path, String... headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + path))
                    .header("content-type", "application/json")
                    .GET();
            if (headers.length > 1) builder.headers(headers);
            return httpClient.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean login(String username, String password) {
        if (apiKey.length() > 0) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        HttpResponse<String> response = post("/login", body.toString(), "");
        if (response == null) return false;
        body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        if (msg.contains("Error:")) return false;
        apiKey = body.get("apiKey").getAsString();
        return true;
    }

    public static boolean logout() {
        if (apiKey.equals("")) return false;
        HttpResponse<String> response = post("/logout", "{}", "x-api-key", apiKey);
        if (response == null) return false;
        JsonObject body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        if (msg.contains("Error:")) return false;
        apiKey = "";
        return true;
    }

    public static boolean resetPassword(String newPass) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("password", newPass);
        HttpResponse<String> response = post("/reset-password", body.toString(), "x-api-key", apiKey);
        if (response == null) return false;
        body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        return !msg.contains("Error:");
    }

    public static JsonObject getProfile() {
        if (apiKey.equals("")) return null;
        HttpResponse<String> response = get("/profile", "x-api-key", apiKey);
        if (response == null) return null;
        JsonObject body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        if (msg.contains("Error:")) return null;
        return body;
    }

    public static JsonObject viewCourses() {
        if (apiKey.equals("")) return null;
        HttpResponse<String> response = get("/view-courses", "x-api-key", apiKey);
        if (response == null) return null;
        JsonObject body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        if (msg.contains("Error:")) return null;
        return body;
    }

    public static boolean registerForCourse(String courseTitle) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("title", courseTitle);
        HttpResponse<String> response = post("/add-course", body.toString(), "x-api-key", apiKey);
        if (response == null) return false;
        body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        return !msg.contains("Error:");
    }

    public static boolean dropCourse(String courseTitle) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("title", courseTitle);
        HttpResponse<String> response = post("/drop-course", body.toString(), "x-api-key", apiKey);
        if (response == null) return false;
        body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        return !msg.contains("Error:");
    }

    public static boolean registerNewStudent(
            String username, String password,
            String firstName, String lastName,
            String major, String address
    ) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        body.addProperty("firstName", firstName);
        body.addProperty("lastName", lastName);
        body.addProperty("major", major);
        body.addProperty("address", address);
        HttpResponse<String> response = post("/admin/register-student", body.toString(), "x-api-key", apiKey);
        if (response == null) return false;
        body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        return !msg.contains("Error:");
    }

    public static boolean registerNewCourse(
            String courseTitle,
            int credits,
            String[] prerequisites,
            String eligibleMajors
    ) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        JsonArray prerequisitesJson = new JsonArray();
        for (String prerequisite : prerequisites) prerequisitesJson.add(prerequisite);
        body.add("prerequisites", prerequisitesJson);
        body.addProperty("title", courseTitle);
        body.addProperty("credits", credits);
        body.addProperty("eligible", eligibleMajors);
        HttpResponse<String> response = post("/admin/register-course", body.toString(), "x-api-key", apiKey);
        if (response == null) return false;
        body = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = body.get("message").toString();
        System.out.println(msg);
        return !msg.contains("Error:");
    }

    public static boolean updateStudentPersonals(
            String username,
            String firstName, String lastName,
            String address
    ) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("firstName", firstName);
        body.addProperty("lastName", lastName);
        body.addProperty("address", address);
        return updateStudentInfo(body);
    }

    public static boolean changeStudentMajor(String username, String major) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("major", major);
        return updateStudentInfo(body);
    }

    public static boolean overrideIntoCourse(String username, String courseTitle) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("courseToEnroll", courseTitle);
        return updateStudentInfo(body);
    }

    public static boolean addCompletedCourse(String username, String courseTitle) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("completedCourse", courseTitle);
        return updateStudentInfo(body);
    }

    public static boolean placeHold(String username, String holdDescription) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("hold", holdDescription);
        return updateStudentInfo(body);
    }

    public static boolean clearHolds(String username) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("clearHolds", true);
        return updateStudentInfo(body);
    }

    public static boolean overrideEnrolledCredits(String username, int newCredits) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("enrolledCredits", newCredits);
        return updateStudentInfo(body);
    }

    public static boolean overrideTotalCredits(String username, int newCredits) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("totalCredits", newCredits);
        return updateStudentInfo(body);
    }

    public static boolean overrideBill(String username, float newBill) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("bill", newBill);
        return updateStudentInfo(body);
    }

    public static boolean overrideAid(String username, float newAid) {
        if (apiKey.equals("")) return false;
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("financialAid", newAid);
        return updateStudentInfo(body);
    }

    private static boolean updateStudentInfo(JsonObject info) {
        HttpResponse<String> response = post("/admin/update-student", info.toString(), "x-api-key", apiKey);
        if (response == null) return false;
        info = JsonParser.parseString(response.body()).getAsJsonObject();
        String msg = info.get("message").toString();
        System.out.println(msg);
        return !msg.contains("Error:");
    }
}
