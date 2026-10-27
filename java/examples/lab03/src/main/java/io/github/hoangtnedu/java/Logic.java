package io.github.hoangtnedu.java;
import java.math.BigInteger;import java.util.Arrays;
public class Logic {
 public static String solve(double a,double b){if(!Double.isFinite(a)||!Double.isFinite(b))throw new IllegalArgumentException("So khong hop le");return a==0?(b==0?"Vo so nghiem":"Vo nghiem"):Double.toString(-b/a);}
 public static String triangle(double a,double b,double c){double[] t={a,b,c};Arrays.sort(t);if(!Double.isFinite(t[2])||t[0]<=0||t[0]+t[1]<=t[2])return "Khong phai tam giac";double x=t[0]/t[2],y=t[1]/t[2];boolean eq=Math.abs(x-y)<1e-9||Math.abs(y-1)<1e-9,right=Math.abs(x*x+y*y-1)<1e-9;return Math.abs(x-1)<1e-9?"Deu":right&&eq?"Vuong can":right?"Vuong":eq?"Can":"Thuong";}
 public static String fibonacci(int n){if(n<1||n>1000)throw new IllegalArgumentException("1 <= n <= 1000");BigInteger a=BigInteger.ZERO,b=BigInteger.ONE;StringBuilder s=new StringBuilder();for(int i=0;i<n;i++){if(i>0)s.append(' ');s.append(a);BigInteger next=a.add(b);a=b;b=next;}return s.toString();}
}