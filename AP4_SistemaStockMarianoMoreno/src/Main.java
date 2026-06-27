// Importamos la ventana principal del sistema.
// Esta clase pertenece al paquete "vista" y contiene la interfaz gráfica.
import vista.InventarioVista;

// UIManager permite modificar la apariencia visual de los componentes Swing.
import javax.swing.UIManager;

/**
 * Clase principal del sistema.
 *
 * Esta clase contiene el método main, que es el punto de entrada
 * de la aplicación Java.
 */
public class Main {

    /**
     * Método principal del programa.
     * Desde acá se inicia la ejecución del sistema.
     */
    public static void main(String[] args) {

        try {
            /*
             * Se configura la apariencia visual de la aplicación para que use
             * el estilo propio del sistema operativo.
             *
             * En Windows, por ejemplo, permite que los botones, ventanas y campos
             * se vean más integrados con el entorno del usuario.
             */
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            /*
             * Si por algún motivo no se puede aplicar el estilo visual,
             * el programa no se detiene. Simplemente muestra el error en consola
             * y continúa ejecutándose con el estilo por defecto de Java.
             */
            e.printStackTrace();
        }

        /*
         * Se crea un objeto de la ventana principal del sistema.
         * Esta ventana contiene la interfaz para gestionar ingredientes,
         * ingresos, egresos, bajo stock y órdenes de compra.
         */
        InventarioVista ventana = new InventarioVista();

        /*
         * Se muestra la ventana en pantalla.
         * Si esto no se ejecuta, la aplicación se inicia pero no muestra nada al usuario.
         */
        ventana.setVisible(true);
    }
}