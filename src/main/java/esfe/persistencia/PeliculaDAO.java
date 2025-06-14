package esfe.persistencia;

import java.sql.PreparedStatement; // Clase para ejecutar consultas SQL preparadas, previniendo inyecciones SQL.
import java.sql.ResultSet;        // Interfaz para representar el resultado de una consulta SQL.
import java.sql.SQLException;     // Clase para manejar errores relacionados con la base de datos SQL.
import java.util.ArrayList;       // Clase para crear listas dinámicas de objetos.

import esfe.dominio.Pelicula;        // Clase que representa la entidad de pelicula en el dominio de la aplicación.


public class PeliculaDAO {
    private ConnectionManager conn; // Objeto para gestionar la conexión con la base de datos.
    private PreparedStatement ps;   // Objeto para ejecutar consultas SQL preparadas.
    private ResultSet rs;           // Objeto para almacenar el resultado de una consulta SQL.

    public PeliculaDAO(){
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nueva la pelicula en la base de datos.
     *
     * @param pelicula El objeto pelicula que contiene la información de la nueva pelicula a crear.



     * o null si ocurre algún error durante la creación.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la creación de la pelicula.
     */
    public Pelicula create(Pelicula pelicula) throws SQLException {
        Pelicula res = null; // Variable para almacenar el la pelicula creado que se retornará.
        try{
            // Preparar la sentencia SQL para la inserción de un nuevo la pelicula.
            // Se especifica que se retornen las claves generadas automáticamente.
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO " +
                            "Pelicula (titulo, director, genero)" +
                            "VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, pelicula.getTitulo()); // Asignar el nombre del la pelicula.
            ps.setString(2, pelicula.getDirector()); // Hashear la contraseña antes de guardarla.
            ps.setString(3, pelicula.getGenero()); // Asignar el correo electrónico del la pelicula.

            // Ejecutar la sentencia de inserción y obtener el número de filas afectadas.
            int affectedRows = ps.executeUpdate();

            // Verificar si la inserción fue exitosa (al menos una fila afectada).
            if (affectedRows != 0) {
                // Obtener las claves generadas automáticamente por la base de datos (en este caso, el ID).
                ResultSet  generatedKeys = ps.getGeneratedKeys();
                // Mover el cursor al primer resultado (si existe).
                if (generatedKeys.next()) {
                    // Obtener el ID generado. Generalmente la primera columna contiene la clave primaria.
                    int idGenerado= generatedKeys.getInt(1);
                    // Recuperar el la pelicula completo utilizando el ID generado.
                    res = getById(idGenerado);
                } else {
                    // Lanzar una excepción si la creación de la pelicula falló y no se obtuvo un ID.
                    throw new SQLException("Creating pelicula failed, no ID obtained.");
                }
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al crear el pelicula: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return res; // Retornar  la pelicula creado (con su ID asignado) o null si hubo un error.
    }

    /**
     * Actualiza la información de la pelicula existente en la base de datos.
     *
     * @param pelicula El objeto de la Pelicula que contiene la información actualizada de la pelicula.
     * Se requiere que el objeto de la Pelicula tenga los campos 'id', 'name', 'email' y 'status'
     * correctamente establecidos para realizar la actualización.
     * @return true si la actualización del pelicula fue exitosa (al menos una fila afectada),
     * false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la actualización de la pelicula.
     */
    public boolean update(Pelicula pelicula) throws SQLException{
        boolean res = false; // Variable para indicar si la actualización fue exitosa.
        try{
            // Preparar la sentencia SQL para actualizar la información de una pelicula.
            ps = conn.connect().prepareStatement(
                    "UPDATE Pelicula " +
                            "SET titulo = ?, director = ?, genero = ? " +
                            "WHERE id = ?"
            );

            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, pelicula.getTitulo());  // Asignar el nuevo nombre de la pelicula.
            ps.setString(2, pelicula.getDirector()); // Asignar el nuevo correo electrónico del Pelicula.
            ps.setString(3, pelicula.getGenero());    // Asignar el nuevo estado del Pelicula.
            ps.setInt(4, pelicula.getId());       // Establecer la condición WHERE para identificar el Pelicula a actualizar por su ID.

            // Ejecutar la sentencia de actualización y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la actualización fue exitosa.
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al modificar el Pelicula: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return res; // Retornar el resultado de la operación de actualización.
    }

    /**
     * Elimina un Pelicula de la base de datos basándose en su ID.
     *
     * @param pelicula El objeto de la Pelicula que contiene el ID del Pelicula a eliminar.
     * Se requiere que el objeto de la Pelicula tenga el campo 'id' correctamente establecido.
     * @return true si la eliminación del Pelicula fue exitosa (al menos una fila afectada),
     * false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la eliminación del Pelicula.
     */
    public boolean delete(Pelicula pelicula) throws SQLException{
        boolean res = false; // Variable para indicar si la eliminación fue exitosa.
        try{
            // Preparar la sentencia SQL para eliminar un Pelicula por su ID.
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Pelicula WHERE id = ?"
            );
            // Establecer el valor del parámetro en la sentencia preparada (el ID del Pelicula a eliminar).
            ps.setInt(1, pelicula.getId());

            // Ejecutar la sentencia de eliminación y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la eliminación fue exitosa.
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al eliminar el Pelicula: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return res; // Retornar el resultado de la operación de eliminación.
    }

    /**
     * Busca Peliculas en la base de datos cuyo nombre contenga la cadena de búsqueda proporcionada.
     * La búsqueda se realiza de forma parcial, es decir, si el nombre del Pelicula contiene
     * la cadena de búsqueda (ignorando mayúsculas y minúsculas), será incluido en los resultados.
     *
     * @param titulo La cadena de texto a buscar dentro de los nombres de los Peliculas.
     * @return Un ArrayList de objetos de la Pelicula que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran Peliculas con el nombre especificado.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la búsqueda de Peliculas.
     */
    public ArrayList<Pelicula> search(String titulo) throws SQLException{
        ArrayList<Pelicula> records  = new ArrayList<>(); // Lista para almacenar los Peliculas encontrados.

        try {
            // Preparar la sentencia SQL para buscar Peliculas por nombre (usando LIKE para búsqueda parcial).
            ps = conn.connect().prepareStatement("SELECT id, titulo, director, genero " +
                    "FROM Pelicula " +
                    "WHERE titulo LIKE ?");

            // Establecer el valor del parámetro en la sentencia preparada.
            // El '%' al inicio y al final permiten la búsqueda de la cadena 'name' en cualquier parte del nombre del Pelicula.
            ps.setString(1, "%" + titulo + "%");

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Iterar a través de cada fila del resultado.
            while (rs.next()){
                // Crear un nuevo objeto de la Pelicula para cada registro encontrado.
                Pelicula pelicula = new Pelicula();
                // Asignar los valores de las columnas a los atributos del objeto de la Pelicula.
                pelicula.setId(rs.getInt(1));       // Obtener el ID del Pelicula.
                pelicula.setTitulo(rs.getString(2));   // Obtener el nombre del Pelicula.
                pelicula.setDirector(rs.getString(3));  // Obtener el correo electrónico del Pelicula.
                pelicula.setGenero(rs.getString(4));    // Obtener el estado del Pelicula.
                // Agregar el objeto de la Pelicula a la lista de resultados.
                records.add(pelicula);
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al buscar pelicula: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return records; // Retornar la lista de Peliculas encontrados.
    }

    /**
     * Obtiene un Pelicula de la base de datos basado en su ID.
     *
     * @param id El ID del Pelicula que se desea obtener.
     * @return Un objeto de la Pelicula si se encuentra un Pelicula con el ID especificado,
     * null si no se encuentra ningún Pelicula con ese ID.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la obtención del Pelicula.
     */
    public Pelicula getById(int id) throws SQLException{
        Pelicula pelicula  = new Pelicula(); // Inicializar un objeto de la Pelicula que se retornará.

        try {
            // Preparar la sentencia SQL para seleccionar un Pelicula por su ID.
            ps = conn.connect().prepareStatement("SELECT id, titulo, director, genero " +
                    "FROM Pelicula " +
                    "WHERE id = ?");

            // Establecer el valor del parámetro en la sentencia preparada (el ID a buscar).
            ps.setInt(1, id);

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Verificar si se encontró algún registro.
            if (rs.next()) {
                // Si se encontró un Pelicula, asignar los valores de las columnas al objeto de la Pelicula.
                pelicula.setId(rs.getInt(1));       // Obtener el ID del Pelicula.
                pelicula.setTitulo(rs.getString(2));   // Obtener el nombre del Pelicula.
                pelicula.setDirector(rs.getString(3));  // Obtener el correo electrónico del Pelicula.
                pelicula.setGenero(rs.getString(4));    // Obtener el estado del Pelicula.
            } else {
                // Si no se encontró ningún Pelicula con el ID especificado, establecer el objeto de la Pelicula a null.
                pelicula = null;
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al obtener la pelicula por id: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return pelicula; // Retornar el objeto de la Pelicula encontrado o null si no existe.
    }
}