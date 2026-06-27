package modelo;

// ArrayList permite manejar listas dinámicas de productos y órdenes de compra.
import java.util.ArrayList;

// Comparator permite ordenar productos según algún criterio, en este caso por nombre.
import java.util.Comparator;

/**
 * Clase de servicio que contiene operaciones de negocio sobre el inventario.
 *
 * Esta clase trabaja con datos en memoria utilizando ArrayList.
 * Fue utilizada principalmente para el prototipo inicial de consola.
 *
 * En la versión final del sistema, muchas operaciones se realizan contra MySQL
 * mediante las clases DAO. Sin embargo, esta clase sigue siendo útil para
 * demostrar la lógica de negocio del inventario y el uso de estructuras dinámicas.
 */
public class InventarioService {

    /*
     * Lista dinámica de productos del inventario.
     *
     * Se utiliza ArrayList porque permite agregar, buscar, ordenar
     * y recorrer productos sin definir un tamaño fijo.
     */
    private ArrayList<ProductoInventario> productos;

    /*
     * Lista dinámica de órdenes de compra generadas en memoria.
     */
    private ArrayList<OrdenCompra> ordenesCompra;

    /**
     * Constructor del servicio de inventario.
     *
     * Inicializa las listas de productos y órdenes de compra.
     */
    public InventarioService() {
        this.productos = new ArrayList<>();
        this.ordenesCompra = new ArrayList<>();
    }

    /**
     * Carga datos iniciales de prueba en el inventario.
     *
     * Este método es útil para probar el sistema sin tener que cargar
     * los productos manualmente.
     *
     * En la versión con MySQL, estos datos se pueden precargar directamente
     * desde un script SQL.
     */
    public void precargarDatos() {
        productos.add(new IngredienteEsencial(1, "Harina 000", "kg", 20, 5, "Secos", "Ingrediente base para masas"));
        productos.add(new IngredienteEsencial(2, "Huevos", "unidad", 60, 24, "Frescos", "Ingrediente frecuente en clases prácticas"));
        productos.add(new Ingrediente(3, "Azúcar", "kg", 10, 3, "Secos"));
        productos.add(new Ingrediente(4, "Sal", "kg", 5, 1, "Secos"));
        productos.add(new IngredienteEsencial(5, "Levadura", "kg", 1, 2, "Frescos", "Ingrediente crítico para panificados"));
    }

    /**
     * Agrega un producto nuevo a la lista del inventario.
     *
     * Al recibir un ProductoInventario, se puede agregar tanto un Ingrediente
     * como un IngredienteEsencial gracias al polimorfismo.
     *
     * @param producto producto que se desea agregar.
     */
    public void agregarProducto(ProductoInventario producto) {
        productos.add(producto);
    }

    /**
     * Devuelve la lista completa de productos.
     *
     * @return lista de productos del inventario.
     */
    public ArrayList<ProductoInventario> getProductos() {
        return productos;
    }

