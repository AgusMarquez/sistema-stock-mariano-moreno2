package controlador;

// DAO encargado de realizar operaciones con la base de datos.
import dao.IngredienteDAO;

// Clases del modelo.
import modelo.Ingrediente;
import modelo.IngredienteEsencial;
import modelo.ProductoInventario;
import modelo.StockException;

// Clase utilitaria para generar PDF de órdenes de compra.
import util.GeneradorPDFOrdenCompra;

// Desktop permite abrir automáticamente el PDF generado.
import java.awt.Desktop;

// File representa el archivo PDF.
import java.io.File;

// IOException permite manejar errores de archivos.
import java.io.IOException;

// SQLException permite manejar errores de base de datos.
import java.sql.SQLException;

// ArrayList permite manejar listas dinámicas de productos.
import java.util.ArrayList;

/**
 * Clase controladora del inventario.
 *
 * Dentro del patrón MVC, esta clase cumple el rol de Controlador.
 *
 * Su responsabilidad es actuar como intermediaria entre:
 * - la vista, que es la interfaz gráfica;
 * - el modelo, que representa las entidades del sistema;
 * - el DAO, que se comunica con la base de datos MySQL.
 */
public class InventarioControlador {

    /*
     * Objeto DAO utilizado para acceder a la base de datos.
     */
    private IngredienteDAO ingredienteDAO;

    /**
     * Constructor del controlador.
     *
     * Al iniciar el controlador, se crea una instancia del DAO.
     */
    public InventarioControlador() {
        this.ingredienteDAO = new IngredienteDAO();
    }

    /**
     * Agrega un nuevo ingrediente al inventario.
     *
     * Nueva regla de negocio:
     *
     * - Si el ingrediente es esencial, debe tener stock mínimo obligatorio.
     * - Si el ingrediente no es esencial, no necesita stock mínimo.
     * - Los ingredientes generales se guardan automáticamente con stock mínimo 0.
     *
     * @param nombre nombre del ingrediente.
     * @param unidad unidad de medida.
     * @param cantidad cantidad disponible inicial.
     * @param stockMinimo stock mínimo definido.
     * @param categoria categoría del ingrediente.
     * @param esEsencial indica si el ingrediente es esencial.
     * @param motivo motivo por el cual se considera esencial.
     * @throws SQLException si ocurre un error en MySQL.
     * @throws StockException si los datos ingresados no son válidos.
     */
    public void agregarIngrediente(String nombre, String unidad, double cantidad, double stockMinimo,
                                   String categoria, boolean esEsencial, String motivo)
            throws SQLException, StockException {

        /*
         * Se validan los datos antes de crear el objeto.
         * La validación contempla si el ingrediente es esencial o no.
         */
        validarDatos(nombre, cantidad, stockMinimo, esEsencial);

        ProductoInventario producto;

        if (esEsencial) {
            /*
             * Si el ingrediente es esencial y el usuario no escribió motivo,
             * se asigna uno por defecto.
             */
            if (motivo == null || motivo.trim().isEmpty()) {
                motivo = "Ingrediente considerado importante para las clases prácticas";
            }

            /*
             * Se crea un ingrediente esencial.
             * Este tipo de ingrediente sí trabaja con stock mínimo.
             */
            producto = new IngredienteEsencial(0, nombre, unidad, cantidad, stockMinimo, categoria, motivo);
        } else {
            /*
             * Si el ingrediente no es esencial, no necesita stock mínimo.
             * Por eso se fuerza el valor a 0 antes de guardarlo.
             */
            stockMinimo = 0;

            /*
             * Se crea un ingrediente general.
             */
            producto = new Ingrediente(0, nombre, unidad, cantidad, stockMinimo, categoria);
        }

        /*
         * Se delega la inserción en la base de datos al DAO.
         */
        ingredienteDAO.insertarIngrediente(producto);
    }

    /**
     * Devuelve todos los ingredientes del inventario.
     */
    public ArrayList<ProductoInventario> listarInventario() throws SQLException {
        return ingredienteDAO.listarIngredientes();
    }

    /**
     * Devuelve únicamente los ingredientes esenciales con bajo stock.
     *
     * Esto funciona porque el método tieneStockBajo() ahora solo considera
     * bajo stock cuando el producto es esencial.
     */
    public ArrayList<ProductoInventario> listarBajoStock() throws SQLException {
        return ingredienteDAO.listarBajoStock();
    }

