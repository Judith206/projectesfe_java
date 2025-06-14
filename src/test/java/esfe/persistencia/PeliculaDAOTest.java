package esfe.persistencia;

import org.junit.jupiter.api.BeforeEach; // Anotación para indicar que el método se ejecuta antes de cada método de prueba.
import org.junit.jupiter.api.Test;       // Anotación para indicar que el método es un caso de prueba.
import esfe.dominio.Pelicula;                // Clase que representa la entidad de usuario utilizada en las pruebas.

import java.util.ArrayList;              // Clase para crear listas dinámicas de objetos, utilizada en algunas pruebas.
import java.util.Random;                 // Clase para generar números aleatorios, útil para crear datos de prueba.

import java.sql.SQLException;             // Clase para manejar excepciones relacionadas con la base de datos, aunque no se espera que las pruebas unitarias interactúen directamente con ella (idealmente se mockean las dependencias).

import static org.junit.jupiter.api.Assertions.*; // Importación estática de métodos de aserción de JUnit 5 para verificar el comportamiento esperado en las pruebas.



class PeliculaDAOTest {
    private PeliculaDAO peliculaDAO; // Instancia de la clase UserDAO que se va a probar.

    @BeforeEach
    void setUp(){
        // Método que se ejecuta antes de cada método de prueba (@Test).
        // Su propósito es inicializar el entorno de prueba, en este caso,
        // creando una nueva instancia de UserDAO para cada prueba.
        peliculaDAO = new PeliculaDAO();
    }
    private Pelicula create(Pelicula pelicula) throws SQLException{
        // Llama al método 'create' del UserDAO para persistir el usuario en la base de datos (simulada).
        Pelicula res = peliculaDAO.create(pelicula);

        // Realiza aserciones para verificar que la creación del usuario fue exitosa
        // y que los datos del usuario retornado coinciden con los datos originales.
        assertNotNull(res, "El usuario creado no debería ser nulo."); // Verifica que el objeto retornado no sea nulo.
        assertEquals(pelicula.getTitulo(), res.getTitulo(), "El nombre del usuario creado debe ser igual al original.");
        assertEquals(pelicula.getDirector(), res.getDirector(), "El email del usuario creado debe ser igual al original.");
        assertEquals(pelicula.getGenero(), res.getGenero(), "El status del usuario creado debe ser igual al original.");

        // Retorna el objeto User creado (tal como lo devolvió el UserDAO).
        return res;
    }

    private void update(Pelicula pelicula) throws SQLException{
        // Modifica los atributos del objeto User para simular una actualización.
        pelicula.setTitulo(pelicula.getTitulo() + "_u"); // Añade "_u" al final del nombre.
        pelicula.setDirector(pelicula.getDirector() + "_u");
        pelicula.setGenero(pelicula.getGenero() + "_u");


        // Llama al método 'update' del UserDAO para actualizar el usuario en la base de datos (simulada).
        boolean res = peliculaDAO.update(pelicula);

        // Realiza una aserción para verificar que la actualización fue exitosa.
        assertTrue(res, "La actualización de la pelicula debería ser exitosa.");

        // Llama al método 'getById' para verificar que los cambios se persistieron correctamente.
        // Aunque el método 'getById' ya tiene sus propias aserciones, esta llamada adicional
        // ayuda a asegurar que la actualización realmente tuvo efecto en la capa de datos.
        getById(pelicula);
    }

    private void getById(Pelicula pelicula) throws SQLException {
        // Llama al método 'getById' del UserDAO para obtener un usuario por su ID.
        Pelicula res = peliculaDAO.getById(pelicula.getId());

        // Realiza aserciones para verificar que el usuario obtenido coincide
        // con el usuario original (o el usuario modificado en pruebas de actualización).
        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(pelicula.getId(), res.getId(), "El ID del usuario obtenido debe ser igual al original.");
        assertEquals(pelicula.getTitulo(), res.getTitulo(), "El nombre del usuario obtenido debe ser igual al esperado.");
        assertEquals(pelicula.getDirector(), res.getDirector(), "El email del usuario obtenido debe ser igual al esperado.");
        assertEquals(pelicula.getGenero(), res.getGenero(), "El status del usuario obtenido debe ser igual al esperado.");
    }

    private void search(Pelicula pelicula) throws SQLException {
        // Llama al método 'search' del UserDAO para buscar usuarios por nombre.
        ArrayList<Pelicula> peliculas = peliculaDAO.search(pelicula.getTitulo());
        boolean find = false; // Variable para rastrear si se encontró un usuario con el nombre buscado.

        // Itera sobre la lista de usuarios devuelta por la búsqueda.
        for (Pelicula peliculaItem : peliculas) {
            // Verifica si el nombre de cada usuario encontrado contiene la cadena de búsqueda.
            if (peliculaItem.getTitulo().contains(pelicula.getTitulo())) {
                find = true; // Si se encuentra una coincidencia, se establece 'find' a true.
            }
            else{
                find = false; // Si un nombre no contiene la cadena de búsqueda, se establece 'find' a false.
                break;      // Se sale del bucle, ya que se esperaba que todos los resultados contuvieran la cadena.
            }
        }

        // Realiza una aserción para verificar que todos los usuarios con el nombre buscado fue encontrado.
        assertTrue(find, "el nombre buscado no fue encontrado : " + pelicula.getTitulo());
    }

    private void delete(Pelicula pelicula) throws SQLException{
        // Llama al método 'delete' del UserDAO para eliminar un usuario por su ID.
        boolean res = peliculaDAO.delete(pelicula);

        // Realiza una aserción para verificar que la eliminación fue exitosa.
        assertTrue(res, "La eliminación de la pelicula debería ser exitosa.");

        // Intenta obtener el usuario por su ID después de la eliminación.
        Pelicula res2 = peliculaDAO.getById(pelicula.getId());

        // Realiza una aserción para verificar que el usuario ya no existe en la base de datos
        // después de la eliminación (el método 'getById' debería retornar null).
        assertNull(res2, "El usuario debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testPeliculaDAO() throws SQLException {
        // Crea una instancia de la clase Random para generar datos de prueba aleatorios.
        Random random = new Random();
        // Genera un número aleatorio entre 1 y 1000 para asegurar la unicidad del email en cada prueba.
        int num = random.nextInt(1000) + 1;
        // Define una cadena base para el email y le concatena el número aleatorio generado.

        // Crea un nuevo objeto User con datos de prueba. El ID se establece en 0 ya que será generado por la base de datos.
        Pelicula pelicula = new Pelicula(0, "Test Pelicula", "director","genero");

        // Llama al método 'create' para persistir el usuario de prueba en la base de datos (simulada) y verifica su creación.
        Pelicula testPelicula = create(pelicula);

        // Llama al método 'update' para modificar los datos del usuario de prueba y verifica la actualización.
        update(testPelicula);

        // Llama al método 'search' para buscar usuarios por el nombre del usuario de prueba y verifica que se encuentre.
        search(testPelicula);


        // Llama al método 'delete' para eliminar el usuario de prueba de la base de datos y verifica la eliminación.
        delete(testPelicula);
    }
    @Test
    void createPelicula() throws SQLException {
        Pelicula pelicula = new Pelicula(0, "loros", "pedro", "literario");
        Pelicula res = peliculaDAO.create(pelicula);
        assertNotEquals(res,null);
    }
}