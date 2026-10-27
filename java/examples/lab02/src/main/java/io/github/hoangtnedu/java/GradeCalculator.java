package io.github.hoangtnedu.java;
public final class GradeCalculator {
 public static void validate(double x){if(!Double.isFinite(x)||x<0||x>10)throw new IllegalArgumentException("Diem phai tu 0 den 10");}
 public static double total(Student s){return s.attendance()*.1+s.midterm()*.3+s.exam()*.6;}
 public static String classify(double s){validate(s);return s>=8.5?"A":s>=7?"B":s>=5.5?"C":s>=4?"D":"F";}
}