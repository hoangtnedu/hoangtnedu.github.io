package io.github.hoangtnedu.java;

import java.util.Arrays;

public final class So {
    private So() { }

    public static long tongChanDenN(int n) {
        kiemTraNguyenDuong(n);
        long k = n / 2L;
        return k * (k + 1);
    }

    public static double tongNghichDao(int n) {
        kiemTraNguyenDuong(n);
        if (n > 1_000_000) {
            throw new IllegalArgumentException("Bai demo gioi han n <= 1000000");
        }
        double sum = 0;
        for (int i = 1; i <= n; i++) sum += 1.0 / i;
        return sum;
    }

    public static boolean laSoNguyenTo(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int i = 3; i <= n / i; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static String loaiTamGiac(double a, double b, double c) {
        if (!Double.isFinite(a) || !Double.isFinite(b) || !Double.isFinite(c)
                || a <= 0 || b <= 0 || c <= 0) return "Khong phai tam giac";
        double[] sides = {a, b, c};
        Arrays.sort(sides);
        // Normalize to avoid overflow when adding or squaring large inputs.
        double x = sides[0] / sides[2], y = sides[1] / sides[2];
        if (x + y <= 1.0) return "Khong phai tam giac";
        final double eps = 1e-9;
        boolean deu = Math.abs(x - 1) < eps;
        boolean can = Math.abs(x - y) < eps || Math.abs(y - 1) < eps;
        boolean vuong = Math.abs(x * x + y * y - 1) < eps;
        if (deu) return "Tam giac deu";
        if (can && vuong) return "Tam giac vuong can";
        if (vuong) return "Tam giac vuong";
        if (can) return "Tam giac can";
        return "Tam giac thuong";
    }

    public static String dayFibonacci(int n) {
        kiemTraNguyenDuong(n);
        if (n > 93) throw new IllegalArgumentException("Can 1 <= n <= 93 cho kieu long");
        StringBuilder output = new StringBuilder();
        long previous = 0, current = 1;
        for (int i = 0; i < n; i++) {
            if (i > 0) output.append(' ');
            if (i == 0) output.append(previous);
            else {
                output.append(current);
                if (i < n - 1) {
                    long next = Math.addExact(previous, current);
                    previous = current;
                    current = next;
                }
            }
        }
        return output.toString();
    }

    private static void kiemTraNguyenDuong(int n) {
        if (n <= 0) throw new IllegalArgumentException("n phai la so nguyen duong");
    }
}
