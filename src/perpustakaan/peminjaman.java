/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perpustakaan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author redmibook
 */
public class peminjaman extends javax.swing.JFrame {
    private DefaultTableModel model=null;
    private PreparedStatement stat;
    private ResultSet rs;
    koneksi k=new koneksi();

    /**
     * Creates new form 
     */
    public peminjaman() {
        initComponents();
         this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        k.connect();
        refreshTablePinjam();
        refreshTableBalik();
        updateComboSiswa();
        updateComboBuku();
    }
    
    class tambahbarang extends peminjaman {
    int id_peminjaman, id_siswa, id_buku;
    String status;
    Date tanggal_pinjam, tanggal_balik;

    public tambahbarang() {
        // Safely cast selected items
        // Retrieve the selected item as a string
// Split the string and extract the ID part
String combo = cbSiswa.getSelectedItem().toString(); 
// Split the string and extract the ID part
String[] arrB = combo.split("-");// E.g., "1 - John Doe - 10A"
this.id_siswa = Integer.parseInt(arrB[0]); // Parse the first part as the integer ID
 // Retrieve the ID (first part)
// Retrieve the selected item for the book combo box
String comboB = cbBuku.getSelectedItem().toString(); // Format: "101-Math Book"
this.id_buku = Integer.parseInt(arrB[1]); // Retrieve the ID (first part)


        this.tanggal_pinjam = new java.sql.Date(TPinjam.getDate().getTime());
        this.tanggal_balik = new java.sql.Date(TBalik.getDate().getTime());
        this.status = (String) cbStatus.getSelectedItem();
    }
}
    
 public class ComboItem {
    private int id;
    private String value;

    public ComboItem(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComboItem other = (ComboItem) obj;
        return id == other.id;
    }

}






    
 
    
        public void refreshTablePinjam(){
            model= new DefaultTableModel();
            model.addColumn("Nama Siswa");
            model.addColumn("Judul Buku");
            model.addColumn("Tanggal Pinjam");
            model.addColumn("Status");
            TabelPinjam.setModel(model);
            try {
               this.stat = k.getKoneksi().prepareStatement(
    "SELECT " +
    "siswa.nama AS nama, " +
    "buku.judul AS judul, " +
    "peminjaman.tanggal_pinjam, " +
    "peminjaman.status " +
    "FROM peminjaman " +
    "JOIN siswa ON peminjaman.id_siswa = siswa.id_siswa " +
    "JOIN buku ON peminjaman.id_buku = buku.id_buku " +
    "WHERE peminjaman.status = 'Sedang Meminjam' OR peminjaman.tanggal_balik IS NULL"
);
                this.rs=this.stat.executeQuery();
                while(rs.next()){
                    Object[] data={
                        rs.getString("nama"),
                        rs.getString("judul"),
                        rs.getDate("tanggal_pinjam"),
                        rs.getString("status"),
                    };
                    model.addRow(data);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
            cbBuku.setSelectedItem("");
            cbSiswa.setSelectedItem("");
            TPinjam.setDate(null);
            TBalik.setDate(null);
            cbStatus.setSelectedItem("");     
        }
        
         public void refreshTableBalik(){
            model= new DefaultTableModel();
            model.addColumn("Nama Siswa");
            model.addColumn("Judul Buku");
            model.addColumn("Tanggal Balik");
            model.addColumn("Status");
            TabelBalik.setModel(model);
            try {
                this.stat = k.getKoneksi().prepareStatement(
    "SELECT " +
    "siswa.nama AS nama, " +
    "buku.judul AS judul, " +
    "peminjaman.tanggal_balik, " +
    "peminjaman.status " +
    "FROM peminjaman " +
    "JOIN siswa ON peminjaman.id_siswa = siswa.id_siswa " +
    "JOIN buku ON peminjaman.id_buku = buku.id_buku " +
    "WHERE peminjaman.status = 'Sudah Dibalikan' AND peminjaman.tanggal_balik IS NOT NULL"
);
                this.rs=this.stat.executeQuery();
                while(rs.next()){
                    Object[] data={
                         rs.getString("nama"),
                        rs.getString("judul"),
                        rs.getDate("tanggal_balik"),
                        rs.getString("status"),
                    };
                    model.addRow(data);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
            cbBuku.setSelectedItem("");
            cbSiswa.setSelectedItem("");
            TPinjam.setDate(null);
            TBalik.setDate(null);
            cbStatus.setSelectedItem("");     
        }
        
private void updateComboBuku() {
    String sql = "SELECT * FROM buku";
    try {
        this.stat = k.getKoneksi().prepareStatement(sql);
        rs = stat.executeQuery();
        cbBuku.removeAllItems(); // Clear existing items
        while (rs.next()) {
            int id_buku = rs.getInt("id_buku");
            String judul = rs.getString("judul");
            String pengarang = rs.getString("pengarang");

            // Add items directly as strings in "id - judul - pengarang" format
            cbBuku.addItem(id_buku + " - " + judul + " - " + pengarang);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

private void updateComboSiswa() {
    String sql = "SELECT * FROM siswa";
    try {
        this.stat = k.getKoneksi().prepareStatement(sql);
        rs = stat.executeQuery();
        cbSiswa.removeAllItems(); // Clear existing items
        while (rs.next()) {
            int id_siswa = rs.getInt("id_siswa");
            String nama = rs.getString("nama");
            String kelas = rs.getString("kelas");

            // Add items directly as strings in "id - nama - kelas" format
            cbSiswa.addItem(id_siswa + " - " + nama + " - " + kelas);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
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
        jTable1 = new javax.swing.JTable();
        TBalik = new com.toedter.calendar.JDateChooser();
        TPinjam = new com.toedter.calendar.JDateChooser();
        cbBuku = new javax.swing.JComboBox<>();
        cbSiswa = new javax.swing.JComboBox<>();
        cbStatus = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelBalik = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelPinjam = new javax.swing.JTable();
        tombolsimpanbarang = new javax.swing.JButton();
        tombolgantibarang = new javax.swing.JButton();
        btn_logout = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(TBalik, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 580, 550, 70));
        getContentPane().add(TPinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 470, 550, 70));

        cbBuku.setFont(cbStatus.getFont());
        cbBuku.setBorder(cbStatus.getBorder());
        cbBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBukuActionPerformed(evt);
            }
        });
        getContentPane().add(cbBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 250, 550, 70));

        cbSiswa.setFont(cbStatus.getFont());
        cbSiswa.setBorder(cbStatus.getBorder());
        cbSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSiswaActionPerformed(evt);
            }
        });
        getContentPane().add(cbSiswa, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 360, 550, 60));

