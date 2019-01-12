/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import model.DBModel;
import model.IModel;
import model.Kerdes;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 *
 * @author Droti
 */
public class LOIMFoablak extends javax.swing.JFrame {

    private List<Kerdes> kerdesek;
    private List<Integer> helytelenValaszokFelezo;
    private boolean felezo;
    private IModel model;
    private int index;
    private Map<Integer, Kerdes> korabbiKerdesek;
    private Kerdes aktualisKerdes;
    private String[] nyeremenyek;
    private String jelszo;

    public void exitProc() {//az ablak bezárásával zárja az adatbázis kapcsolatot is
        if (model != null) {
            try {
                model.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
//új kérdés kiírása

    private void kerdesKiir() {
        Random rn = new Random();

        btValasz0.setEnabled(true);
        btValasz1.setEnabled(true);
        btValasz2.setEnabled(true);
        btValasz3.setEnabled(true);

        aktualisKerdes = kerdesek.get(rn.nextInt(kerdesek.size()));

        while (korabbiKerdesek.containsValue(aktualisKerdes)) {
            aktualisKerdes = kerdesek.get(rn.nextInt(kerdesek.size()));
        }

        lbKerdes.setText(aktualisKerdes.getKerdes());
        btValasz0.setText("1. " + aktualisKerdes.getValasz0());
        btValasz1.setText("2. " + aktualisKerdes.getValasz1());
        btValasz2.setText("3. " + aktualisKerdes.getValasz2());
        btValasz3.setText("4. " + aktualisKerdes.getValasz3());

        korabbiKerdesek.put(aktualisKerdes.getId(), aktualisKerdes);

    }
//feltölti a nyeremények listáját

    private void nyeremenyFeltolt() {

        lstNyeremeny.setListData(nyeremenyek);

    }
//a játék menetétől függően lépteti a kijelölt nyereményt a megfelelő indexű nyereményre

    private void kijeloltNyeremeny() {
        lstNyeremeny.setSelectedIndex(index);
    }

    //az adott válasz alapján megvizsgálja, jó-e a bejelölt válasz
    private void kerdesVizsgal(int sorszam) {

        if (aktualisKerdes.getHelyesValasz() == sorszam) {//ha jól válaszolt a játékos
            if (!nyertes()) {//ha még nem érte el a 40.000.000-t

                boolean tovabbLep = ujKerdes();//megjelenik egy párbeszéd ablak, hogy szeretne-e továbbmenni a játékos
                if (tovabbLep == true) {

                    index--;
                    kijeloltNyeremeny();//lépteti az aktuális nyereményt az index alapján
                    kerdesKiir();//kiírja az új kérdést
                } else {//ha nem akar továbbmenni, megjelenítni a nyereményt
                    String nyeremeny = nyeremenyMegall();
                    JOptionPane.showMessageDialog(rootPane, ("Gratulálok, a nyereménye " + nyeremeny + " Ft."), "Játék vége", JOptionPane.INFORMATION_MESSAGE);
                    gombLezar();//új játék indításig nem lehet a gombokat megnyomni
                }
            }
        } else {//ha rosszul válaszolt, megvizsgálja, van-e garantált nyeremény, és megjeleníti az eredményt
            String nyeremeny = nyeremenyRosszValasz();
            JOptionPane.showMessageDialog(rootPane, ("Helytelen válasz, vége a játéknak. A nyereménye " + nyeremeny + " Ft."), "Helytelen válasz", JOptionPane.INFORMATION_MESSAGE);
            gombLezar();
        }

    }

    private String nyeremenyMegall() {
        return (String) lstNyeremeny.getSelectedValue();
    }

    private String nyeremenyRosszValasz() {
        String nyeremeny = (String) lstNyeremeny.getSelectedValue();

        if (nyeremeny.equals("50 000") || nyeremeny.equals("20 000") || nyeremeny.equals("10 000") || nyeremeny.equals("5000")) {
            index = 14;
            kijeloltNyeremeny();//visszalépteti a kijelölést a garantált nyereményre
            return "0";
        } else if ((nyeremeny.equals("100 000") || nyeremeny.equals("200 000") || nyeremeny.equals("300 000") || nyeremeny.equals("500 000") || nyeremeny.equals("800 000"))) {
            index = 10;
            kijeloltNyeremeny();
            return "100 000";
        } else if (nyeremeny.equals("3 000 000") || nyeremeny.equals("5 000 000")
                || nyeremeny.equals("10 000 000") || nyeremeny.equals("20 000 000")) {
            index = 5;
            kijeloltNyeremeny();
            return "1 500 000";
        }
        return null;
    }

    private boolean ujKerdes() {
        int valasz = JOptionPane.showConfirmDialog(rootPane, "Helyes válasz, gratulálok. Szeretne továbbmenni " + nyeremenyek[index - 1] + " forintért?", "Továbblépés", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (valasz == JOptionPane.YES_OPTION) {
            felezo = false;
            return true;
        } else {
            return false;
        }
    }

    private void gombLezar() {
        btValasz0.setEnabled(false);
        btValasz1.setEnabled(false);
        btValasz2.setEnabled(false);
        btValasz3.setEnabled(false);

    }

    private boolean nyertes() {
        if (index == 0) {
            JOptionPane.showMessageDialog(rootPane, "Gratulálok, megnyerte a 40.000.000 forintot!", "Játék vége", JOptionPane.INFORMATION_MESSAGE);
            gombLezar();
            return true;
        }

        return false;
    }

    private void kerdesVizsgalThread(int kerdes) {//késleleti a válasz vizsgálatát
        gombLezar();
        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1500);
                return null;
            }

            @Override
            protected void done() {
                kerdesVizsgal(kerdes);
            }

        };

        sw.execute();
    }

    public LOIMFoablak() {
        initComponents();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProc();
                dispose();
                System.exit(0);
            }

        });

        setTitle("Legyen Ön is milliomos");
        setLocationRelativeTo(null);

        jDialog_db_belepes.pack();
        jDialog_db_belepes.setLocationRelativeTo(this);
        jDialog_db_belepes.setTitle("Belépés a kérdésszerkesztőbe");
        jDialog_telefoncsorges.pack();
        jDialog_telefoncsorges.setTitle("Kicseng");
        jDialog_telefoncsorges.setLocationRelativeTo(this);

        UIManager.put("OptionPane.cancelButtonText", "Mégsem");
        UIManager.put("OptionPane.noButtonText", "Nem");
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.yesButtonText", "Igen");

        Container cont = this.getContentPane();
        Color c = new Color(000066);
        cont.setBackground(c);
