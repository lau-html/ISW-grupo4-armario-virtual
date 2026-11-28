package icai.dtc.isw.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import icai.dtc.isw.controler.GestorUsuarios;
import icai.dtc.isw.domain.Usuario;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnIrRegistro;
    private GestorUsuarios gestorUsuarios;

    public VentanaLogin() {
        gestorUsuarios = new GestorUsuarios();

        setTitle("dressB - Inicio de sesión");
        setSize(400, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        panel.setBackground(Tema.CREMA); // fondo crema de la app
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblEmail = new JLabel("Correo electrónico:");
        lblEmail.setFont(Tema.getInstancia().fuenteNegrita); // letra en negrita de tema
        lblEmail.setForeground(Tema.BURDEOS); // texto burdeos
        txtEmail = new JTextField();
        txtEmail.setFont(Tema.getInstancia().fuenteNormal); // letra normal de Tema

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(Tema.getInstancia().fuenteNegrita);
        lblPassword.setForeground(Tema.BURDEOS);
        txtPassword = new JPasswordField();
        txtPassword.setFont(Tema.getInstancia().fuenteNormal);

        btnLogin = Tema.getInstancia().crearBoton("Iniciar Sesión"); // boton burdeos de Tema
        btnIrRegistro = Tema.getInstancia().crearBoton("Crear Cuenta");

        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnIrRegistro);
        panel.add(btnLogin);

        add(panel);

        // Boton Iniciar Sesion: llama al metodo general de validacion
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        // Permitir iniciar sesión al pulsar ENTER en el campo de texto del email
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
        });

        // Permitir iniciar sesión al pulsar ENTER en el campo de contraseña
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
        });

        // Ir a la pantalla de Registro
        btnIrRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VentanaRegistro().setVisible(true);
            }
        });
    }

    // Método centralizado para validar campos y ejecutar el login
    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(VentanaLogin.this,
                    "Introduce correo y contraseña.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = gestorUsuarios.login(email, password);
            if (usuario != null) {
                // Guardar usuario en sesión global para el aislamiento del armario
                SesionUsuario.setUsuarioLogueado(usuario);

                JOptionPane.showMessageDialog(VentanaLogin.this,
                        "Bienvenido/a, " + usuario.getEmail(),
                        "Sesión iniciada",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Cerrar ventana de login

                // Abrir la ventana principal de prendas (JVentana)
                new JVentana().setVisible(true);
            }
        } catch (IllegalArgumentException ex) {
            // Con las credenciales erróneas se muestra un error
            JOptionPane.showMessageDialog(VentanaLogin.this,
                    ex.getMessage(),
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(VentanaLogin.this,
                    "Error de conexión: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo main para probar la ventana
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin().setVisible(true);
        });
    }
}