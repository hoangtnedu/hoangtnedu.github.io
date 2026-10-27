package io.github.hoangtnedu.java;
import java.nio.file.*;import java.io.*;import java.math.BigInteger;import javax.swing.*;import java.util.concurrent.CancellationException;
public class Jobs {
 public static BigInteger fib(int n){if(n<0||n>10000)throw new IllegalArgumentException("0 <= n <= 10000");BigInteger a=BigInteger.ZERO,b=BigInteger.ONE;for(int i=0;i<n;i++){BigInteger c=a.add(b);a=b;b=c;}return a;}
 public static long primeSum(int n){if(n<0||n>10000000)throw new IllegalArgumentException("0 <= N <= 10000000");boolean[] composite=new boolean[n];long sum=0;for(int i=2;i<n;i++){if(Thread.currentThread().isInterrupted())throw new CancellationException();if(!composite[i]){sum+=i;if((long)i*i<n)for(int j=i*i;j<n;j+=i)composite[j]=true;}}return sum;}
 public static long lines(Path path)throws IOException{try(var r=Files.lines(path)){return r.count();}}
 public static SwingWorker<Long,Integer> countdown(int seconds,java.util.function.Consumer<Integer> tick){if(seconds<0)throw new IllegalArgumentException();return new SwingWorker<>(){protected Long doInBackground()throws Exception{for(int i=seconds;i>=0;i--){if(isCancelled())break;publish(i);if(i>0)Thread.sleep(1000);}return 0L;}protected void process(java.util.List<Integer> values){tick.accept(values.get(values.size()-1));}};}
}