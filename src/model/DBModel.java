/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nati
 */
public class DBModel implements IModel{
    
    private Connection conn;
    private PreparedStatement getAllKerdesPstmt;
    private PreparedStatement addKerdesPstmt;
    private PreparedStatement updateKerdesPstmt;
    private PreparedStatement deleteKerdesPstmt;
    private PreparedStatement getJelszoPstmt;
    private PreparedStatement setJelszoPstmt;
    

    public DBModel(Connection conn) throws SQLException{
        this.conn = conn;
        getAllKerdesPstmt = conn.prepareStatement("SELECT * FROM kerdesek");
        addKerdesPstmt = conn.prepareStatement("INSERT INTO kerdesek (kerdes, valasz0, valasz1, valasz2, valasz3, helyesvalasz) VALUES (?,?,?,?,?,?)");
        updateKerdesPstmt = conn.prepareStatement("UPDATE kerdesek SET kerdes =?, valasz0=?, valasz1=?, valasz2=?, valasz3=?, helyesvalasz=? WHERE id =?");
        deleteKerdesPstmt = conn.prepareStatement("DELETE FROM kerdesek WHERE id = ?");
        getJelszoPstmt = conn.prepareStatement("SELECT jelszo FROM jelszo WHERE id=1");
        setJelszoPstmt = conn.prepareStatement("UPDATE jelszo SET jelszo =? WHERE id=1");
        
    }
    
    

    @Override
    public List<Kerdes> getAllKerdes() throws SQLException {
        List<Kerdes> kerdesek = new ArrayList<>();
        
        ResultSet rs = getAllKerdesPstmt.executeQuery();
        
        while (rs.next()){
            kerdesek.add(new Kerdes(rs.getInt("id"), rs.getString("kerdes"), rs.getString("valasz0"),rs.getString("valasz1") , 
                    rs.getString("valasz2"), rs.getString("valasz3"), rs.getInt("helyesvalasz")));
            
            
        }
        rs.close();
        return kerdesek;
     }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    @Override
    public void updateKerdes(Kerdes k) throws SQLException {
        updateKerdesPstmt.setString(1, k.getKerdes());
        updateKerdesPstmt.setString(2, k.getValasz0());
        updateKerdesPstmt.setString(3, k.getValasz1());
        updateKerdesPstmt.setString(4, k.getValasz2());
        updateKerdesPstmt.setString(5, k.getValasz3());
        updateKerdesPstmt.setInt(6, k.getHelyesValasz());
        updateKerdesPstmt.setInt(7, k.getId());
        
        updateKerdesPstmt.executeUpdate();
        
    }

    @Override
    public void deleteKerdes(Kerdes k) throws SQLException {
        deleteKerdesPstmt.setInt(1, k.getId());
        
        deleteKerdesPstmt.executeUpdate();
        
    }

    @Override
    public void addKerdes(Kerdes k) throws SQLException {
        addKerdesPstmt.setString(1, k.getKerdes());
        addKerdesPstmt.setString(2, k.getValasz0());
        addKerdesPstmt.setString(3, k.getValasz1());
        addKerdesPstmt.setString(4, k.getValasz2());
        addKerdesPstmt.setString(5, k.getValasz3());
        addKerdesPstmt.setInt(6, k.getHelyesValasz());
        
        addKerdesPstmt.executeUpdate();
    }

    @Override
    public String getJelszo() throws SQLException {
     String jelszo ="";
     ResultSet rs = getJelszoPstmt.executeQuery();
     
     while(rs.next()){
         jelszo += rs.getString("jelszo");
     }
     
     return jelszo;
    }

    @Override
    public int setJelszo(String jelszo) throws SQLException {
        setJelszoPstmt.setString(1, jelszo);
        
        return setJelszoPstmt.executeUpdate();
        

    }
    
    

}
