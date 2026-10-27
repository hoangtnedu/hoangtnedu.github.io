package io.github.hoangtnedu.java;
import org.junit.jupiter.api.Test;import org.junit.jupiter.params.ParameterizedTest;import org.junit.jupiter.params.provider.CsvSource;import static org.junit.jupiter.api.Assertions.*;
class GradeTest {
 @Test void weightedScore(){assertEquals(8.3,GradeCalculator.total(new Student("SV1","An",8,7,9)),1e-12);}
 @ParameterizedTest @CsvSource({"0,F","3.99,F","4,D","5.5,C","7,B","8.5,A","10,A"}) void boundaries(double x,String grade){assertEquals(grade,GradeCalculator.classify(x));}
 @Test void invalidData(){for(double x:new double[]{-1,11,Double.NaN,Double.POSITIVE_INFINITY})assertThrows(IllegalArgumentException.class,()->GradeCalculator.validate(x));assertThrows(IllegalArgumentException.class,()->new Student("","An",8,7,9));}
}