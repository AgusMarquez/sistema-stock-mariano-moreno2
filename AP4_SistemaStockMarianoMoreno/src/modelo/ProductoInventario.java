package modelo;

/**
 * Clase abstracta que representa un producto dentro del inventario.
 *
 * En este sistema, los productos del inventario son principalmente ingredientes.
 *
 * Se define como abstracta porque no se espera crear objetos directamente
 * de ProductoInventario, sino de sus clases hijas:
 *
 * - Ingrediente
 * - IngredienteEsencial
 *
 * Esta clase contiene los atributos y comportamientos comunes a todos
 * los productos del inventario.
 */
public abstract class ProductoInventario {

    /*
     * Identificador único del producto.
     * En la base de datos corresponde al campo id_ingrediente.
     */
    private int id;

    /*
     * Nombre del producto o ingrediente.
     * Ejemplo: Harina 000, Huevos, Levadura, Azúcar.
     */
    private String nombre;

    /*
     * Unidad de medida del producto.
     * Ejemplo: kg, unidad, litro, gramos, ml.
     */
    private String unidadMedida;

    /*
     * Cantidad disponible actualmente en el inventario.
     */
    private double cantidadDisponible;

    /*
     * Stock mínimo del producto.
     *
     * Nueva regla de negocio:
     * - En ingredientes esenciales, este valor es obligatorio y sirve
     *   para detectar bajo stock.
     * - En ingredientes generales, este valor queda en 0 porque no son
     *   críticos para la generación de órdenes de compra.
     */
    private double stockMinimo;

    /**
     * Constructor de la clase ProductoInventario.
     *
     * Permite inicializar los datos comunes de cualquier producto
     * que forme parte del inventario.
     *
     * @param id identificador del producto.
     * @param nombre nombre del producto.
     * @param unidadMedida unidad de medida del producto.
     * @param cantidadDisponible cantidad actual disponible.
     * @param stockMinimo stock mínimo definido.
     */
    public ProductoInventario(int id, String nombre, String unidadMedida, double cantidadDisponible, double stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.cantidadDisponible = cantidadDisponible;
        this.stockMinimo = stockMinimo;
    }

    /**
     * Devuelve el identificador del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve el nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la unidad de medida.
     */
    public String getUnidadMedida() {
        return unidadMedida;
    }

    /**
     * Devuelve la cantidad disponible.
     */
    public double getCantidadDisponible() {
        return cantidadDisponible;
    }

    /**
     * Devuelve el stock mínimo.
     */
    public double getStockMinimo() {
        return stockMinimo;
    }

    /**
     * Permite modificar el nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Permite modificar la unidad de medida.
     */
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    /**
     * Permite modificar la cantidad disponible.
     */
    public void setCantidadDisponible(double cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    /**
     * Permite modificar el stock mínimo.
     */
    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /**
     * Registra un ingreso de stock.
     *
     * Valida que la cantidad ingresada sea mayor a cero.
     *
     * @param cantidad cantidad que se desea sumar al stock.
     * @throws StockException si la cantidad es inválida.
     */
    public void ingresarStock(double cantidad) throws StockException {
        if (cantidad <= 0) {
            throw new StockException("La cantidad a ingresar debe ser mayor a cero.");
        }

        this.cantidadDisponible += cantidad;
    }

    /**
     * Registra un egreso de stock.
     *
     * Valida:
     * - que la cantidad sea mayor a cero;
     * - que exista stock suficiente para descontar.
     *
     * @param cantidad cantidad que se desea descontar.
     * @throws StockException si la cantidad es inválida o no hay stock suficiente.
     */
    public void egresarStock(double cantidad) throws StockException {
        if (cantidad <= 0) {
            throw new StockException("La cantidad a egresar debe ser mayor a cero.");
        }

        if (cantidad > cantidadDisponible) {
            throw new StockException("No hay stock suficiente para realizar el egreso.");
        }

        this.cantidadDisponible -= cantidad;
    }

    /**
     * Determina si el producto tiene stock bajo.
     *
     * Nueva regla de negocio:
     *
     * Solo los ingredientes esenciales se controlan por stock mínimo.
     * Esto significa que un ingrediente general puede llegar a cero
     * sin ser considerado crítico para una orden de compra.
     *
     * @return true si el producto es esencial, tiene stock mínimo definido
     * y su cantidad disponible es menor o igual al mínimo.
     */
    public boolean tieneStockBajo() {
        return "Ingrediente esencial".equalsIgnoreCase(getTipoProducto())
                && stockMinimo > 0
                && cantidadDisponible <= stockMinimo;
    }

    /**
     * Devuelve el estado actual del stock.
     *
     * Para ingredientes generales se devuelve "NO CRÍTICO",
     * ya que no se controlan por stock mínimo.
     *
     * Para ingredientes esenciales se puede devolver:
     * - SIN STOCK
     * - STOCK BAJO
     * - STOCK NORMAL
     *
     * @return estado del stock.
     */
    public String obtenerEstadoStock() {
        /*
         * Si no es esencial, no importa si se termina.
         * Por eso no se considera bajo stock.
         */
        if (!"Ingrediente esencial".equalsIgnoreCase(getTipoProducto())) {
            return "NO CRÍTICO";
        }

        /*
         * Si es esencial y la cantidad disponible es cero,
         * se informa como sin stock.
         */
        if (cantidadDisponible == 0) {
            return "SIN STOCK";
        }

        /*
         * Si es esencial y está por debajo o igual al mínimo,
         * se informa como stock bajo.
         */
        if (tieneStockBajo()) {
            return "STOCK BAJO";
        }

        /*
         * Si es esencial y supera el mínimo, está en estado normal.
         */
        return "STOCK NORMAL";
    }

    /**
     * Método abstracto que obliga a las clases hijas a indicar
     * qué tipo de producto son.
     *
     * Ejemplo:
     * - Ingrediente general
     * - Ingrediente esencial
     *
     * Esto permite aplicar polimorfismo.
     */
    public abstract String getTipoProducto();

    /**
     * Devuelve una representación textual del producto.
     *
     * Es útil para pruebas, consola o depuración.
     */
    @Override
    public String toString() {
        return "ID: " + id
                + " | Tipo: " + getTipoProducto()
                + " | Nombre: " + nombre
                + " | Cantidad: " + cantidadDisponible + " " + unidadMedida
                + " | Stock mínimo: " + stockMinimo
                + " | Estado: " + obtenerEstadoStock();
    }
}