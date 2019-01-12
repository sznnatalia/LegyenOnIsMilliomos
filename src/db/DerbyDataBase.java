/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.DBModel;
import java.util.List;
import model.Kerdes;

/**
 *
 * @author Nati
 */
public class DerbyDataBase {

    public static void main(String[] args) {

//      String connURL = "jdbc:mysql://localhost:3306/java_vizsga";
//        String user = "root";
//        String pass = "";
//        Connection conn;
//        List<Kerdes> kerdesek = new ArrayList<>();
//        
//          try {
//            conn = DriverManager.getConnection(connURL, user, pass);
//            DBModel model = new DBModel(conn);
//            kerdesek = model.getAllKerdes();
//            conn.close();
//            
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//
//        } 
        

//            PreparedStatement pstmt = c.prepareStatement("INSERT INTO kerdesek (kerdes,valasz0,valasz1,valasz2,valasz3,helyesvalasz) VALUES (?,?,?,?,?,?)");
//
// //első futtatásnál kell ez, hogy csináljon egy táblát és tegyen bele adatokat
//            //stmt.executeUpdate("CREATE TABLE kerdesek (id int not null primary key GENERATED ALWAYS AS IDENTITY,kerdes varchar(200),valasz0 varchar(200),valasz1 varchar(200),valasz2 varchar(200),valasz3 varchar(200),helyesvalasz int)");
////////             
//            for (Kerdes k : kerdesek) {
//             pstmt.setString(1, k.getKerdes());
//             pstmt.setString(2, k.getValasz0());
//             pstmt.setString(3,k.getValasz1());
//             pstmt.setString(4,k.getValasz2());
//             pstmt.setString(5, k.getValasz3());
//             pstmt.setInt(6, k.getHelyesValasz());
//             
//             pstmt.executeUpdate();
//            }
//            
//
//            pstmt.close();
//            
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }
}

