package modelo;

// LocalDateTime permite guardar fecha y hora de creación de la orden.
import java.time.LocalDateTime;

// DateTimeFormatter permite mostrar la fecha en un formato más claro.
import java.time.format.DateTimeFormatter;

// ArrayList se utiliza para guardar la lista de productos solicitados.
import java.util.ArrayList;

/**
 * Clase que representa una orden de compra dentro del sistema.
 *
 * Una orden de compra se genera cuando existen ingredientes con stock bajo
 * y es necesario reponerlos.
 *
 * Esta clase forma parte del modelo del sistema y permite representar
 * una orden con número, fecha, estado y productos solicitados.
 */
public class OrdenCompra {

    /*
     * Contador estático utilizado para generar números de orden
     * de manera automática en memoria.
     *
     * Al ser static, pertenece a la clase y no a un objeto específico.
     */
    private static int contadorOrdenes = 1;

    /*
     * Número identificador de la orden de compra.
     */
    private int numeroOrden;

    /*
     * Fecha y hora en que se crea la orden.
     */
    private LocalDateTime fecha;

    /*
     * Estado actual de la orden.
     * En este prototipo se inicia como "Pendiente".
     */
    private String estado;

    /*
     * Lista de productos incluidos en la orden de compra.
     *
     * Se utiliza ArrayList porque permite agregar productos dinámicamente
     * sin necesidad de definir un tamaño fijo.
     */
    private ArrayList<ProductoInventario> productosSolicitados;

    /**
     * Constructor de la clase OrdenCompra.
     *
     * Al crear una nueva orden:
     * - se asigna un número automático;
     * - se registra la fecha actual;
     * - se establece el estado inicial como Pendiente;
     * - se inicializa la lista de productos solicitados.
     */
    public OrdenCompra() {
        this.numeroOrden = contadorOrdenes++;
        this.fecha = LocalDateTime.now();
        this.estado = "Pendiente";
        this.productosSolicitados = new ArrayList<>();
    }

    /**
     * Devuelve el número de orden.
     */
    public int getNumeroOrden() {
        return numeroOrden;
    }

    /**
     * Devuelve el estado actual de la orden.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Agrega un producto a la orden de compra.
     *
     * Este método permite ir armando la lista de ingredientes
     * que deben ser repuestos.
     *
     * @param producto producto o ingrediente a solicitar.
     */
    public void agregarProducto(ProductoInventario producto) {
        productosSolicitados.add(producto);
    }

    /**
     * Devuelve la cantidad de productos incluidos en la orden.
     */
    public int getCantidadProductos() {
        return productosSolicitados.size();
    }

    /**
     * Muestra por consola los datos de la orden de compra.
     *
     * Este método es útil para pruebas o para una versión de consola
     * del sistema. En la versión final con interfaz gráfica, la orden
     * también se registra en MySQL y se genera como archivo PDF.
     */
    public void mostrarOrden() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\nORDEN DE COMPRA Nº " + numeroOrden);
        System.out.println("Fecha: " + fecha.format(formato));
        System.out.println("Estado: " + estado);
        System.out.println("Productos solicitados:");

        /*
         * Se recorre la lista de productos solicitados y se muestra
         * la información principal de cada ingrediente.
         */
        for (ProductoInventario producto : productosSolicitados) {
            System.out.println("- " + producto.getNombre()
                    + " | Stock actual: " + producto.getCantidadDisponible() + " " + producto.getUnidadMedida()
                    + " | Stock mínimo: " + producto.getStockMinimo());
        }
    }
}