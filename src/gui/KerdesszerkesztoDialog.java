/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import model.IModel;
import model.Kerdes;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Droti
 */
public class KerdesszerkesztoDialog extends javax.swing.JDialog {

    private List<Kerdes> kerdesek;
    private java.awt.Frame parent;
    private IModel model;
    private DefaultTableModel dtm;

    private void refreshTable() {
        try {
            kerdesek = model.getAllKerdes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
        }

        dtm.getDataVector().clear();

        for (Kerdes k : kerdesek) {
            Vector sor = new Vector();

            sor.add(k.getId());
            sor.add(k.getKerdes());
            sor.add(k.getValasz0());
            sor.add(k.getValasz1());
            sor.add(k.getValasz2());
            sor.add(k.getValasz3());
            sor.add((k.getHelyesValasz()+1));

            dtm.addRow(sor);
        }

        dtm.fireTableDataChanged();
    }

    public KerdesszerkesztoDialog(java.awt.Frame parent, boolean modal, IModel model) {
        super(parent, modal);
        initComponents();

        setTitle("Kérdésszerkesztő");
        setLocationRelativeTo(null);

        this.model = model;
        this.parent = parent;

        dtm = (DefaultTableModel) tblKerdesek.getModel();
        

        refreshTable();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblKerdesek = new javax.swing.JTable();
        btTorles = new javax.swing.JButton();
        btKilepes = new javax.swing.JButton();
        btUjKerdes = new javax.swing.JButton();
        btModosit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblKerdesek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "kerdes", "valasz 1", "valasz 2", "valasz 3", "valasz 4", "helyes_valasz"
            }
        ));
        jScrollPane1.setViewportView(tblKerdesek);

        btTorles.setText("Törlés");
        btTorles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTorlesActionPerformed(evt);
            }
        });

        btKilepes.setText("Kilépés");
        btKilepes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKilepesActionPerformed(evt);
            }
        });

        btUjKerdes.setText("Új kérdés");
        btUjKerdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUjKerdesActionPerformed(evt);
            }
        });

        btModosit.setText("Módosít");
        btModosit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btModositActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btModosit)
                        .addGap(54, 54, 54)
                        .addComponent(btTorles)
                        .addGap(60, 60, 60)
                        .addComponent(btUjKerdes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btKilepes))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btModosit, btTorles, btUjKerdes});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btKilepes)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btTorles)
                        .addComponent(btModosit)
                        .addComponent(btUjKerdes)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btUjKerdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUjKerdesActionPerformed
        KerdesAdatDialog kad = new KerdesAdatDialog(parent, true, null);
        kad.setVisible(true);

        if (kad.isMentes()) {
            Kerdes ujKerdes = kad.getKerdes();
            try {
                model.addKerdes(ujKerdes);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
        refreshTable();
    }//GEN-LAST:event_btUjKerdesActionPerformed

    private void btKilepesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKilepesActionPerformed
        setVisible(false);

    }//GEN-LAST:event_btKilepesActionPerformed

    private void btTorlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTorlesActionPerformed
        int sorIndex = tblKerdesek.getSelectedRow();
        
        if (sorIndex<0){
            JOptionPane.showMessageDialog(rootPane, "Nincsen kiválasztva kérdés", "Figyelmeztetés", JOptionPane.WARNING_MESSAGE);
        } else {
         int valasz =   JOptionPane.showConfirmDialog(parent, "Biztosan törölni szeretné a kiválasztott kérdést?", "Törlési megerősítés", JOptionPane.YES_NO_OPTION);
        if (valasz == JOptionPane.YES_OPTION){
            Kerdes kerdes = kerdesek.get(sorIndex);
            
             try {
                 model.deleteKerdes(kerdes);
                 refreshTable();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
             }
        }
        
        }
    }//GEN-LAST:event_btTorlesActionPerformed

    private void btModositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btModositActionPerformed
        int sorIndex = tblKerdesek.getSelectedRow();
        
        if (sorIndex<0){
            JOptionPane.showMessageDialog(rootPane, "Nincsen kiválasztva kérdés", "Figyelmeztetés", JOptionPane.WARNING_MESSAGE);
        } else {
         
            Kerdes kerdes = kerdesek.get(sorIndex);
            
            KerdesAdatDialog kad = new KerdesAdatDialog(parent, true, kerdes);
            kad.setVisible(true);
            
            if (kad.isMentes()){
                try {
                    kerdes = kad.getKerdes();
                    
                    model.updateKerdes(kerdes);
                    refreshTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
                }
            }
        
        
        }
    }//GEN-LAST:event_btModositActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btKilepes;
    private javax.swing.JButton btModosit;
    private javax.swing.JButton btTorles;
    private javax.swing.JButton btUjKerdes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKerdesek;
    // End of variables declaration//GEN-END:variables

   
    }

