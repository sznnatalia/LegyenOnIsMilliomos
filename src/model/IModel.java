/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Droti
 */
public interface IModel {
    
    List<Kerdes> getAllKerdes() throws SQLException;
    void close() throws SQLException;
    void updateKerdes(Kerdes k)throws SQLException;
    void deleteKerdes(Kerdes k)throws SQLException;
    void addKerdes(Kerdes k)throws SQLException;
    String getJelszo() throws SQLException;
    int setJelszo(String jelszo) throws SQLException;
            
    
     
    
}
