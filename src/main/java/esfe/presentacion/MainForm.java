package esfe.presentacion;

import javax.swing.*; // Importa el paquete javax.swing, que proporciona clases para crear interfaces gráficas de usuario (GUI) en Java Swing (como JFrame, JPanel, JButton, JLabel, JTextField, JPasswordField, JOptionPane, etc.).
import esfe.dominio.User; // Importa la clase User desde el paquete esfe.dominio. Esta clase  representa la entidad de usuario con sus atributos (id, nombre, email, contraseña, estado, etc.).

public class MainForm extends JFrame {

    private User userAutenticate; // Declaración de una variable de instancia llamada 'userAutenticate' de tipo User. Esta variable se utilizará para almacenar la información del usuario que ha sido autenticado en el sistema.

    public User getUserAutenticate() {
        return userAutenticate;
    }

    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    public MainForm(){
        setTitle("Sistema en java de escritorio"); // Establece el título de la ventana principal (JFrame).
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la operación por defecto al cerrar la ventana para que la aplicación se termine.
        setLocationRelativeTo(null); // Centra la ventana principal en la pantalla.
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicializa la ventana principal en estado maximizado, ocupando toda la pantalla.
        createMenu(); // Llama al método 'createMenu()' para crear y agregar la barra de menú a la ventana principal.
    }

    private void createMenu() {
        // Barra de menú
        JMenuBar menuBar = new JMenuBar(); // Crea una nueva barra de menú.
        setJMenuBar(menuBar); // Establece la barra de menú creada como la barra de menú de este JFrame (MainForm).

        JMenu menuPerfil = new JMenu("Perfil"); // Crea un nuevo menú llamado "Perfil".
        menuBar.add(menuPerfil); // Agrega el menú "Perfil" a la barra de menú.

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña"); // Crea un nuevo elemento de menú llamado "Cambiar contraseña".
        menuPerfil.add(itemChangePassword); // Agrega el elemento "Cambiar contraseña" al menú "Perfil".
        itemChangePassword.addActionListener(e -> { // Agrega un ActionListener al elemento "Cambiar contraseña".
            ChangePasswordForm changePassword = new ChangePasswordForm(this); // Cuando se hace clic, crea una nueva instancia de ChangePasswordForm, pasándole la instancia actual de MainForm como padre.
            changePassword.setVisible(true); // Hace visible la ventana de cambio de contraseña.

        });


        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario"); // Crea un nuevo elemento de menú llamado "Cambiar de usuario".
        menuPerfil.add(itemChangeUser); // Agrega el elemento "Cambiar de usuario" al menú "Perfil".
        itemChangeUser.addActionListener(e -> { // Agrega un ActionListener al elemento "Cambiar de usuario".
            LoginForm loginForm = new LoginForm(this); // Cuando se hace clic, crea una nueva instancia de LoginForm (ventana de inicio de sesión), pasándole la instancia actual de MainForm como padre.
            loginForm.setVisible(true); // Hace visible la ventana de inicio de sesión.
        });


        JMenuItem itemSalir = new JMenuItem("Salir"); // Crea un nuevo elemento de menú llamado "Salir".
        menuPerfil.add(itemSalir); // Agrega el elemento "Salir" al menú "Perfil".
        itemSalir.addActionListener(e -> System.exit(0)); // Agrega un ActionListener al elemento "Salir". Cuando se hace clic, termina la ejecución de la aplicación (cierra la JVM).


        // Menú "Matenimiento"
        JMenu menuMantenimiento = new JMenu("Mantenimientos"); // Crea un nuevo menú llamado "Mantenimientos".
        menuBar.add(menuMantenimiento); // Agrega el menú "Mantenimientos" a la barra de menú.

        JMenuItem itemUsers = new JMenuItem("Usuarios"); // Crea un nuevo elemento de menú llamado "Usuarios".
        menuMantenimiento.add(itemUsers); // Agrega el elemento "Usuarios" al menú "Mantenimientos".
        itemUsers.addActionListener(e -> { // Agrega un ActionListener al elemento "Usuarios".
            UserReadingForm userReadingForm=new UserReadingForm(this); // Cuando se hace clic, crea una nueva instancia de UserReadingForm (formulario para leer/listar usuarios), pasándole la instancia actual de MainForm como padre.
            userReadingForm.setVisible(true); // Hace visible el formulario de lectura de usuarios.
        });

    }
}
