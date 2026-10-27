package io.github.hoangtnedu.java;

import java.util.Scanner;

public final class App {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("1. Tong chan  2. Tong nghich dao  3. Nguyen to");
                System.out.println("4. Tam giac  5. Fibonacci  0. Thoat");
                try {
                    int choice = readInt(scanner, "Chon bai: ");
                    if (choice == 0) break;
                    switch (choice) {
                        case 1 -> System.out.println(So.tongChanDenN(readInt(scanner, "n = ")));
                        case 2 -> System.out.printf("%.4f%n", So.tongNghichDao(readInt(scanner, "n = ")));
                        case 3 -> System.out.println(So.laSoNguyenTo(readInt(scanner, "n = "))
                                ? "Nguyen to" : "Khong nguyen to");
                        case 4 -> System.out.println(So.loaiTamGiac(
                                readDouble(scanner, "a = "), readDouble(scanner, "b = "),
                                readDouble(scanner, "c = ")));
                        case 5 -> System.out.println(So.dayFibonacci(readInt(scanner, "n = ")));
                        default -> System.out.println("Lua chon khong hop le");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Loi: " + ex.getMessage());
                } catch (java.util.NoSuchElementException ex) {
                    break;
                }
            }
        }
    }
    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }
    private static double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt + "(dau thap phan: .) ");
        return Double.parseDouble(scanner.nextLine().trim());
    }
}
