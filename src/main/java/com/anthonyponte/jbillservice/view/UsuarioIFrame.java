/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.anthonyponte.jbillservice.view;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

/**
 *
 * @author anthony
 */
public class UsuarioIFrame extends JInternalFrame {

    /**
     * Creates new form UserIFrame
     */
    public UsuarioIFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbed = new JTabbedPane();
        pnlFirmaDigital = new JPanel();
        lblFirmaJks = new JLabel();
        btnFirmaJks = new JButton();
        tfFirmaJks = new JTextField();
        lblFirmaUsuario = new JLabel();
        tfFirmaUsuario = new JTextField();
        lblFirmaContrasena = new JLabel();
        tfFirmaContrasena = new JPasswordField();
        pnlClaveSol = new JPanel();
        lblRuc = new JLabel();
        lblRazonSocial = new JLabel();
        tfRazonSocial = new JTextField();
        lblClaveSolUsuario = new JLabel();
        tfClaveSolUsuario = new JTextField();
        lblClaveSolContrasena = new JLabel();
        tfClaveSolContrasena = new JPasswordField();
        tfRuc = new JTextField();
        separator = new JSeparator();
        cbRecordar = new JCheckBox();
        btnEntrar = new JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Entrar");
        setMaximumSize(null);
        setMinimumSize(null);
        setName(""); // NOI18N

        lblFirmaJks.setFont(lblFirmaJks.getFont().deriveFont(lblFirmaJks.getFont().getStyle() | Font.BOLD, lblFirmaJks.getFont().getSize()-2));
        lblFirmaJks.setText("JKS");

        btnFirmaJks.setMaximumSize(new Dimension(30, 30));
        btnFirmaJks.setMinimumSize(new Dimension(30, 30));
        btnFirmaJks.setPreferredSize(new Dimension(30, 30));

        tfFirmaJks.setMaximumSize(null);
        tfFirmaJks.setMinimumSize(null);
        tfFirmaJks.setName(""); // NOI18N
        tfFirmaJks.setPreferredSize(new Dimension(300, 30));

        lblFirmaUsuario.setFont(lblFirmaUsuario.getFont().deriveFont(lblFirmaUsuario.getFont().getStyle() | Font.BOLD, lblFirmaUsuario.getFont().getSize()-2));
        lblFirmaUsuario.setText("Usuario");

        tfFirmaUsuario.setMaximumSize(null);
        tfFirmaUsuario.setMinimumSize(null);
        tfFirmaUsuario.setName(""); // NOI18N
        tfFirmaUsuario.setPreferredSize(new Dimension(300, 30));

        lblFirmaContrasena.setFont(lblFirmaContrasena.getFont().deriveFont(lblFirmaContrasena.getFont().getStyle() | Font.BOLD, lblFirmaContrasena.getFont().getSize()-2));
        lblFirmaContrasena.setLabelFor(tfClaveSolContrasena);
        lblFirmaContrasena.setText("Contraseña");

        tfFirmaContrasena.setMaximumSize(null);
        tfFirmaContrasena.setMinimumSize(null);
        tfFirmaContrasena.setName(""); // NOI18N
        tfFirmaContrasena.setPreferredSize(new Dimension(300, 30));