    /**
     * Registra un ingreso de stock.
     */
    public void registrarIngreso(int idIngrediente, double cantidad) throws SQLException, StockException {
        ingredienteDAO.registrarIngreso(idIngrediente, cantidad);
    }

    /**
     * Registra un egreso de stock.
     */
    public void registrarEgreso(int idIngrediente, double cantidad) throws SQLException, StockException {
        ingredienteDAO.registrarEgreso(idIngrediente, cantidad);
    }

    /**
     * Genera una orden de compra.
     *
     * La orden se genera únicamente con ingredientes esenciales
     * que tengan bajo stock.
     *
     * Además:
     * - registra la orden en MySQL;
     * - genera un PDF;
     * - intenta abrir automáticamente el PDF.
     *
     * @return mensaje para mostrar en la interfaz.
     */
    public String generarOrdenCompra() throws SQLException, StockException {
        /*
         * Se obtiene la lista de productos con bajo stock.
         * Como la regla se corrigió en ProductoInventario,
         * esta lista solo incluirá ingredientes esenciales.
         */
        ArrayList<ProductoInventario> productosBajoStock = ingredienteDAO.listarBajoStock();

        /*
         * Se genera la orden en la base de datos.
         */
        int idOrden = ingredienteDAO.generarOrdenCompraPorStockBajo();

        try {
            /*
             * Se genera el archivo PDF con el detalle de la orden.
             */
            String rutaPDF = GeneradorPDFOrdenCompra.generarOrdenCompraPDF(idOrden, productosBajoStock);

            File archivoPDF = new File(rutaPDF);

            /*
             * Si el archivo existe y el sistema permite abrir archivos,
             * se abre automáticamente con el visor de PDF predeterminado.
             */
            if (archivoPDF.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivoPDF);
            }

            return "Orden de compra generada correctamente.\n"
                    + "Número de orden: " + idOrden + "\n"
                    + "El PDF fue generado y abierto automáticamente.\n"
                    + "Ubicación:\n" + rutaPDF;

        } catch (IOException e) {
            /*
             * Si falla la creación o apertura del PDF, la orden igual
             * puede haber quedado registrada en la base de datos.
             */
            return "La orden se registró en la base de datos, pero no se pudo generar o abrir el PDF.\n"
                    + "Detalle: " + e.getMessage();
        }
    }

    /**
     * Actualiza el stock mínimo de un ingrediente.
     *
     * Esta opción se usa principalmente para ingredientes esenciales
     * cuando se detecta que se necesita una cantidad superior al stock actual.
     */
    public void actualizarStockMinimo(int idIngrediente, double nuevoStockMinimo) throws SQLException, StockException {
        if (nuevoStockMinimo <= 0) {
            throw new StockException("El nuevo stock mínimo debe ser mayor a cero.");
        }

        ingredienteDAO.actualizarStockMinimo(idIngrediente, nuevoStockMinimo);
    }

    /**
     * Elimina un ingrediente del inventario.
     */
    public void eliminarIngrediente(int idIngrediente) throws SQLException, StockException {
        if (idIngrediente <= 0) {
            throw new StockException("Debe seleccionar un ingrediente válido para eliminar.");
        }

        ingredienteDAO.eliminarIngrediente(idIngrediente);
    }

    /**
     * Valida los datos antes de guardar un ingrediente.
     *
     * Nueva regla:
     * - El nombre no puede estar vacío.
     * - La cantidad disponible no puede ser negativa.
     * - El stock mínimo no puede ser negativo.
     * - Si el ingrediente es esencial, el stock mínimo debe ser mayor a cero.
     *
     * @param nombre nombre del ingrediente.
     * @param cantidad cantidad disponible.
     * @param stockMinimo stock mínimo ingresado.
     * @param esEsencial indica si el ingrediente es esencial.
     * @throws StockException si algún dato no cumple las reglas.
     */
    private void validarDatos(String nombre, double cantidad, double stockMinimo, boolean esEsencial)
            throws StockException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new StockException("El nombre del ingrediente no puede estar vacío.");
        }

        if (cantidad < 0) {
            throw new StockException("La cantidad disponible no puede ser negativa.");
        }

        if (stockMinimo < 0) {
            throw new StockException("El stock mínimo no puede ser negativo.");
        }

        /*
         * Regla principal:
         * solo los ingredientes esenciales están obligados
         * a tener stock mínimo.
         */
        if (esEsencial && stockMinimo <= 0) {
            throw new StockException("Los ingredientes esenciales deben tener un stock mínimo mayor a cero.");
        }
    }
}