//        
        nyeremenyek = new String[]{"40 000 000", "20 000 000", "10 000 000", "5 000 000", "2 000 000", "1 500 000", "800 000", "500 000", "300 000", "200 000", "100 000", "50 000", "20 000", "10 000", "5000"};

        index = nyeremenyek.length - 1;

        korabbiKerdesek = new HashMap<>();

        btValasz0.setEnabled(true);
        btValasz1.setEnabled(true);
        btValasz2.setEnabled(true);
        btValasz3.setEnabled(true);
        felezo = false;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:mydb;create=true");
            model = new DBModel(conn);
            kerdesek = model.getAllKerdes();
            jelszo = model.getJelszo();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        kerdesKiir();
        nyeremenyFeltolt();
        kijeloltNyeremeny();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_db_belepes = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        tfPassword = new javax.swing.JPasswordField();
        btOK = new javax.swing.JButton();
        btMegsem = new javax.swing.JButton();
        btJelszoValtoztat = new javax.swing.JButton();
        jDialog_telefoncsorges = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        lbKerdes = new javax.swing.JLabel();
        btValasz0 = new javax.swing.JButton();
        btValasz2 = new javax.swing.JButton();
        btValasz1 = new javax.swing.JButton();
        btValasz3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstNyeremeny = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        btFelezo = new javax.swing.JButton();
        btKozonseg = new javax.swing.JButton();
        btTelefonos = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuUjJatek = new javax.swing.JMenuItem();
        menuKerdesSzerkeszto = new javax.swing.JMenuItem();
        menuKilepes = new javax.swing.JMenuItem();

        jDialog_db_belepes.setTitle("Kapcsolódás...");
        jDialog_db_belepes.setMinimumSize(new java.awt.Dimension(400, 150));
        jDialog_db_belepes.setModal(true);
        jDialog_db_belepes.setResizable(false);

        jLabel3.setText("Jelszó:");
        jLabel3.setToolTipText("A program az alapértelmezett jelszóval indul. A \"Jelszó megváltoztatása\" gombra kattintva lehet módosítani.");

        tfPassword.setText("12345");
        tfPassword.setToolTipText("A program az alapértelmezett jelszóval indul. A \"Jelszó megváltoztatása\" gombra kattintva lehet módosítani");
        tfPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPasswordActionPerformed(evt);
            }
        });

        btOK.setText("OK");
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });

        btMegsem.setText("Mégsem");
        btMegsem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMegsemActionPerformed(evt);
            }
        });

        btJelszoValtoztat.setText("Jelszó megváltoztatása");
        btJelszoValtoztat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btJelszoValtoztatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_db_belepesLayout = new javax.swing.GroupLayout(jDialog_db_belepes.getContentPane());
        jDialog_db_belepes.getContentPane().setLayout(jDialog_db_belepesLayout);
        jDialog_db_belepesLayout.setHorizontalGroup(
            jDialog_db_belepesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_db_belepesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_db_belepesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_db_belepesLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(tfPassword))
                    .addGroup(jDialog_db_belepesLayout.createSequentialGroup()
                        .addComponent(btOK)
                        .addGap(18, 18, 18)
                        .addComponent(btMegsem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(btJelszoValtoztat)))
                .addContainerGap())
        );

        jDialog_db_belepesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btMegsem, btOK});

        jDialog_db_belepesLayout.setVerticalGroup(
            jDialog_db_belepesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_db_belepesLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jDialog_db_belepesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jDialog_db_belepesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btOK)
                    .addComponent(btMegsem)
                    .addComponent(btJelszoValtoztat))
                .addGap(27, 27, 27))
        );

        jDialog_telefoncsorges.setResizable(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/telefon_logo.png"))); // NOI18N

        javax.swing.GroupLayout jDialog_telefoncsorgesLayout = new javax.swing.GroupLayout(jDialog_telefoncsorges.getContentPane());
        jDialog_telefoncsorges.getContentPane().setLayout(jDialog_telefoncsorgesLayout);
        jDialog_telefoncsorgesLayout.setHorizontalGroup(
            jDialog_telefoncsorgesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_telefoncsorgesLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialog_telefoncsorgesLayout.setVerticalGroup(
            jDialog_telefoncsorgesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbKerdes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbKerdes.setForeground(new java.awt.Color(255, 255, 255));
        lbKerdes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btValasz0.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btValasz0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btValasz0ActionPerformed(evt);
            }
        });

        btValasz2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btValasz2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btValasz2ActionPerformed(evt);
            }
        });

        btValasz1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btValasz1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btValasz1ActionPerformed(evt);
            }
        });

        btValasz3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btValasz3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btValasz3ActionPerformed(evt);
            }
        });

        lstNyeremeny.setForeground(new java.awt.Color(51, 0, 153));
        lstNyeremeny.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstNyeremeny.setEnabled(false);
        lstNyeremeny.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstNyeremenyMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstNyeremeny);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nyeremény");

        btFelezo.setText("50:50");
        btFelezo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFelezoActionPerformed(evt);
            }
        });

        btKozonseg.setText("Közönség");
        btKozonseg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKozonsegActionPerformed(evt);
            }
        });

        btTelefonos.setText("Telefonos");
        btTelefonos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTelefonosActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/loim_logo.jpg"))); // NOI18N

        jMenu1.setText("Menü");

        menuUjJatek.setText("Új játék");
        menuUjJatek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUjJatekActionPerformed(evt);
            }
        });
        jMenu1.add(menuUjJatek);

        menuKerdesSzerkeszto.setText("Kérdésszerkesztő");
        menuKerdesSzerkeszto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKerdesSzerkesztoActionPerformed(evt);
            }
        });
        jMenu1.add(menuKerdesSzerkeszto);

        menuKilepes.setText("Kilépés");
        menuKilepes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKilepesActionPerformed(evt);
            }
        });
        jMenu1.add(menuKilepes);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btValasz0, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btValasz1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btValasz2, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(72, 72, 72)
                                        .addComponent(btValasz3, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(lbKerdes, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btFelezo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(btKozonseg))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel2)))
                        .addGap(26, 26, 26)
                        .addComponent(btTelefonos)))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFelezo)
                    .addComponent(btKozonseg)
                    .addComponent(btTelefonos))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(45, 45, 45)
                        .addComponent(lbKerdes, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btValasz0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btValasz1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btValasz2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btValasz3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btValasz2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btValasz2ActionPerformed
        kerdesVizsgalThread(2);

    }//GEN-LAST:event_btValasz2ActionPerformed

    private void btValasz0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btValasz0ActionPerformed

        kerdesVizsgalThread(0);
    }//GEN-LAST:event_btValasz0ActionPerformed

    private void btValasz1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btValasz1ActionPerformed

        kerdesVizsgalThread(1);
    }//GEN-LAST:event_btValasz1ActionPerformed

    private void btValasz3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btValasz3ActionPerformed

        kerdesVizsgalThread(3);
    }//GEN-LAST:event_btValasz3ActionPerformed

    private void menuKilepesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKilepesActionPerformed
        exitProc();
        dispose();
        System.exit(0);
    }//GEN-LAST:event_menuKilepesActionPerformed

    private void menuUjJatekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUjJatekActionPerformed
        index = 14;

        felezo = false;

        btValasz0.setEnabled(true);
        btValasz1.setEnabled(true);
        btValasz2.setEnabled(true);
        btValasz3.setEnabled(true);
        btFelezo.setEnabled(true);
        btKozonseg.setEnabled(true);
        btTelefonos.setEnabled(true);

        kerdesKiir();
        nyeremenyFeltolt();
        kijeloltNyeremeny();
    }//GEN-LAST:event_menuUjJatekActionPerformed

    private void menuKerdesSzerkesztoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKerdesSzerkesztoActionPerformed

        jDialog_db_belepes.setVisible(true);


    }//GEN-LAST:event_menuKerdesSzerkesztoActionPerformed

    private void lstNyeremenyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstNyeremenyMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lstNyeremenyMouseClicked

    private void btFelezoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFelezoActionPerformed
        btFelezo.setEnabled(false);
        felezo = true;
        Random rn = new Random();
        Integer helyesValasz = (Integer) aktualisKerdes.getHelyesValasz();

        helytelenValaszokFelezo = new ArrayList<>();

        for (int i = 0; i < 4; i++) { //feltölt egy listát 4 számmal, 0-3 között
            helytelenValaszokFelezo.add(i);
        }

        helytelenValaszokFelezo.remove(helyesValasz); //kiveszi a jó válasz sorszámát
        helytelenValaszokFelezo.remove(rn.nextInt(3)); //a maradék 3 szám közül kivesz még egyet random index alapján

        switch (helytelenValaszokFelezo.get(0)) { //beazonosítja a listában marad kérdéseket
            case 0:
                btValasz0.setEnabled(false);
                break;
            case 1:
                btValasz1.setEnabled(false);
                break;
            case 2:
                btValasz2.setEnabled(false);
                break;
            case 3:
                btValasz3.setEnabled(false);
                break;

        }

        switch (helytelenValaszokFelezo.get(1)) {
            case 0:
                btValasz0.setEnabled(false);
                break;
            case 1:
                btValasz1.setEnabled(false);
                break;
            case 2:
                btValasz2.setEnabled(false);
                break;
            case 3:
                btValasz3.setEnabled(false);
                break;

        }


    }//GEN-LAST:event_btFelezoActionPerformed

    private void btKozonsegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKozonsegActionPerformed
        Random rn = new Random();
        btKozonseg.setEnabled(false);

        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1500);
                return null;
            }

            @Override
            protected void done() {

                if (!felezo) {//megviszgálja, volt-e már felezés, és attól függően ad választ
                    int vJo = 40;//a válaszok között elosztja a százalékokat, a jó válasz 40%-kal indul
                    int v1 = 0;
                    int v2 = 0;
                    int v3 = 0;

                    int osszeg = 60;

                    while (osszeg > 0) {//szétosztja a maradék 60%-ot

                        int valasz = rn.nextInt(4);
                        switch (valasz) {
                            case 0:
                                vJo++;
                                osszeg--;
                                break;
                            case 1:
                                v1++;
                                osszeg--;
                                break;
                            case 2:
                                v2++;
                                osszeg--;
                                break;
                            case 3:
                                v3++;
                                osszeg--;
                                break;
                        }

                    }

                    String eredmeny = "";
                    switch (aktualisKerdes.getHelyesValasz()) {//attól függően, az aktuális kérdésnél mi a helyes válasz, kiírja a szétosztott %-okakt a kérdésekhez
                        case 0:
                            eredmeny = "1. válasz " + vJo + " %"
                                    + "\n2. válasz " + v1 + " %"
                                    + "\n3. válasz " + v2 + " %"
                                    + "\n4. válasz " + v3 + " %";
                            break;
                        case 1:
                            eredmeny = "1. válasz " + v1 + " %"
                                    + "\n2. válasz " + vJo + " %"
                                    + "\n3. válasz " + v2 + " %"
                                    + "\n4. válasz " + v3 + " %";
                            break;
                        case 2:
                            eredmeny = "1. válasz " + v1 + " %"
                                    + "\n2. válasz " + v2 + " %"
                                    + "\n3. válasz " + vJo + " %"
                                    + "\n4. válasz " + v3 + " %";
                            break;
                        case 3:
                            eredmeny = "1. válasz " + v1 + " %"
                                    + "\n2. válasz " + v2 + " %"
                                    + "\n3. válasz " + v3 + " %"
                                    + "\n4. válasz " + vJo + " %";
                            break;

                    }

                    JOptionPane.showMessageDialog(rootPane, eredmeny, "A közönségszavazás eredménye", JOptionPane.INFORMATION_MESSAGE);

                } else {//ha volt már felezés, be kell azonosítani a még aktív helytelen választ
                    int joValasz = aktualisKerdes.getHelyesValasz();

                    int helytelenValasz1 = helytelenValaszokFelezo.get(0);

                    int helytelenValasz2 = helytelenValaszokFelezo.get(1);

                    int masikValaszValoszinuseg = rn.nextInt(41) + 20;//a még aktív helytelen válasz kap egy százalékot 40 és 60% között
                    int joValaszValoszinuseg = 100 - masikValaszValoszinuseg;//a jó válasz kapja a maradék százalékot
                    String szoveg = "";

                    if (joValasz == 0 && helytelenValasz1 == 1 && helytelenValasz2 == 2) {//attól függően, mi volt a jó válasz és a még aktív rossz válasz, megadja a szavazás eredményét
                        szoveg = "1. válasz " + joValaszValoszinuseg + " %"
                                + "\n2. válasz 0 %"
                                + "\n3. válasz 0 %"
                                + "\n4. válasz " + masikValaszValoszinuseg + " %";

                    } else if (joValasz == 0 && helytelenValasz1 == 1 && helytelenValasz2 == 3) {
                        szoveg = "1. válasz " + joValaszValoszinuseg + " %"
                                + "\n2. válasz 0 %"
                                + "\n3. válasz " + masikValaszValoszinuseg + " %"
                                + "\n4. válasz 0 %";

                    } else if (joValasz == 0 && helytelenValasz1 == 2 && helytelenValasz2 == 3) {
                        szoveg = "1. válasz " + joValaszValoszinuseg + " %"
                                + "\n2. válasz " + masikValaszValoszinuseg + " %"
                                + "\n3. válasz 0 %"
                                + "\n4. válasz 0 %";

                    } else if (joValasz == 1 && helytelenValasz1 == 0 && helytelenValasz2 == 2) {
                        szoveg = "1. válasz 0 %"
                                + "\n2. válasz " + joValaszValoszinuseg + " %"
                                + "\n3. válasz 0 %"
                                + "\n4. válasz " + masikValaszValoszinuseg + " %";

                    } else if (joValasz == 1 && helytelenValasz1 == 0 && helytelenValasz2 == 3) {
                        szoveg = "1. válasz 0 %"
                                + "\n2. válasz " + joValaszValoszinuseg + " %"
                                + "\n3. válasz " + masikValaszValoszinuseg + " %"
                                + "\n4. válasz 0 %";

                    } else if (joValasz == 1 && helytelenValasz1 == 2 && helytelenValasz2 == 3) {
                        szoveg = "1. válasz " + masikValaszValoszinuseg + " %"
                                + "\n2. válasz " + joValaszValoszinuseg + " %"
                                + "\n3. válasz 0 %"
                                + "\n4. válasz 0 %";

                    } else if (joValasz == 2 && helytelenValasz1 == 1 && helytelenValasz2 == 3) {
                        szoveg = "1. válasz " + masikValaszValoszinuseg + " %"
                                + "\n2. válasz 0 %"
                                + "\n3. válasz " + joValaszValoszinuseg + " %"
                                + "\n4. válasz 0 %";

                    } else if (joValasz == 2 && helytelenValasz1 == 0 && helytelenValasz2 == 1) {
                        szoveg
                                = "1. válasz 0 %"
                                + "\n2. válasz 0 %"
                                + "\n3. válasz " + joValaszValoszinuseg + " %"
                                + "\n4. válasz " + masikValaszValoszinuseg + " %";

                    } else if (joValasz == 2 && helytelenValasz1 == 0 && helytelenValasz2 == 3) {
                        szoveg
                                = "1. válasz 0 %"
                                + "\n2. válasz " + masikValaszValoszinuseg + " %"
                                + "\n3. válasz " + joValaszValoszinuseg + " %"
                                + "\n4. válasz 0 %";

                    } else if (joValasz == 3 && helytelenValasz1 == 0 && helytelenValasz2 == 1) {
                        szoveg
                                = "1. válasz 0 %"
                                + "\n2. válasz 0 %"
                                + "\n3. válasz " + masikValaszValoszinuseg + " %"
                                + "\n4. válasz " + joValaszValoszinuseg + " %";

                    } else if (joValasz == 3 && helytelenValasz1 == 1 && helytelenValasz2 == 2) {
                        szoveg
                                = "1. válasz " + masikValaszValoszinuseg + " %"
                                + "\n2. válasz 0 %"
                                + "\n3. válasz 0 %"
                                + "\n4. válasz " + joValaszValoszinuseg + " %";

                    } else if (joValasz == 3 && helytelenValasz1 == 0 && helytelenValasz2 == 2) {
                        szoveg
                                = "1. válasz 0 %"
                                + "\n2. válasz " + masikValaszValoszinuseg + " %"
                                + "\n3. válasz 0 %"
                                + "\n4. válasz " + joValaszValoszinuseg + " %";

                    }

                    JOptionPane.showMessageDialog(rootPane, szoveg, "A közönségszavazás eredménye", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        };
                
        sw.run();
    }//GEN-LAST:event_btKozonsegActionPerformed

    private void btTelefonosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTelefonosActionPerformed

        btTelefonos.setEnabled(false);

        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                //jDialog_telefoncsorges.setVisible(true);
                Thread.sleep(2000);
                //jDialog_telefoncsorges.setVisible(false);
                return null;
            }

            @Override
            protected void done() {

                Random rn = new Random();

                if (!felezo) { //ha még nem felezte le a válaszokat, más lesz a válasz, mintha már lefelezte
                    Map<Integer, Integer> valaszok = new HashMap<>();

                    valaszok.put(0, 0);
                    valaszok.put(1, 0);
                    valaszok.put(2, 0);
                    valaszok.put(3, 0);

                    valaszok.replace(aktualisKerdes.getHelyesValasz(), 40); //a jó válasz eleve 40% előnnyel indul

                    int osszeg = 60; //még 60%-nyi valószínűséget szétoszt a válaszok között

                    while (osszeg > 0) {

                        int valasz = rn.nextInt(4); //a random kisorsolt válaszhoz hozzáad 1%-ot
                        int szazalek = valaszok.get(valasz);
                        szazalek++;
                        valaszok.replace(valasz, szazalek);
                        osszeg--;

                    }

                    int maxSzazalek = 0;
                    int maxValasz = 0;
                    for (Integer key : valaszok.keySet()) {
                        if (valaszok.get(key) > maxSzazalek) {
                            maxSzazalek = valaszok.get(key);
                            maxValasz = key;
                        }
                    }
                    String telefonosValasz = "";

                    switch (maxValasz) {
                        case 0:
                            telefonosValasz += "Szerintem az 1. válasz jó";
                            break;
                        case 1:
                            telefonosValasz += "Szerintem a 2. válasz jó";
                            break;
                        case 2:
                            telefonosValasz += "Szerintem a 3. válasz jó";
                            break;
                        case 3:
                            telefonosValasz += "Szerintem a 4. válasz jó";
                            break;
                    }

                    if (maxSzazalek < 85) {
                        telefonosValasz += ", de nem vagyok biztos benne.";
                    } else if (maxSzazalek < 95) {
                        telefonosValasz += ", ebben eléggé biztos vagyok.";
                    } else {
                        telefonosValasz += ", teljesen biztos vagyok benne.";
                    }

                    JOptionPane.showMessageDialog(rootPane, telefonosValasz, "Telefonos válasz", JOptionPane.PLAIN_MESSAGE);
                } else {
                    int joValasz = aktualisKerdes.getHelyesValasz();

                    int helytelenValasz1 = helytelenValaszokFelezo.get(0);

                    int helytelenValasz2 = helytelenValaszokFelezo.get(1);

                    int masikValaszValoszinuseg = rn.nextInt(41) + 20; //a rossz válasz kap 40-60% között egy valószínűséget
                    int joValaszValoszinuseg = 100 - masikValaszValoszinuseg; //a jó válasz kapja a maradél valószínűséget
                    System.out.println("Jó válasz: " + joValasz + " valószínűség: " + joValaszValoszinuseg);

                    List<Integer> valaszok = new ArrayList<>(); //a válaszok közül kizárással kiválasztja azt, amelyik még aktív (nem takarta le a felező) és nem a jó válasz
                    valaszok.add(0);
                    valaszok.add(1);
                    valaszok.add(2);
                    valaszok.add(3);
                    valaszok.remove((Integer) helytelenValasz1);
                    valaszok.remove((Integer) helytelenValasz2);
                    valaszok.remove((Integer) joValasz);

                    System.out.println("Másik válasz: " + valaszok.get(0) + " valószínűség: " + masikValaszValoszinuseg);

                    String telefonosValasz = "";

                    if (joValaszValoszinuseg > masikValaszValoszinuseg) {//ha a jó válasz kapta a nagyobb valószínűséget, akkor azt válaszolja
                        telefonosValasz += "Szerintem a " + (joValasz + 1) + ". válasz a jó.";
                    } else {//ha a rossz válasz kapta a nagyobb valószínűséget, akkor a rossz válasz indexétől függően írja ki a választ

                        switch (valaszok.get(0)) {
                            case 0:
                                telefonosValasz += "Szerintem az 1. válasz a jó.";
                                break;
                            case 1:
                                telefonosValasz += "Szerintem a 2. válasz a jó.";
                                break;
                            case 2:
                                telefonosValasz += "Szerintem a 3. válasz a jó.";
                                break;
                            case 3:
                                telefonosValasz += "Szerintem a 4. válasz a jó.";
                                break;
                        }
                    }
                    JOptionPane.showMessageDialog(rootPane, telefonosValasz, "Telefonos válasz", JOptionPane.PLAIN_MESSAGE);
                }
            }
        };

        sw.run();
    }//GEN-LAST:event_btTelefonosActionPerformed

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        jDialog_db_belepes.setVisible(false);
        String pass = tfPassword.getText();
        if (pass != null && pass.equals(jelszo)) {
            KerdesszerkesztoDialog kd = new KerdesszerkesztoDialog(this, true, model);
            kd.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Hibás jelszó", "Figyelmeztetés", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_btOKActionPerformed

    private void btMegsemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMegsemActionPerformed
        jDialog_db_belepes.setVisible(false);
    }//GEN-LAST:event_btMegsemActionPerformed

    private void btJelszoValtoztatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btJelszoValtoztatActionPerformed
        jDialog_db_belepes.setVisible(false);
        JelszoDialog jd = new JelszoDialog(this, true);
        jd.setVisible(true);

        if (jd.isMentes()) {
            try {
                int sor = model.setJelszo(jd.getJelszo());
                if (sor == 1) {
                    jelszo = jd.getJelszo();
                    JOptionPane.showMessageDialog(rootPane, "A jelszó megváltozott", "Jelszó változás", JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

        }
    }//GEN-LAST:event_btJelszoValtoztatActionPerformed

    private void tfPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPasswordActionPerformed

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
            java.util.logging.Logger.getLogger(LOIMFoablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LOIMFoablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LOIMFoablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LOIMFoablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LOIMFoablak().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFelezo;
    private javax.swing.JButton btJelszoValtoztat;
    private javax.swing.JButton btKozonseg;
    private javax.swing.JButton btMegsem;
    private javax.swing.JButton btOK;
    private javax.swing.JButton btTelefonos;
    private javax.swing.JButton btValasz0;
    private javax.swing.JButton btValasz1;
    private javax.swing.JButton btValasz2;
    private javax.swing.JButton btValasz3;
    private javax.swing.JDialog jDialog_db_belepes;
    private javax.swing.JDialog jDialog_telefoncsorges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbKerdes;
    private javax.swing.JList lstNyeremeny;
    private javax.swing.JMenuItem menuKerdesSzerkeszto;
    private javax.swing.JMenuItem menuKilepes;
    private javax.swing.JMenuItem menuUjJatek;
    private javax.swing.JPasswordField tfPassword;
    // End of variables declaration//GEN-END:variables

}
