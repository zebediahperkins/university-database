import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!UniversityAPI.isLoggedIn()) {
                String username, password;
                System.out.println("----------------------------------");
                System.out.println("Welcome to the University Database");
                System.out.print("\nUsername: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
                if (username.length() > 0 && password.length() > 0) {
                    UniversityAPI.login(username, password);
                }
            }
            else {
                System.out.println("\n\n\n\n");
                System.out.println("----------------------------------");
                System.out.print("Response: ");
                JsonObject profileJson = UniversityAPI.getProfile();
                System.out.println("----------------------------------");
                System.out.println("            Profile               ");
                System.out.println("            =======               ");
                System.out.println("    Username: " + profileJson.get("username"));
                if (profileJson.get("admin") != null) {
                    System.out.println("    Admin: " + profileJson.get("admin"));
                    System.out.println();
                    System.out.println("----------------------------------");
                    System.out.println("1. Register New Student");
                    System.out.println("2. Register New Course");
                    System.out.println("3. Update Student Info");
                    System.out.println("4. Change Student Major");
                    System.out.println("5. Override Into Course");
                    System.out.println("6. Add Completed Course");
                    System.out.println("7. Place Hold");
                    System.out.println("8. Clear Holds");
                    System.out.println("9. Override Credits");
                    System.out.println("10. Override Total Credits");
                    System.out.println("11. Override Bill");
                    System.out.println("12. Override Aid");
                    System.out.println("13. Logout");
                    System.out.println("----------------------------------");
                    System.out.println();
                    try {
                        switch (Integer.parseInt(scanner.nextLine())) {
                            case 1 -> {
                                String username, password, first, last, major, address;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Password: ");
                                password = scanner.nextLine();
                                System.out.print("First Name: ");
                                first = scanner.nextLine();
                                System.out.print("Last Name: ");
                                last = scanner.nextLine();
                                System.out.print("Major: ");
                                major = scanner.nextLine();
                                System.out.print("Address: ");
                                address = scanner.nextLine();
                                UniversityAPI.registerNewStudent(username, password, first, last, major, address);
                            }
                            case 2 -> {
                                String title, credits, eligible;
                                List<String> prerequisites = new ArrayList<>();
                                System.out.print("Course Title: ");
                                title = scanner.nextLine();
                                System.out.print("Credits: ");
                                credits = scanner.nextLine();
                                System.out.print("Concentration: ");
                                eligible = scanner.nextLine();
                                while (true) {
                                    System.out.print("Prerequisite (Hit enter if none): ");
                                    String tmp = scanner.nextLine();
                                    if (tmp.isEmpty()) break;
                                    prerequisites.add(tmp);
                                }
                                UniversityAPI.registerNewCourse(title, Integer.parseInt(credits), prerequisites.toArray(new String[0]), eligible);
                            }
                            case 3 -> {
                                String username, first, last, address;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("First Name: ");
                                first = scanner.nextLine();
                                System.out.print("Last Name: ");
                                last = scanner.nextLine();
                                System.out.print("Address: ");
                                address = scanner.nextLine();
                                UniversityAPI.updateStudentPersonals(username, first, last, address);
                            }
                            case 4 -> {
                                String username, major;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Major: ");
                                major = scanner.nextLine();
                                UniversityAPI.changeStudentMajor(username, major);
                            }
                            case 5 -> {
                                String username, title;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Course Title: ");
                                title = scanner.nextLine();
                                UniversityAPI.overrideIntoCourse(username, title);
                            }
                            case 6 -> {
                                String username, title;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Course Title: ");
                                title = scanner.nextLine();
                                UniversityAPI.addCompletedCourse(username, title);
                            }
                            case 7 -> {
                                String username, hold;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Hold Description: ");
                                hold = scanner.nextLine();
                                UniversityAPI.placeHold(username, hold);
                            }
                            case 8 -> {
                                String username;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                UniversityAPI.clearHolds(username);
                            }
                            case 9 -> {
                                String username, newCredits;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Credits: ");
                                newCredits = scanner.nextLine();
                                UniversityAPI.overrideEnrolledCredits(username, Integer.parseInt(newCredits));
                            }
                            case 10 -> {
                                String username, newCredits;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Credits: ");
                                newCredits = scanner.nextLine();
                                UniversityAPI.overrideTotalCredits(username, Integer.parseInt(newCredits));
                            }
                            case 11 -> {
                                String username, newBill;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Bill: ");
                                newBill = scanner.nextLine();
                                UniversityAPI.overrideBill(username, Float.parseFloat(newBill));
                            }
                            case 12 -> {
                                String username, newAid;
                                System.out.print("Username: ");
                                username = scanner.nextLine();
                                System.out.print("Aid: ");
                                newAid = scanner.nextLine();
                                UniversityAPI.overrideAid(username, Float.parseFloat(newAid));
                            }
                            case 13 -> UniversityAPI.logout();
                        }
                    } catch (NumberFormatException ignored) {}
                }
                else {
                    System.out.println("            -------               ");
                    System.out.println("         Personal Info            ");
                    System.out.println("            -------               ");
                    System.out.println("    Name: " + profileJson.get("personalInfo").getAsJsonObject().get("firstName") + " " + profileJson.get("personalInfo").getAsJsonObject().get("lastName"));
                    System.out.println("    Address: " + profileJson.get("personalInfo").getAsJsonObject().get("address"));
                    System.out.println("            -------               ");
                    System.out.println("           Academics              ");
                    System.out.println("            -------               ");
                    System.out.println("    Major: " + profileJson.get("academics").getAsJsonObject().get("major"));
                    System.out.println("    Courses: " + profileJson.get("academics").getAsJsonObject().get("courses"));
                    System.out.println("    Credits: " + profileJson.get("academics").getAsJsonObject().get("credits"));
                    System.out.println("    Completed: " + profileJson.get("academics").getAsJsonObject().get("completedCourses"));
                    System.out.println("    Total Credits: " + profileJson.get("academics").getAsJsonObject().get("totalCredits"));
                    System.out.println("    Academic Holds: " + profileJson.get("academics").getAsJsonObject().get("holds"));
                    System.out.println("            -------               ");
                    System.out.println("            Finance               ");
                    System.out.println("            -------               ");
                    System.out.println("    Bill: " + profileJson.get("finances").getAsJsonObject().get("totalBill"));
                    System.out.println("    Aid: " + profileJson.get("finances").getAsJsonObject().get("financialAid"));
                    System.out.println();
                    System.out.println("----------------------------------");
                    System.out.println("1. View Course Catalogue");
                    System.out.println("2. Enroll In Course");
                    System.out.println("3. Drop Course");
                    System.out.println("4. Change Password");
                    System.out.println("5. Logout");
                    System.out.println("----------------------------------");
                    System.out.println();
                    try {
                        switch (Integer.parseInt(scanner.nextLine())) {
                            case 1 -> {
                                JsonArray coursesJson = UniversityAPI.viewCourses().getAsJsonArray("courses");
                                coursesJson.forEach(course -> {
                                    JsonObject courseObj = course.getAsJsonObject();
                                    System.out.println("-------");
                                    System.out.println(courseObj.get("title"));
                                    System.out.println(courseObj.get("credits") + " credits");
                                    System.out.println("Open to " + courseObj.get("eligible") + " majors");
                                    JsonArray prerequisites = courseObj.getAsJsonArray("prerequisites");
                                    System.out.print("Prereqs: ");
                                    for (JsonElement courseTitle : prerequisites) {
                                        System.out.print(courseTitle.toString() + ' ');
                                    }
                                    System.out.println();
                                });
                            }
                            case 2 -> {
                                String courseTitle;
                                System.out.print("Course Title: ");
                                courseTitle = scanner.nextLine();
                                UniversityAPI.registerForCourse(courseTitle);
                            }
                            case 3 -> {
                                String courseTitle;
                                System.out.print("Course Title: ");
                                courseTitle = scanner.nextLine();
                                UniversityAPI.dropCourse(courseTitle);
                            }
                            case 4 -> {
                                String password;
                                System.out.print("New Password: ");
                                password = scanner.nextLine();
                                UniversityAPI.resetPassword(password);
                            }
                            case 5 -> UniversityAPI.logout();
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
            System.out.println("----------------------------------");
            System.out.println("      Type Enter To Continue      ");
            scanner.nextLine();
        }
    }
}