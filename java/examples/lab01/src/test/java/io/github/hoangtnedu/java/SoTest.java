package io.github.hoangtnedu.java;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SoTest {
    @ParameterizedTest
    @CsvSource({"1,0", "2,2", "9,20", "10,30", "2147483647,1152921503533105152"})
    void evenSumHandlesOddEvenAndLargeInputs(int n, long expected) {
        assertEquals(expected, So.tongChanDenN(n));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void positiveInputOperationsRejectZeroAndNegativeNumbers(int n) {
        assertThrows(IllegalArgumentException.class, () -> So.tongChanDenN(n));
        assertThrows(IllegalArgumentException.class, () -> So.tongNghichDao(n));
        assertThrows(IllegalArgumentException.class, () -> So.dayFibonacci(n));
    }

    @ParameterizedTest
    @CsvSource({"1,1.0", "2,1.5", "4,2.0833333333333333"})
    void harmonicSumUsesFloatingPointDivision(int n, double expected) {
        assertEquals(expected, So.tongNghichDao(n), 1e-12);
    }

    @Test
    void harmonicDemoLimitIsEnforced() {
        assertEquals(14.392726722865724, So.tongNghichDao(1_000_000), 1e-10);
        assertThrows(IllegalArgumentException.class, () -> So.tongNghichDao(1_000_001));
    }

    @ParameterizedTest
    @CsvSource({"-5,false", "0,false", "1,false", "2,true", "17,true",
        "21,false", "49,false", "2147483647,true", "2147483646,false"})
    void primalityHandlesSmallValuesSquaresAndIntegerLimit(int n, boolean expected) {
        assertEquals(expected, So.laSoNguyenTo(n));
    }

    @ParameterizedTest
    @CsvSource({"3,4,5,Tam giac vuong", "5,3,4,Tam giac vuong",
        "3,3,3,Tam giac deu", "2,2,3,Tam giac can", "2,3,4,Tam giac thuong",
        "1,2,3,Khong phai tam giac", "0,1,1,Khong phai tam giac",
        "-1,2,2,Khong phai tam giac", "3e200,4e200,5e200,Tam giac vuong",
        "3e-200,4e-200,5e-200,Tam giac vuong"})
    void triangleClassificationHandlesOrderDegeneracyAndScale(
            double a, double b, double c, String expected) {
        assertEquals(expected, So.loaiTamGiac(a, b, c));
    }

    @Test
    void rightIsoscelesTriangleIsClassifiedBeforeGenericTypes() {
        assertEquals("Tam giac vuong can", So.loaiTamGiac(1, 1, Math.sqrt(2)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void triangleRejectsNonFiniteValuesInEveryPosition(double invalid) {
        assertEquals("Khong phai tam giac", So.loaiTamGiac(invalid, 3, 4));
        assertEquals("Khong phai tam giac", So.loaiTamGiac(3, invalid, 4));
        assertEquals("Khong phai tam giac", So.loaiTamGiac(3, 4, invalid));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,0 1", "7,0 1 1 2 3 5 8"})
    void fibonacciStartsAtZeroAndHasTheRequestedLength(int n, String expected) {
        assertEquals(expected, So.dayFibonacci(n));
    }

    @Test
    void fibonacciAcceptsTheLastLongValueWithoutComputingAnExtraTerm() {
        String[] terms = So.dayFibonacci(93).split(" ");
        assertEquals(93, terms.length);
        assertEquals("4660046610375530309", terms[91]);
        assertEquals("7540113804746346429", terms[92]);
        assertThrows(IllegalArgumentException.class, () -> So.dayFibonacci(94));
    }
}
