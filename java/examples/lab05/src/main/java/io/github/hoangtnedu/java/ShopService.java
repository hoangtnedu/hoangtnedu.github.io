package io.github.hoangtnedu.java;
import java.math.BigDecimal;import java.sql.SQLException;import java.util.*;
public class ShopService {
 private final ShopDao dao;public ShopService(ShopDao dao){this.dao=dao;}
 public long product(String name,BigDecimal price,int stock)throws SQLException{validate(name,price,stock);return dao.insert("INSERT INTO san_pham(ten_sp,don_gia,so_luong) VALUES(?,?,?)",name,price,stock);}
 public void editProduct(long id,String name,BigDecimal price,int stock)throws SQLException{validate(name,price,stock);dao.update("UPDATE san_pham SET ten_sp=?,don_gia=?,so_luong=? WHERE ma_sp=?",name,price,stock,id);}
 private void validate(String name,BigDecimal price,int stock){if(name==null||name.isBlank()||price==null||price.signum()<0||stock<0)throw new IllegalArgumentException("San pham khong hop le");}
 public long customer(String name,String phone,String address)throws SQLException{if(name.isBlank()||!phone.matches("0[0-9]{9}"))throw new IllegalArgumentException("Ten/SDT khong hop le");return dao.insert("INSERT INTO khach_hang(ten_kh,sdt,dia_chi) VALUES(?,?,?)",name,phone,address);}
 public long invoice(long customer,Map<Long,Integer> items)throws SQLException{if(items.isEmpty())throw new IllegalArgumentException("Hoa don rong");return dao.invoice(customer,items);}
 public List<Object[]> products(String search)throws SQLException{return dao.rows("SELECT * FROM san_pham WHERE LOWER(ten_sp) LIKE LOWER(?) ORDER BY ma_sp","%"+search+"%");}
 public List<Object[]> customers()throws SQLException{return dao.rows("SELECT * FROM khach_hang ORDER BY ma_kh");}
 public List<Object[]> revenue()throws SQLException{return dao.rows("SELECT ngay_lap,SUM(tong_tien) FROM hoa_don GROUP BY ngay_lap");}
 public void deleteProduct(long id)throws SQLException{dao.update("DELETE FROM san_pham WHERE ma_sp=?",id);}
 public void editCustomer(long id,String name,String phone,String address)throws SQLException{if(name.isBlank()||!phone.matches("0[0-9]{9}"))throw new IllegalArgumentException("Ten/SDT khong hop le");dao.update("UPDATE khach_hang SET ten_kh=?,sdt=?,dia_chi=? WHERE ma_kh=?",name,phone,address,id);}
 public void deleteCustomer(long id)throws SQLException{dao.update("DELETE FROM khach_hang WHERE ma_kh=?",id);}
}