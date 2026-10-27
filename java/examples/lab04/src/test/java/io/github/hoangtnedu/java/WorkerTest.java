package io.github.hoangtnedu.java;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;import javax.swing.*;import java.nio.file.*;import java.util.concurrent.*;
class WorkerTest {
 @Test void algorithmsAndFile()throws Exception{assertEquals(17,Jobs.primeSum(10));assertEquals("55",Jobs.fib(10).toString());Path p=Files.createTempFile("lab4", ".txt");try{Files.writeString(p,"a\nb\n");assertEquals(2,Jobs.lines(p));}finally{Files.delete(p);}}
 @Test void backgroundDoesNotBlockEdtAndCanCancel()throws Exception{CountDownLatch tick=new CountDownLatch(1);var worker=Jobs.countdown(5,x->{assertTrue(SwingUtilities.isEventDispatchThread());tick.countDown();});worker.execute();assertTrue(tick.await(3,TimeUnit.SECONDS));SwingUtilities.invokeAndWait(()->assertTrue(SwingUtilities.isEventDispatchThread()));assertTrue(worker.cancel(true));assertThrows(CancellationException.class,()->worker.get());}
 @Test void windowBuildsOnEdt()throws Exception{SwingUtilities.invokeAndWait(()->{JFrame f=App.window();f.setVisible(true);f.dispose();});}
}