        cbStatus.setFont(new java.awt.Font("Rubik", 0, 18)); // NOI18N
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sedang Meminjam", "Sudah Dibalikan" }));
        cbStatus.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        cbStatus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbStatus.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        cbStatus.setOpaque(false);
        getContentPane().add(cbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 700, 550, 60));

        TabelBalik.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TabelBalik.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelBalikMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TabelBalik);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 250, 480, 610));

        jLabel3.setFont(new java.awt.Font("Rubik", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NAMA BUKU");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 230, 100, -1));

        jLabel4.setFont(new java.awt.Font("Rubik", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TANGGAL PINJAM");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 450, 170, -1));

        jLabel1.setFont(new java.awt.Font("Rubik", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TANGGAL BALIK");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 560, 160, -1));

        TabelPinjam.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(142, 114, 69), 1, true));
        TabelPinjam.setFont(new java.awt.Font("Rubik", 0, 13)); // NOI18N
        TabelPinjam.setForeground(new java.awt.Color(0, 0, 102));
        TabelPinjam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TabelPinjam.setFocusable(false);
        TabelPinjam.setGridColor(new java.awt.Color(0, 0, 102));
        TabelPinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelPinjamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TabelPinjam);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1280, 250, 530, 610));

        tombolsimpanbarang.setBackground(new java.awt.Color(255, 255, 255,0));
        tombolsimpanbarang.setFont(new java.awt.Font("Rubik", 0, 18)); // NOI18N
        tombolsimpanbarang.setForeground(new java.awt.Color(255, 255, 255));
        tombolsimpanbarang.setText("Simpan");
        tombolsimpanbarang.setBorderPainted(false);
        tombolsimpanbarang.setFocusPainted(false);
        tombolsimpanbarang.setFocusable(false);
        tombolsimpanbarang.setOpaque(false);
        tombolsimpanbarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolsimpanbarangActionPerformed(evt);
            }
        });
        getContentPane().add(tombolsimpanbarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 810, 150, 50));

        tombolgantibarang.setBackground(new java.awt.Color(255, 255, 255,0));
        tombolgantibarang.setFont(new java.awt.Font("Rubik", 0, 18)); // NOI18N
        tombolgantibarang.setForeground(new java.awt.Color(255, 255, 255));
        tombolgantibarang.setText("Ganti");
        tombolgantibarang.setBorderPainted(false);
        tombolgantibarang.setFocusPainted(false);
        tombolgantibarang.setFocusable(false);
        tombolgantibarang.setOpaque(false);
        tombolgantibarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolgantibarangActionPerformed(evt);
            }
        });
        getContentPane().add(tombolgantibarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 810, 140, 50));

        btn_logout.setBackground(new java.awt.Color(255, 255, 255,0));
        btn_logout.setFont(new java.awt.Font("Rubik", 0, 18)); // NOI18N
        btn_logout.setForeground(new java.awt.Color(255, 255, 255));
        btn_logout.setText("Kembali");
        btn_logout.setBorderPainted(false);
        btn_logout.setFocusPainted(false);
        btn_logout.setFocusable(false);
        btn_logout.setOpaque(false);
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });
        getContentPane().add(btn_logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1600, 130, 140, 50));

        jLabel2.setFont(new java.awt.Font("Rubik", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NAMA SISWA");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 340, 140, -1));

        jLabel6.setFont(new java.awt.Font("Rubik", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("STATUS");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 670, 130, -1));

        jLabel9.setBackground(new java.awt.Color(0, 0, 51));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/perpustakaan/Thelema (2).png"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tombolgantibarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolgantibarangActionPerformed
        // TODO add your handling code here:
      try {
    // Get the selected item from the combo box (the string "id_siswa - nama - kelas")
    String selectedSiswa = (String) cbSiswa.getSelectedItem();
    String[] parts = selectedSiswa.split(" - ");
    int id_siswa = Integer.parseInt(parts[0]);

    String selectedBuku = (String) cbBuku.getSelectedItem();
    parts = selectedBuku.split(" - ");
    int id_buku = Integer.parseInt(parts[0]);

    // Convert the date from date pickers to java.sql.Date
    java.sql.Date tanggal_pinjam = new java.sql.Date(TPinjam.getDate().getTime());

    // Check if the TBalik (return date) is empty or null
    java.sql.Date tanggal_balik = null;
    if (TBalik.getDate() != null) {
        tanggal_balik = new java.sql.Date(TBalik.getDate().getTime());
    }

    // Prepare the SQL query to select id_peminjaman based on id_siswa and id_buku
    String selectSql = "SELECT id_peminjaman FROM peminjaman WHERE id_siswa=? AND id_buku=? LIMIT 1";
    PreparedStatement selectStat = k.getKoneksi().prepareStatement(selectSql);
    selectStat.setInt(1, id_siswa);
    selectStat.setInt(2, id_buku);

    // Execute the select query to get the id_peminjaman
    ResultSet rs = selectStat.executeQuery();
    if (rs.next()) {
        int id_peminjaman = rs.getInt("id_peminjaman");

        // Prepare the SQL update statement using the retrieved id_peminjaman
        String status = (String) cbStatus.getSelectedItem();
        this.stat = k.getKoneksi().prepareStatement("UPDATE peminjaman SET id_buku=?, id_siswa=?, tanggal_pinjam=?, tanggal_balik=?, status=? WHERE id_peminjaman=?");

        stat.setInt(1, id_buku);
        stat.setInt(2, id_siswa);
        stat.setDate(3, tanggal_pinjam);

        // If tanggal_balik is null, set it as NULL in the database
        if (tanggal_balik != null) {
            stat.setDate(4, tanggal_balik);
        } else {
            stat.setNull(4, java.sql.Types.DATE);  // This sets NULL if no return date is selected
        }

        stat.setString(5, status);
        stat.setInt(6, id_peminjaman);  // Set the id_peminjaman for the WHERE clause

        // Execute the update query
        stat.executeUpdate();

        // Refresh tables and show success message
        refreshTablePinjam();
        refreshTableBalik();
        JOptionPane.showMessageDialog(null, "Data updated successfully!");
    } else {
        JOptionPane.showMessageDialog(null, "No matching peminjaman found for the given id_siswa and id_buku.");
    }

} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
}

    }//GEN-LAST:event_tombolgantibarangActionPerformed

    private void tombolsimpanbarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolsimpanbarangActionPerformed
        // TODO add your handling code here:
      try {
        // Get the selected item from the combo box (the string "id_siswa - nama - kelas")
        String selectedSiswa = (String) cbSiswa.getSelectedItem();
        String[] parts = selectedSiswa.split(" - ");
        int id_siswa = Integer.parseInt(parts[0]);

        String selectedBuku = (String) cbBuku.getSelectedItem();
        parts = selectedBuku.split(" - ");
        int id_buku = Integer.parseInt(parts[0]);

        // Convert the date from date pickers to java.sql.Date
        java.sql.Date tanggal_pinjam = new java.sql.Date(TPinjam.getDate().getTime());
        
        // Check if the TBalik (return date) is empty or null
        java.sql.Date tanggal_balik = null;
        if (TBalik.getDate() != null) {
            tanggal_balik = new java.sql.Date(TBalik.getDate().getTime());
        }

        // Prepare the SQL insert statement
        String status = (String) cbStatus.getSelectedItem();
        this.stat = k.getKoneksi().prepareStatement("INSERT INTO peminjaman (id_buku, id_siswa, tanggal_pinjam, tanggal_balik, status) VALUES (?, ?, ?, ?, ?)");

        stat.setInt(1, id_buku);
        stat.setInt(2, id_siswa);
        stat.setDate(3, tanggal_pinjam);

        // If tanggal_balik is null, set it as NULL in the database
        if (tanggal_balik != null) {
            stat.setDate(4, tanggal_balik);
        } else {
            stat.setNull(4, java.sql.Types.DATE);  // This sets NULL if no return date is selected
        }

        stat.setString(5, status);
        stat.executeUpdate();

        refreshTablePinjam();
        refreshTableBalik();
        JOptionPane.showMessageDialog(null, "Data saved successfully!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_tombolsimpanbarangActionPerformed

    private void TabelPinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelPinjamMouseClicked
        // TODO add your handling code here:
       try {
    // Get the selected row index
    int selectedRow = TabelPinjam.getSelectedRow(); // Get selected row index
    if (selectedRow != -1) {
        // Get values for the first and second combo box
        Object bukuValue = TabelPinjam.getValueAt(selectedRow, 1); // Second column (Name)
        Object siswaValue = TabelPinjam.getValueAt(selectedRow, 0); // Third column (Name)

        // Match and select item in cbBuku
        if (bukuValue != null) {
            String name = bukuValue.toString();
            for (int i = 0; i < cbBuku.getItemCount(); i++) {
                String item = cbBuku.getItemAt(i);
                // Check if the item contains the name after the first " - "
                String[] parts = item.split(" - ");
                if (parts.length > 1 && parts[1].equals(name)) {
                    cbBuku.setSelectedIndex(i); // Select the matching item
                    break;
                }
            }
        }

        // Match and select item in cbSiswa
        if (siswaValue != null) {
            String siswaName = siswaValue.toString();
            for (int i = 0; i < cbSiswa.getItemCount(); i++) {
                String item = cbSiswa.getItemAt(i);
                // Check if the item contains the name after the first " - "
                String[] parts = item.split(" - ");
                if (parts.length > 1 && parts[1].equals(siswaName)) {
                    cbSiswa.setSelectedIndex(i); // Select the matching item
                    break;
                }
            }
        }

        // Set the tanggal_pinjam (assume it's in column 2)
        String tanggalPinjamStr = TabelPinjam.getValueAt(selectedRow, 2).toString();
        java.util.Date tanggalPinjam = new SimpleDateFormat("yyyy-MM-dd").parse(tanggalPinjamStr);
        TPinjam.setDate(tanggalPinjam);

        // Set the status (assume it's in column 5)
        String status = TabelPinjam.getValueAt(selectedRow,3).toString();
        cbStatus.setSelectedItem(status);
    }
} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
}


    }//GEN-LAST:event_TabelPinjamMouseClicked

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
        // TODO add your handling code here:
        new DashPustakawan().show();
this.dispose();

    }//GEN-LAST:event_btn_logoutActionPerformed

    private void TabelBalikMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelBalikMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TabelBalikMouseClicked

    private void cbSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSiswaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSiswaActionPerformed

    private void cbBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBukuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBukuActionPerformed

    
    
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
            java.util.logging.Logger.getLogger(peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new peminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser TBalik;
    private com.toedter.calendar.JDateChooser TPinjam;
    private javax.swing.JTable TabelBalik;
    private javax.swing.JTable TabelPinjam;
    private javax.swing.JButton btn_logout;
    private javax.swing.JComboBox<String> cbBuku;
    private javax.swing.JComboBox<String> cbSiswa;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton tombolgantibarang;
    private javax.swing.JButton tombolsimpanbarang;
    // End of variables declaration//GEN-END:variables
}
