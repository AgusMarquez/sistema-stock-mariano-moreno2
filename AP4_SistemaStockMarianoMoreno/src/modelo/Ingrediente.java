package modelo;

/**
 * Clase que representa un ingrediente general dentro del inventario.
 *
 * Hereda de ProductoInventario, por lo tanto reutiliza los atributos
 * y métodos comunes como id, nombre, unidad de medida, cantidad disponible
 * y stock mínimo.
 *
 * Esta clase agrega un dato propio: la categoría del ingrediente.
 */
public class Ingrediente extends ProductoInventario {

    /*
     * Categoría del ingrediente.
     * Ejemplos: Secos, Frescos, Lácteos, Carnes, Verduras u Otros.
     */
    private String categoria;

    /**
     * Constructor de la clase Ingrediente.
     *
     * Recibe los datos comunes del producto y además la categoría.
     * Los datos comunes se envían al constructor de la clase padre
     * mediante la palabra reservada super.
     *
     * @param id identificador del ingrediente.
     * @param nombre nombre del ingrediente.
     * @param unidadMedida unidad de medida utilizada.
     * @param cantidadDisponible cantidad actual disponible.
     * @param stockMinimo cantidad mínima recomendada.
     * @param categoria categoría del ingrediente.
     */
    public Ingrediente(int id, String nombre, String unidadMedida, double cantidadDisponible, double stockMinimo, String categoria) {
        super(id, nombre, unidadMedida, cantidadDisponible, stockMinimo);
        this.categoria = categoria;
    }

    /**
     * Devuelve la categoría del ingrediente.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Permite modificar la categoría del ingrediente.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Implementación del método abstracto definido en ProductoInventario.
     *
     * En este caso, indica que el objeto corresponde a un ingrediente general.
     *
     * @return tipo de producto.
     */
    @Override
    public String getTipoProducto() {
        return "Ingrediente general";
    }

    /**
     * Devuelve una representación en texto del ingrediente.
     *
     * Primero utiliza el toString de la clase padre y luego agrega
     * la categoría propia de esta clase.
     */
    @Override
    public String toString() {
        return super.toString() + " | Categoría: " + categoria;
    }
}