package modelo;

/**
 * Clase que representa un ingrediente esencial dentro del inventario.
 *
 * Un ingrediente esencial es un tipo particular de ingrediente que tiene
 * una importancia especial para las clases prácticas de la escuela.
 *
 * Esta clase hereda de Ingrediente, por lo tanto también hereda indirectamente
 * de ProductoInventario.
 *
 * Ejemplo de ingredientes esenciales:
 * - Harina
 * - Levadura
 * - Huevos
 */
public class IngredienteEsencial extends Ingrediente {

    /*
     * Motivo por el cual el ingrediente se considera esencial.
     * Este dato permite justificar su importancia dentro del inventario.
     */
    private String motivoEsencial;

    /**
     * Constructor de la clase IngredienteEsencial.
     *
     * Recibe los datos comunes del producto, la categoría del ingrediente
     * y además el motivo por el cual se considera esencial.
     *
     * La palabra super se utiliza para llamar al constructor de la clase padre
     * Ingrediente, reutilizando así sus atributos y comportamiento.
     *
     * @param id identificador del ingrediente.
     * @param nombre nombre del ingrediente.
     * @param unidadMedida unidad de medida utilizada.
     * @param cantidadDisponible cantidad actual disponible.
     * @param stockMinimo cantidad mínima recomendada.
     * @param categoria categoría del ingrediente.
     * @param motivoEsencial motivo por el cual el ingrediente es esencial.
     */
    public IngredienteEsencial(int id, String nombre, String unidadMedida, double cantidadDisponible,
                               double stockMinimo, String categoria, String motivoEsencial) {
        super(id, nombre, unidadMedida, cantidadDisponible, stockMinimo, categoria);
        this.motivoEsencial = motivoEsencial;
    }

    /**
     * Devuelve el motivo por el cual el ingrediente se considera esencial.
     */
    public String getMotivoEsencial() {
        return motivoEsencial;
    }

    /**
     * Permite modificar el motivo por el cual el ingrediente se considera esencial.
     */
    public void setMotivoEsencial(String motivoEsencial) {
        this.motivoEsencial = motivoEsencial;
    }

    /**
     * Sobrescribe el método getTipoProducto.
     *
     * Aunque esta clase también es un ingrediente, se diferencia del ingrediente
     * general porque representa un producto crítico para el funcionamiento
     * de las clases prácticas.
     *
     * Este método demuestra polimorfismo, ya que Ingrediente e IngredienteEsencial
     * responden de manera diferente al mismo método.
     *
     * @return tipo de producto.
     */
    @Override
    public String getTipoProducto() {
        return "Ingrediente esencial";
    }

    /**
     * Devuelve una representación en texto del ingrediente esencial.
     *
     * Primero toma la información de la clase padre y luego agrega
     * el motivo esencial.
     */
    @Override
    public String toString() {
        return super.toString() + " | Motivo: " + motivoEsencial;
    }
}