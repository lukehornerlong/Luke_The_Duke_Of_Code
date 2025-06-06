package fsd.assignment.assignment1.datamodel;

public class Student {
    private String studId;
    private String yearOfStudy;
    private String module1;
    private String module2;
    private String module3;

    /* TODO: the constructor must accept parameters so that all
             instance variables are set accordingly
    */
    public Student(String studId, String yearOfStudy, String module1, String module2, String module3) {

        this.studId = studId;

        this.module1 = module1;

        this.module2 = module2;

        this.module3 = module3;

        this.yearOfStudy = yearOfStudy;

    }


    /* TODO: ensure that all getters are included here
     */
    public String GetModule1() {
        return module1;
    }

    public String GetModule2() {
        return module2;
    }

    public String GetModule3() {
        return module3;
    }

    public String GetYearOfStudy() {
        return yearOfStudy;
    }


    /* TODO: include a toString() that returns studId
     */
    public String ToString() {
        return studId;
    }


}
