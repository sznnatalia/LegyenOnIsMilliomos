/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JOptionPane;

/**
 *
 * @author Droti
 */
public class JelszoDialog extends javax.swing.JDialog {

    private boolean mentes;
    private String jelszo;

    public JelszoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setTitle("Jelszó megváltoztatása");
        setLocationRelativeTo(null);

        mentes = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btMentes = new javax.swing.JButton();
        btMegsem = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tfJelszo1 = new javax.swing.JPasswordField();
        tfJelszo2 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Új jelszó*");

        jLabel2.setText("Új jelszó újra");

        btMentes.setText("Mentés");
        btMentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMentesActionPerformed(evt);
            }
        });

        btMegsem.setText("Mégsem");
        btMegsem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMegsemActionPerformed(evt);
            }
        });

        jLabel3.setText("*Legalább 4, legfeljebb 10 karakter");

        tfJelszo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfJelszo2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btMentes)
                        .addGap(68, 68, 68)
                        .addComponent(btMegsem))
                    .addComponent(tfJelszo1)
                    .addComponent(tfJelszo2, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(tfJelszo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfJelszo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btMentes)
                    .addComponent(btMegsem))
                .addGap(22, 22, 22)
                .addComponent(jLabel3)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btMentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMentesActionPerformed
        String jelszo1 = tfJelszo1.getText();
        String jelszo2 = tfJelszo2.getText();

        if (jelszo1.length() < 4 || jelszo1.length() > 10) {
            JOptionPane.showMessageDialog(rootPane, "Nem megfelelő hosszúságú jelszó (legalább 4, legfeljebb 10 karakter)", "Nem megfelelő jelszó", JOptionPane.WARNING_MESSAGE);
        } else if (!jelszo1.equals(jelszo2)) {
            JOptionPane.showMessageDialog(rootPane, "Nem egyezik a két beírt jelszó", "Nem megfelelő jelszó", JOptionPane.WARNING_MESSAGE);
        } else {
            mentes = true;
            jelszo = jelszo1;
            setVisible(false);
        }
    }//GEN-LAST:event_btMentesActionPerformed

    private void btMegsemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMegsemActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btMegsemActionPerformed

    private void tfJelszo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfJelszo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfJelszo2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JelszoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JelszoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JelszoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JelszoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JelszoDialog dialog = new JelszoDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    public String getJelszo() {
        return jelszo;
    }

    public boolean isMentes() {
        return mentes;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btMegsem;
    private javax.swing.JButton btMentes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField tfJelszo1;
    private javax.swing.JPasswordField tfJelszo2;
    // End of variables declaration//GEN-END:variables
}
