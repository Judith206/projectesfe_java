package esfe.presentacion;

import esfe.dominio.Pelicula;
import esfe.persistencia.PeliculaDAO;
import esfe.utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PeliculaReadingForm extends JDialog{
    private JPanel mainPanel;
    private JTextField textTitulo;
    private JButton btnAgre;
    private JTable tablePelicula;
    private JButton btnUpdate;
    private JButton btnDelete;

    private PeliculaDAO peliculaDAO; // Instancia de PeliculaDAO para realizar operaciones de base de datos de Peliculas.
    private MainForm mainForm; // Referencia a la ventana principal de la aplicación.

    // Constructor de la clase PeliculaReadingForm. Recibe una instancia de MainForm como parámetro.
    public PeliculaReadingForm(MainForm mainForm) {
        super(mainForm);
        this.mainForm = mainForm; // Asigna la instancia de MainForm recibida a la variable local.
        peliculaDAO = new PeliculaDAO(); // Crea una nueva instancia de PeliculaDAO al instanciar este formulario.
        setContentPane(mainPanel); // Establece el panel principal como el contenido de este diálogo.
        setModal(true); // Hace que este diálogo sea modal, bloqueando la interacción con la ventana principal hasta que se cierre.
        setTitle("Buscar pelicula"); // Establece el título de la ventana del diálogo.
        pack(); // Ajusta el tamaño de la ventana para que todos sus componentes se muestren correctamente.
        setLocationRelativeTo(mainForm); // Centra la ventana del diálogo relative a la ventana principal.

        // Agrega un listener de teclado al campo de texto txtNombre.
        textTitulo.addKeyListener(new KeyAdapter() {
            // Sobrescribe el método keyReleased, que se llama cuando se suelta una tecla.
            @Override
            public void keyReleased(KeyEvent e) {
                // Verifica si el campo de texto txtNombre no está vacío.
                if (!textTitulo.getText().trim().isEmpty()) {
                    // Llama al método search para buscar Peliculas según el texto ingresado.
                    search(textTitulo.getText());
                } else {
                    // Si el campo de texto está vacío, crea un modelo de tabla vacío y lo asigna a la tabla de Peliculas para limpiarla.
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    tablePelicula.setModel(emptyModel);
                }
            }
        });

        // Agrega un ActionListener al botón btnCreate.
        btnAgre.addActionListener(s -> {
            // Crea una nueva instancia de PeliculaWriteForm para la creación de un nuevo pelicula, pasando la MainForm, la constante CREATE de CUD y un nuevo objeto User vacío.
            PeliculaWriteForm peliculaWriteForm = new PeliculaWriteForm(this.mainForm, CUD.CREATE, new Pelicula());
            // Hace visible el formulario de escritura de pelicula.
            peliculaWriteForm.setVisible(true);
            // Limpia la tabla de Peliculas creando y asignando un modelo de tabla vacío  para refrescar la lista después de la creación.
            DefaultTableModel emptyModel = new DefaultTableModel();
            tablePelicula.setModel(emptyModel);
        });

        // Agrega un ActionListener al botón btnUpdate.
        btnUpdate.addActionListener(s -> {
            // Llama al método getUserFromTableRow para obtener el pelicula seleccionado en la tabla.
            Pelicula pelicula = getPeliculaFromTableRow();
            // Verifica si se seleccionó un pelicula en la tabla (getUserFromTableRow no devolvió null).
            if (pelicula != null) {
                // Crea una nueva instancia de UserWriteForm para la actualización del pelicula seleccionado, pasando la MainForm, la constante UPDATE de CUD y el objeto User obtenido.
                PeliculaWriteForm peliculaWriteForm = new PeliculaWriteForm(this.mainForm, CUD.UPDATE, pelicula);
                // Hace visible el formulario de escritura de pelicula.
                peliculaWriteForm.setVisible(true);
                // Limpia la tabla de Peliculas creando y asignando un modelo de tabla vacío para refrescar la lista después de la actualización.
                DefaultTableModel emptyModel = new DefaultTableModel();
                tablePelicula.setModel(emptyModel);
            }
        });

        // Agrega un ActionListener al botón btnEliminar.
        btnDelete.addActionListener(s -> {
            // Llama al método getUserFromTableRow para obtener el pelicula seleccionado en la tabla.
            Pelicula pelicula = getPeliculaFromTableRow();
            // Verifica si se seleccionó un pelicula en la tabla (getUserFromTableRow no devolvió null).
            if (pelicula != null) {
                // Crea una nueva instancia de UserWriteForm para la eliminación del pelicula seleccionado, pasando la MainForm, la constante DELETE de CUD y el objeto User obtenido.
                PeliculaWriteForm peliculaWriteForm = new PeliculaWriteForm(this.mainForm, CUD.DELETE, pelicula);
                // Hace visible el formulario de escritura de pelicula.
                peliculaWriteForm.setVisible(true);
                // Limpia la tabla de Peliculas creando y asignando un modelo de tabla vacío  para refrescar la lista después de la eliminación.
                DefaultTableModel emptyModel = new DefaultTableModel();
                tablePelicula.setModel(emptyModel);
            }
        });
    }
    private void search(String query) {
        try {
            // Llama al método 'search' del PeliculaDAO para buscar Peliculas cuya información
            // coincida con la cadena de búsqueda 'query'. La implementación específica
            ArrayList<Pelicula> peliculas = peliculaDAO.search(query);
            // Llama al método 'createTable' para actualizar la tabla de Peliculas
            // en la interfaz gráfica con los resultados de la búsqueda.
            createTable(peliculas);
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso de búsqueda
            // (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error al pelicula.
            return; // Sale del método 'search' después de mostrar el error.
        }
    }
    public void createTable(ArrayList<Pelicula> peliculas) {

        // Crea un nuevo modelo de tabla por defecto (DefaultTableModel).
        // Se sobrescribe el método isCellEditable para hacer que todas las celdas de la tabla no sean editables.
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Retorna false para indicar que ninguna celda debe ser editable.
            }
        };

        // Define las columnas de la tabla. Los nombres de las columnas corresponden
        // a los atributos que se mostrarán de cada objeto User.
        model.addColumn("Id");
        model.addColumn("Titulo");
        model.addColumn("Director");
        model.addColumn("Genero");

        // Establece el modelo de tabla creado como el modelo de datos para la
        // JTable 'tableUsers' (la tabla que se muestra en la interfaz gráfica).
        this.tablePelicula.setModel(model);

        // Declara un array de objetos 'row' que se utilizará temporalmente para agregar filas.
        Object row[] = null;

        // Itera a través de la lista de objetos User proporcionada.
        for (int i = 0; i < peliculas.size(); i++) {
            // Obtiene el objeto User actual de la lista.
            Pelicula pelicula = peliculas.get(i);
            // Agrega una nueva fila vacía al modelo de la tabla.
            model.addRow(row);
            // Establece el valor del ID del pelicula en la celda correspondiente de la fila actual (columna 0).
            model.setValueAt(pelicula.getId(), i, 0);
            // Establece el valor del nombre del pelicula en la celda correspondiente de la fila actual (columna 1).
            model.setValueAt(pelicula.getTitulo(), i, 1);
            // Establece el valor del email del pelicula en la celda correspondiente de la fila actual (columna 2).
            model.setValueAt(pelicula.getDirector(), i, 2);
            // Establece el valor del estatus del pelicula (probablemente obtenido a través de un método 'getStrEstatus()')
            // en la celda correspondiente de la fila actual (columna 3).
            model.setValueAt(pelicula.getGenero(), i, 3);
        }

        // Llama al método 'hideCol' para ocultar la columna con índice 0 (la columna del ID).
        // Esto es común cuando el ID es necesario internamente pero no se quiere mostrar al pelicula.
        hideCol(0);
    }

    private void hideCol(int pColumna) {
        // Obtiene el modelo de columnas de la JTable y establece el ancho máximo de la columna especificada a 0.
        // Esto hace que la columna no sea visible en la vista de datos de la tabla.
        this.tablePelicula.getColumnModel().getColumn(pColumna).setMaxWidth(0);
        // Establece el ancho mínimo de la columna especificada a 0.
        // Esto asegura que la columna no ocupe espacio incluso si el layout manager intenta ajustarla.
        this.tablePelicula.getColumnModel().getColumn(pColumna).setMinWidth(0);
        // Realiza las mismas operaciones para el encabezado de la tabla.
        // Esto asegura que el nombre de la columna también se oculte y no ocupe espacio en la parte superior de la tabla.
        this.tablePelicula.getTableHeader().getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tablePelicula.getTableHeader().getColumnModel().getColumn(pColumna).setMinWidth(0);
    }

    // Método privado para obtener el objeto User seleccionado de la fila de la tabla.
    private Pelicula getPeliculaFromTableRow() {
        Pelicula pelicula = null; // Inicializa la variable user a null.
        try {
            // Obtiene el índice de la fila seleccionada en la tabla.
            int filaSelect = this.tablePelicula.getSelectedRow();
            int id = 0; // Inicializa la variable id a 0.

            // Verifica si se ha seleccionado alguna fila en la tabla.
            if (filaSelect != -1) {
                // Si se seleccionó una fila, obtiene el valor de la primera columna  ID de esa fila.
                id = (int) this.tablePelicula.getValueAt(filaSelect, 0);
            } else {
                // Si no se seleccionó ninguna fila, muestra un mensaje de advertencia al pelicula.
                JOptionPane.showMessageDialog(null,
                        "Seleccionar una fila de la tabla.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null; // Retorna null ya que no se puede obtener un pelicula sin una fila seleccionada.
            }

            // Llama al método 'getById' del PeliculaDAO para obtener el objeto User correspondiente al ID obtenido de la tabla.
            pelicula = peliculaDAO.getById(id);

            // Verifica si se encontró un pelicula con el ID proporcionado.
            if (pelicula.getId() == 0) {
                // Si el ID del pelicula devuelto es 0 (o alguna otra indicación de que no se encontró),
                // muestra un mensaje de advertencia al pelicula.
                JOptionPane.showMessageDialog(null,
                        "No se encontró ningún pelicula.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null; // Retorna null ya que no se encontró ningún pelicula con ese ID.
            }

            // Si se encontró una pelicula, lo retorna.
            return pelicula;
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error al pelicula con la descripción de la excepción.
            return null; // Retorna null en caso de error.
        }
    }
}


