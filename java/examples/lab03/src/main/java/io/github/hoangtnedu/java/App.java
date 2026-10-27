package io.github.hoangtnedu.java;
import javax.swing.*;import java.awt.*;import java.util.function.Function;
public class App {
 public static JFrame window(){JFrame f=new JFrame("HoangTN - Lab 3");JTabbedPane tabs=new JTabbedPane();
 tabs.add("1. Chao",panel("Ho ten",x->"Xin chao, "+x.trim()));tabs.add("2. Tong",panel("a,b",x->{String[] p=x.split(",");return Double.toString(Double.parseDouble(p[0])+Double.parseDouble(p[1]));}));
 tabs.add("3. Phuong trinh",panel("a,b",x->{String[] p=x.split(",");return Logic.solve(Double.parseDouble(p[0]),Double.parseDouble(p[1]));}));
 tabs.add("4. Tam giac",panel("a,b,c",x->{String[] p=x.split(",");return Logic.triangle(Double.parseDouble(p[0]),Double.parseDouble(p[1]),Double.parseDouble(p[2]));}));
 tabs.add("5. Fibonacci",panel("n",x->Logic.fibonacci(Integer.parseInt(x.trim()))));f.add(tabs);f.setSize(760,420);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);return f;}
 private static JPanel panel(String label,Function<String,String> action){JPanel p=new JPanel(new BorderLayout());JTextField in=new JTextField();JTextArea out=new JTextArea();out.setEditable(false);JButton run=new JButton("Thuc hien");run.addActionListener(e->{try{out.setText(action.apply(in.getText()));}catch(RuntimeException ex){out.setText("Du lieu khong hop le: "+ex.getMessage());}});JPanel top=new JPanel(new BorderLayout());top.add(new JLabel(label),BorderLayout.WEST);top.add(in);top.add(run,BorderLayout.EAST);p.add(top,BorderLayout.NORTH);p.add(new JScrollPane(out));return p;}
 public static void main(String[] args){SwingUtilities.invokeLater(()->window().setVisible(true));}
}