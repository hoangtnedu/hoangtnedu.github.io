package io.github.hoangtnedu.java;
import java.sql.*;import java.math.BigDecimal;import java.util.*;
public class ShopDao {
 public Connection open()throws SQLException{return DriverManager.getConnection(System.getenv().getOrDefault("DB_URL","jdbc:h2:./data/lab05;MODE=MySQL"),System.getenv().getOrDefault("DB_USER","sa"),System.getenv().getOrDefault("DB_PASSWORD",""));}
 public void init()throws SQLException{try(var c=open();var s=c.createStatement()){
 s.execute("CREATE TABLE IF NOT EXISTS san_pham(ma_sp BIGINT AUTO_INCREMENT PRIMARY KEY,ten_sp VARCHAR(100) NOT NULL,don_gia DECIMAL(12,2) NOT NULL,so_luong INT NOT NULL)");
 s.execute("CREATE TABLE IF NOT EXISTS khach_hang(ma_kh BIGINT AUTO_INCREMENT PRIMARY KEY,ten_kh VARCHAR(100) NOT NULL,sdt VARCHAR(10) NOT NULL,dia_chi VARCHAR(255))");
 s.execute("CREATE TABLE IF NOT EXISTS hoa_don(ma_hd BIGINT AUTO_INCREMENT PRIMARY KEY,ma_kh BIGINT NOT NULL,ngay_lap DATE NOT NULL,tong_tien DECIMAL(12,2),FOREIGN KEY(ma_kh) REFERENCES khach_hang(ma_kh))");
 s.execute("CREATE TABLE IF NOT EXISTS chi_tiet_hoa_don(ma_hd BIGINT,ma_sp BIGINT,so_luong INT,don_gia DECIMAL(12,2),thanh_tien DECIMAL(12,2),PRIMARY KEY(ma_hd,ma_sp),FOREIGN KEY(ma_hd) REFERENCES hoa_don(ma_hd),FOREIGN KEY(ma_sp) REFERENCES san_pham(ma_sp))");}}
 public long insert(String sql,Object...args)throws SQLException{try(var c=open();var p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);p.executeUpdate();try(var r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Missing key");return r.getLong(1);}}}
 public void update(String sql,Object...args)throws SQLException{try(var c=open();var p=c.prepareStatement(sql)){for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);if(p.executeUpdate()!=1)throw new SQLException("Khong tim thay ban ghi");}}
 public List<Object[]> rows(String sql,Object...args)throws SQLException{try(var c=open();var p=c.prepareStatement(sql)){for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);try(var r=p.executeQuery()){List<Object[]> out=new ArrayList<>();while(r.next()){Object[] row=new Object[r.getMetaData().getColumnCount()];for(int i=0;i<row.length;i++)row[i]=r.getObject(i+1);out.add(row);}return out;}}}
 public long invoice(long customer,Map<Long,Integer> items)throws SQLException{
  try(var c=open()){c.setAutoCommit(false);try{
   long id;try(var p=c.prepareStatement("INSERT INTO hoa_don(ma_kh,ngay_lap,tong_tien) VALUES(?,CURRENT_DATE,0)",Statement.RETURN_GENERATED_KEYS)){p.setLong(1,customer);p.executeUpdate();try(var r=p.getGeneratedKeys()){r.next();id=r.getLong(1);}}
   BigDecimal total=BigDecimal.ZERO;
   for(var item:items.entrySet()){
    BigDecimal price;int stock;try(var p=c.prepareStatement("SELECT don_gia,so_luong FROM san_pham WHERE ma_sp=? FOR UPDATE")){p.setLong(1,item.getKey());try(var r=p.executeQuery()){if(!r.next())throw new SQLException("Khong tim thay san pham");price=r.getBigDecimal(1);stock=r.getInt(2);}}
    if(item.getValue()<1||item.getValue()>stock)throw new SQLException("Khong du ton kho");
    BigDecimal amount=price.multiply(BigDecimal.valueOf(item.getValue()));total=total.add(amount);
    try(var p=c.prepareStatement("INSERT INTO chi_tiet_hoa_don VALUES(?,?,?,?,?)")){p.setLong(1,id);p.setLong(2,item.getKey());p.setInt(3,item.getValue());p.setBigDecimal(4,price);p.setBigDecimal(5,amount);p.executeUpdate();}
    try(var p=c.prepareStatement("UPDATE san_pham SET so_luong=so_luong-? WHERE ma_sp=?")){p.setInt(1,item.getValue());p.setLong(2,item.getKey());p.executeUpdate();}
   }
   try(var p=c.prepareStatement("UPDATE hoa_don SET tong_tien=? WHERE ma_hd=?")){p.setBigDecimal(1,total);p.setLong(2,id);p.executeUpdate();}c.commit();return id;
  }catch(Exception ex){c.rollback();if(ex instanceof SQLException sql)throw sql;throw new SQLException(ex);}}
 }
}