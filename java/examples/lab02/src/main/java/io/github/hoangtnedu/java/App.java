package io.github.hoangtnedu.java;
import java.util.Scanner;
public class App {
 public static void main(String[] args){try(Scanner in=new Scanner(System.in)){
 System.out.println("Ma SV:");String id=in.nextLine();System.out.println("Ho ten:");String name=in.nextLine();
 Student s=new Student(id,name,score(in),score(in),score(in));double total=GradeCalculator.total(s);
 System.out.printf(java.util.Locale.US,"%s - %s - %.2f - %s%n",s.id(),s.name(),total,GradeCalculator.classify(total));}}
 private static double score(Scanner in){while(true){System.out.println("Diem:");try{double x=Double.parseDouble(in.nextLine());GradeCalculator.validate(x);return x;}catch(NumberFormatException e){System.out.println("Nhap so hop le");}catch(IllegalArgumentException e){System.out.println(e.getMessage());}}}
}