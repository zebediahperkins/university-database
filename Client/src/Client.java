public class Client {
    public static void main(String[] args) {
        UniversityAPI.login("tsmith14", "ezpass");
        UniversityAPI.getProfile();
        UniversityAPI.viewCourses();
        UniversityAPI.registerForCourse("ENG 101");
        UniversityAPI.dropCourse("ENG 101");
        UniversityAPI.resetPassword("ezpass12");
        UniversityAPI.resetPassword("ezpass");
        UniversityAPI.registerNewStudent(
                "tsmith14", "ezpass",
                "Timothy", "Smith",
                "English", "14 Main Street"
        );
        UniversityAPI.registerNewCourse(
                "ENG 102",
                3,
                new String[] { "ENG 101" },
                "Any"
        );
        UniversityAPI.updateStudentPersonals(
                "tsmith14",
                "Timmy", "Smith",
                "15 Main Street"
        );
        UniversityAPI.logout();

        System.out.println();

        UniversityAPI.login("sys_admin", "uni_admin");
        UniversityAPI.registerNewStudent(
                "tsmith14", "ezpass",
                "Timothy", "Smith",
                "English", "14 Main Street"
        );
        UniversityAPI.registerNewCourse(
                "ENG 102",
                3,
                new String[] { "ENG 101" },
                "Any"
        );
        UniversityAPI.updateStudentPersonals(
                "tsmith14",
                "Timmy", "Smith",
                "15 Main Street"
        );
        UniversityAPI.changeStudentMajor("tsmith14", "Computer Science");
        UniversityAPI.placeHold("tsmith14", "Financial hold");
        UniversityAPI.logout();
    }
}