    /**
     * Muestra por consola todos los productos cargados en el inventario.
     *
     * Este método fue utilizado en la versión de consola del prototipo.
     */
    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados en el inventario.");
            return;
        }

        System.out.println("\nINVENTARIO ACTUAL");

        /*
         * Se recorre la lista de productos y se imprime cada uno.
         * Java utiliza automáticamente el método toString() de cada objeto.
         */
        for (ProductoInventario producto : productos) {
            System.out.println(producto);
        }
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador buscado.
     * @return producto encontrado o null si no existe.
     */
    public ProductoInventario buscarPorId(int id) {
        for (ProductoInventario producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }

        return null;
    }

    /**
     * Busca un producto por su nombre.
     *
     * La comparación se realiza ignorando mayúsculas y minúsculas.
     *
     * @param nombre nombre del producto buscado.
     * @return producto encontrado o null si no existe.
     */
    public ProductoInventario buscarPorNombre(String nombre) {
        for (ProductoInventario producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }

    /**
     * Busca un ingrediente por nombre y muestra el resultado por consola.
     *
     * Este método es una variante pensada para la versión de consola,
     * ya que imprime directamente el resultado.
     *
     * @param nombre nombre del ingrediente a buscar.
     */
    public void buscarYMostrarPorNombre(String nombre) {
        ProductoInventario producto = buscarPorNombre(nombre);

        if (producto == null) {
            System.out.println("No se encontró un ingrediente con ese nombre.");
        } else {
            System.out.println("Ingrediente encontrado:");
            System.out.println(producto);
        }
    }

    /**
     * Registra un ingreso de stock para un producto.
     *
     * Primero busca el producto por ID. Si existe, llama al método
     * ingresarStock() de ProductoInventario.
     *
     * @param id identificador del producto.
     * @param cantidad cantidad que se desea sumar al stock.
     * @throws StockException si el producto no existe o la cantidad es inválida.
     */
    public void registrarIngreso(int id, double cantidad) throws StockException {
        ProductoInventario producto = buscarPorId(id);

        if (producto == null) {
            throw new StockException("No existe un ingrediente con ese ID.");
        }

        producto.ingresarStock(cantidad);
        System.out.println("Ingreso registrado correctamente.");
    }

    /**
     * Registra un egreso de stock para un producto.
     *
     * Primero busca el producto por ID. Si existe, llama al método
     * egresarStock() de ProductoInventario.
     *
     * Luego informa si el producto quedó con stock bajo.
     *
     * @param id identificador del producto.
     * @param cantidad cantidad que se desea descontar.
     * @throws StockException si el producto no existe, la cantidad es inválida
     *                        o no hay stock suficiente.
     */
    public void registrarEgreso(int id, double cantidad) throws StockException {
        ProductoInventario producto = buscarPorId(id);

        if (producto == null) {
            throw new StockException("No existe un ingrediente con ese ID.");
        }

        producto.egresarStock(cantidad);
        System.out.println("Egreso registrado correctamente.");

        if (producto.tieneStockBajo()) {
            System.out.println("ALERTA: el ingrediente quedó con stock bajo.");
        }
    }

    /**
     * Ordena el inventario alfabéticamente por nombre.
     *
     * Se utiliza Comparator.comparing para indicar que el criterio
     * de ordenamiento será el nombre del producto.
     */
    public void ordenarPorNombre() {
        productos.sort(Comparator.comparing(ProductoInventario::getNombre));
        System.out.println("Inventario ordenado alfabéticamente.");
    }

    /**
     * Genera una orden de compra con los productos que tienen stock bajo.
     *
     * Recorre todos los productos y agrega a la orden aquellos cuya cantidad
     * disponible sea menor o igual al stock mínimo.
     *
     * @return orden de compra generada.
     * @throws StockException si no existen productos con stock bajo.
     */
    public OrdenCompra generarOrdenCompraPorStockBajo() throws StockException {
        OrdenCompra orden = new OrdenCompra();

        for (ProductoInventario producto : productos) {
            if (producto.tieneStockBajo()) {
                orden.agregarProducto(producto);
            }
        }

        if (orden.getCantidadProductos() == 0) {
            throw new StockException("No hay ingredientes con stock bajo para generar una orden de compra.");
        }

        ordenesCompra.add(orden);
        return orden;
    }

    /**
     * Lista por consola las órdenes de compra generadas.
     *
     * Este método pertenece a la versión de consola del prototipo.
     */
    public void listarOrdenesCompra() {
        if (ordenesCompra.isEmpty()) {
            System.out.println("No hay órdenes de compra registradas.");
            return;
        }

        for (OrdenCompra orden : ordenesCompra) {
            orden.mostrarOrden();
        }
    }

    /**
     * Genera un nuevo ID para un producto.
     *
     * Busca el ID más alto dentro de la lista y devuelve el siguiente.
     * Esto se utilizaba en la versión en memoria para simular identificadores.
     *
     * En la versión con MySQL, el ID se genera automáticamente con AUTO_INCREMENT.
     *
     * @return nuevo identificador disponible.
     */
    public int generarNuevoId() {
        int mayor = 0;

        for (ProductoInventario producto : productos) {
            if (producto.getId() > mayor) {
                mayor = producto.getId();
            }
        }

        return mayor + 1;
    }
}