        GroupLayout pnlFirmaDigitalLayout = new GroupLayout(pnlFirmaDigital);
        pnlFirmaDigital.setLayout(pnlFirmaDigitalLayout);
        pnlFirmaDigitalLayout.setHorizontalGroup(pnlFirmaDigitalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlFirmaDigitalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFirmaDigitalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFirmaDigitalLayout.createSequentialGroup()
                        .addComponent(btnFirmaJks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfFirmaJks, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(tfFirmaUsuario, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfFirmaContrasena, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFirmaJks)
                    .addComponent(lblFirmaContrasena)
                    .addComponent(lblFirmaUsuario))
                .addContainerGap())
        );
        pnlFirmaDigitalLayout.setVerticalGroup(pnlFirmaDigitalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlFirmaDigitalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFirmaJks)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFirmaDigitalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(btnFirmaJks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfFirmaJks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFirmaUsuario)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfFirmaUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFirmaContrasena)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfFirmaContrasena, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbed.addTab("Firma Digital", pnlFirmaDigital);

        lblRuc.setFont(lblRuc.getFont().deriveFont(lblRuc.getFont().getStyle() | Font.BOLD, lblRuc.getFont().getSize()-2));
        lblRuc.setText("RUC");

        lblRazonSocial.setFont(lblRazonSocial.getFont().deriveFont(lblRazonSocial.getFont().getStyle() | Font.BOLD, lblRazonSocial.getFont().getSize()-2));
        lblRazonSocial.setText("Razon Social");

        tfRazonSocial.setMaximumSize(null);
        tfRazonSocial.setMinimumSize(null);
        tfRazonSocial.setName(""); // NOI18N
        tfRazonSocial.setPreferredSize(new Dimension(300, 30));

        lblClaveSolUsuario.setFont(lblClaveSolUsuario.getFont().deriveFont(lblClaveSolUsuario.getFont().getStyle() | Font.BOLD, lblClaveSolUsuario.getFont().getSize()-2));
        lblClaveSolUsuario.setText("Usuario");

        tfClaveSolUsuario.setMaximumSize(null);
        tfClaveSolUsuario.setMinimumSize(null);
        tfClaveSolUsuario.setName(""); // NOI18N
        tfClaveSolUsuario.setPreferredSize(new Dimension(300, 30));

        lblClaveSolContrasena.setFont(lblClaveSolContrasena.getFont().deriveFont(lblClaveSolContrasena.getFont().getStyle() | Font.BOLD, lblClaveSolContrasena.getFont().getSize()-2));
        lblClaveSolContrasena.setLabelFor(tfClaveSolContrasena);
        lblClaveSolContrasena.setText("Contraseña");

        tfClaveSolContrasena.setMaximumSize(null);
        tfClaveSolContrasena.setMinimumSize(null);
        tfClaveSolContrasena.setName(""); // NOI18N
        tfClaveSolContrasena.setPreferredSize(new Dimension(300, 30));

        tfRuc.setMaximumSize(null);
        tfRuc.setMinimumSize(null);
        tfRuc.setPreferredSize(new Dimension(300, 30));

        GroupLayout pnlClaveSolLayout = new GroupLayout(pnlClaveSol);
        pnlClaveSol.setLayout(pnlClaveSolLayout);
        pnlClaveSolLayout.setHorizontalGroup(pnlClaveSolLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlClaveSolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlClaveSolLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblRazonSocial)
                    .addComponent(lblRuc)
                    .addComponent(lblClaveSolContrasena)
                    .addComponent(lblClaveSolUsuario)
                    .addComponent(tfRuc, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfRazonSocial, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfClaveSolUsuario, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfClaveSolContrasena, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlClaveSolLayout.setVerticalGroup(pnlClaveSolLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlClaveSolLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRuc)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfRuc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRazonSocial)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfRazonSocial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblClaveSolUsuario)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfClaveSolUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblClaveSolContrasena)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfClaveSolContrasena, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbed.addTab("Clave SOL", pnlClaveSol);

        cbRecordar.setText("Recordar");
        cbRecordar.setMaximumSize(new Dimension(150, 30));
        cbRecordar.setMinimumSize(new Dimension(150, 30));
        cbRecordar.setName(""); // NOI18N
        cbRecordar.setPreferredSize(new Dimension(150, 30));

        btnEntrar.setText("Entrar");
        btnEntrar.setName(""); // NOI18N
        btnEntrar.setPreferredSize(new Dimension(300, 30));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(cbRecordar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntrar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabbed, GroupLayout.Alignment.TRAILING)
                    .addComponent(separator, GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbRecordar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntrar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton btnEntrar;
    public JButton btnFirmaJks;
    public JCheckBox cbRecordar;
    public JLabel lblClaveSolContrasena;
    public JLabel lblClaveSolUsuario;
    public JLabel lblFirmaContrasena;
    public JLabel lblFirmaJks;
    public JLabel lblFirmaUsuario;
    public JLabel lblRazonSocial;
    public JLabel lblRuc;
    public JPanel pnlClaveSol;
    public JPanel pnlFirmaDigital;
    public JSeparator separator;
    public JTabbedPane tabbed;
    public JPasswordField tfClaveSolContrasena;
    public JTextField tfClaveSolUsuario;
    public JPasswordField tfFirmaContrasena;
    public JTextField tfFirmaJks;
    public JTextField tfFirmaUsuario;
    public JTextField tfRazonSocial;
    public JTextField tfRuc;
    // End of variables declaration//GEN-END:variables
}
