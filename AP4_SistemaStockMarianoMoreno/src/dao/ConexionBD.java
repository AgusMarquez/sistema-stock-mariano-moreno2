package dao;

// Importa la clase Connection, que representa una conexión activa con la base de datos.
import java.sql.Connection;

// DriverManager permite establecer la conexión con MySQL usando una URL, usuario y contraseña.
import java.sql.DriverManager;

// SQLException se utiliza para manejar errores relacionados con la base de datos.
import java.sql.SQLException;

/**
 * Clase encargada de centralizar la conexión con la base de datos MySQL.
 *
 * Esta clase pertenece al paquete DAO porque se relaciona directamente
 * con el acceso a datos.
 *
 * Su objetivo es evitar repetir los datos de conexión en distintas clases.
 */
public class ConexionBD {

    /**
     * URL de conexión a la base de datos.
     *
     * localhost indica que MySQL está corriendo en la misma computadora.
     * 3306 es el puerto por defecto de MySQL.
     * gestion_stock_mm es el nombre de la base de datos del sistema.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock_mm";

    /**
     * Usuario de MySQL.
     *
     * En XAMPP, por defecto, el usuario suele ser "root".
     */
    private static final String USUARIO = "root";

    /**
     * Contraseña del usuario de MySQL.
     *
     * En XAMPP, por defecto, root no tiene contraseña.
     * Por eso se deja como cadena vacía.
     */
    private static final String PASSWORD = "";

    /**
     * Constructor privado.
     *
     * Se agrega para evitar que se creen objetos de esta clase,
     * ya que su única función es brindar el método estático obtenerConexion().
     */
    private ConexionBD() {
    }

    /**
     * Método que establece y devuelve una conexión con la base de datos.
     *
     * Cada vez que una clase DAO necesita consultar, insertar, actualizar
     * o eliminar datos en MySQL, utiliza este método.
     *
     * @return una conexión activa a la base de datos.
     * @throws SQLException si ocurre un error al intentar conectar con MySQL.
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}