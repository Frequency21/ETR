package main.model;

public class GradePointAverage {
   private String etrCode;
   private String firstName;
   private String lastName;
   private short year;
   private byte semester;
   private double average;
   private double kki;
   private int creditSum;

   public GradePointAverage(String etrCode, String firstName, String lastName,
                            short year, byte semester, double average, double kki, int creditSum) {
      this.etrCode = etrCode;
      this.firstName = firstName;
      this.lastName = lastName;
      this.year = year;
      this.semester = semester;
      this.average = average;
      this.kki = kki;
      this.creditSum = creditSum;
   }

   public String getEtrCode() {
      return etrCode;
   }

   public void setEtrCode(String etrCode) {
      this.etrCode = etrCode;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public short getYear() {
      return year;
   }

   public void setYear(short year) {
      this.year = year;
   }

   public byte getSemester() {
      return semester;
   }

   public void setSemester(byte semester) {
      this.semester = semester;
   }

   public double getAverage() {
      return average;
   }

   public void setAverage(double average) {
      this.average = average;
   }

   public double getKki() {
      return kki;
   }

   public void setKki(double kki) {
      this.kki = kki;
   }

   public int getCreditSum() {
      return creditSum;
   }

   public void setCreditSum(int creditSum) {
      this.creditSum = creditSum;
   }
}
