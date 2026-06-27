package modelo;

/**
 * Excepción personalizada para errores relacionados con la lógica de stock.
 *
 * Se utiliza para diferenciar los errores propios del sistema
 * de otros errores generales de Java o de la base de datos.
 *
 * Ejemplos de uso:
 * - intentar ingresar una cantidad menor o igual a cero;
 * - intentar egresar más stock del disponible;
 * - intentar actualizar un stock mínimo inválido;
 * - intentar trabajar con un ingrediente inexistente.
 */
public class StockException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * Recibe un mensaje explicativo que luego puede mostrarse al usuario
     * mediante la interfaz gráfica.
     *
     * @param mensaje descripción del error ocurrido.
     */
    public StockException(String mensaje) {
        super(mensaje);
    }
}