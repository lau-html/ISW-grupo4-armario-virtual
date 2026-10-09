package icai.dtc.isw.ui;

import icai.dtc.isw.controler.GestorUsuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistro extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnRegistrar;
    private JButton btnVolverLogin;
    private GestorUsuarios gestorUsuarios;

    public VentanaRegistro() {
        gestorUsuarios = new GestorUsuarios();

        setTitle(" Dress Better - Registro de Usuario - Armario Virtual");
        setSize(400, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblEmail = new JLabel("Correo electrónico:");
        txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField();

        btnRegistrar = new JButton("Registrarse");
        btnVolverLogin = new JButton("Volver al Login");

        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnVolverLogin);
        panel.add(btnRegistrar);

        add(panel);

        // Registrar usuario
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaRegistro.this,
                            "Por favor, rellena todos los campos.",
                            "Campos incompletos",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    boolean registrado = gestorUsuarios.registrar(email, password);
                    if (registrado) {
                        JOptionPane.showMessageDialog(VentanaRegistro.this,
                                "¡Usuario registrado con éxito!",
                                "Registro Completado",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new VentanaLogin().setVisible(true);
                    }
                } catch (IllegalArgumentException ex) {
                    // Correo duplicado
                    JOptionPane.showMessageDialog(VentanaRegistro.this,
                            ex.getMessage(),
                            "Error de Registro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaRegistro.this,
                            "Error de conexión con la base de datos: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción: Volver al Login
        btnVolverLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VentanaLogin().setVisible(true);
            }
        });
    }
}