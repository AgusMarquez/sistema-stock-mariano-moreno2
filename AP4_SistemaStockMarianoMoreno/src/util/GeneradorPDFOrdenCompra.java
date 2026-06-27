package util;

// Clase del modelo que representa los ingredientes o productos del inventario.
import modelo.ProductoInventario;

// Clases utilizadas para crear y escribir el archivo PDF.
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// StandardCharsets permite definir la codificación de caracteres usada al escribir el PDF.
import java.nio.charset.StandardCharsets;

// Clases para obtener y formatear la fecha y hora actual.
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ArrayList se utiliza para recibir la lista de productos con bajo stock.
import java.util.ArrayList;

/**
 * Clase utilitaria encargada de generar una orden de compra en formato PDF.
 *
 * Esta clase no utiliza una librería externa de PDF. En su lugar, genera
 * un archivo PDF básico utilizando instrucciones internas del formato PDF.
 *
 * El objetivo es que el sistema pueda emitir una orden de compra imprimible
 * para que el personal administrativo tenga una lista clara de ingredientes
 * a reponer.
 */
public class GeneradorPDFOrdenCompra {

    /**
     * Constructor privado.
     *
     * Como esta clase solo contiene métodos estáticos, no es necesario
     * crear objetos de GeneradorPDFOrdenCompra.
     */
    private GeneradorPDFOrdenCompra() {
    }

    /**
     * Genera un archivo PDF con el detalle de la orden de compra.
     *
     * El PDF incluye:
     * - título;
     * - nombre de la institución;
     * - número de orden;
     * - fecha de emisión;
     * - estado;
     * - motivo de la orden;
     * - detalle de ingredientes a comprar;
     * - espacio para observaciones;
     * - espacio para firma del responsable.
     *
     * @param numeroOrden número de orden generado en la base de datos.
     * @param productos lista de productos con stock bajo.
     * @return ruta absoluta donde se generó el archivo PDF.
     * @throws IOException si ocurre un error al crear o escribir el archivo.
     */
    public static String generarOrdenCompraPDF(int numeroOrden, ArrayList<ProductoInventario> productos)
            throws IOException {

        /*
         * Se crea una carpeta llamada "ordenes" dentro del proyecto.
         * Allí se guardarán todos los PDF generados.
         */
        File carpeta = new File("ordenes");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        /*
         * Se arma el nombre del archivo utilizando el número de orden.
         * Ejemplo: orden_compra_1.pdf
         */
        String nombreArchivo = "orden_compra_" + numeroOrden + ".pdf";
        File archivo = new File(carpeta, nombreArchivo);

        /*
         * StringBuilder permite ir construyendo el contenido interno
         * de la página PDF.
         */
        StringBuilder contenido = new StringBuilder();

        /*
         * La variable y representa la posición vertical del texto dentro
         * de la hoja. En PDF, las coordenadas comienzan desde abajo.
         */
        int y = 790;

        /*
         * Encabezado principal del documento.
         */
        agregarTexto(contenido, "ORDEN DE COMPRA", 20, 50, y);
        y -= 30;

        agregarTexto(contenido, "Escuela de Chefs Mariano Moreno", 13, 50, y);
        y -= 20;

        agregarTexto(contenido, "Sistema de Gestion de Stock y Ordenes de Compra", 11, 50, y);
        y -= 30;

        /*
         * Se obtiene la fecha y hora actual para registrar cuándo
         * se generó la orden.
         */
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fecha = LocalDateTime.now().format(formato);

        agregarTexto(contenido, "Numero de orden: " + numeroOrden, 12, 50, y);
        y -= 18;

        agregarTexto(contenido, "Fecha de emision: " + fecha, 12, 50, y);
        y -= 18;

        agregarTexto(contenido, "Estado: Pendiente", 12, 50, y);
        y -= 30;

        /*
         * Motivo por el cual se genera la orden.
         */
        agregarTexto(contenido, "Motivo de la orden:", 13, 50, y);
        y -= 18;

        agregarTexto(contenido, "Reposicion de ingredientes detectados con stock bajo.", 11, 70, y);
        y -= 30;

        agregarTexto(contenido, "Detalle de ingredientes a comprar:", 13, 50, y);
        y -= 25;

        /*
         * Si la lista de productos está vacía, se deja constancia en el PDF.
         * En condiciones normales, esta situación no debería ocurrir porque
         * la orden solo se genera si existen ingredientes con bajo stock.
         */
        if (productos == null || productos.isEmpty()) {
            agregarTexto(contenido, "No se registran ingredientes con bajo stock.", 11, 70, y);
            y -= 20;
        } else {
            /*
             * Se recorre la lista de productos con bajo stock y se escribe
             * cada uno dentro de la orden de compra.
             */
            for (ProductoInventario producto : productos) {

                /*
                 * Como cantidad sugerida de compra se utiliza el doble
                 * del stock mínimo definido para el ingrediente.
                 */
                double cantidadSugerida = producto.getStockMinimo() * 2;

                agregarTexto(contenido, "- Ingrediente: " + producto.getNombre(), 11, 70, y);
                y -= 16;

                agregarTexto(contenido, "  Stock actual: " + producto.getCantidadDisponible() + " " + producto.getUnidadMedida(), 10, 90, y);
                y -= 15;

                agregarTexto(contenido, "  Stock minimo: " + producto.getStockMinimo() + " " + producto.getUnidadMedida(), 10, 90, y);
                y -= 15;

                agregarTexto(contenido, "  Cantidad sugerida para comprar: " + cantidadSugerida + " " + producto.getUnidadMedida(), 10, 90, y);
                y -= 25;

                /*
                 * Control simple para evitar escribir fuera de la hoja.
                 * Si la lista es demasiado larga, se informa que continúa
                 * en una hoja complementaria.
                 */
                if (y < 100) {
                    agregarTexto(contenido, "Continua en nueva orden / hoja complementaria.", 10, 50, y);
                    break;
                }
            }
        }

        y -= 20;

        /*
         * Espacio para observaciones manuales.
         */
        agregarTexto(contenido, "Observaciones:", 12, 50, y);
        y -= 20;

        agregarTexto(contenido, "__________________________________________________________________", 10, 50, y);
        y -= 25;

        agregarTexto(contenido, "__________________________________________________________________", 10, 50, y);
        y -= 45;

        /*
         * Espacio para firma del responsable.
         */
        agregarTexto(contenido, "Firma responsable: ________________________________", 11, 50, y);
        y -= 20;

        agregarTexto(contenido, "Aclaracion: ______________________________________", 11, 50, y);

        /*
         * Finalmente se escribe físicamente el archivo PDF en la carpeta ordenes.
         */
        escribirPDF(archivo, contenido.toString());

        /*
         * Se devuelve la ruta absoluta para que el controlador pueda mostrarla
         * y abrir automáticamente el PDF.
         */
        return archivo.getAbsolutePath();
    }

    /**
     * Agrega una línea de texto al contenido interno del PDF.
     *
     * En PDF, el texto se escribe con instrucciones como:
     * BT: Begin Text
     * Tf: define fuente y tamaño
     * Td: define posición
     * Tj: escribe texto
     * ET: End Text
     *
     * @param contenido contenido acumulado del PDF.
     * @param texto texto a escribir.
     * @param tamanio tamaño de la fuente.
     * @param x posición horizontal.
     * @param y posición vertical.
     */
    private static void agregarTexto(StringBuilder contenido, String texto, int tamanio, int x, int y) {
        contenido.append("BT ");
        contenido.append("/F1 ").append(tamanio).append(" Tf ");
        contenido.append(x).append(" ").append(y).append(" Td ");
        contenido.append("(").append(escaparTexto(texto)).append(") Tj ");
        contenido.append("ET\n");
    }

    /**
     * Escapa caracteres especiales para evitar errores dentro del PDF.
     *
     * El formato PDF utiliza algunos caracteres con significado especial,
     * como paréntesis y barras invertidas. Por eso deben reemplazarse.
     *
     * Además, se reemplazan caracteres acentuados por versiones sin tilde
     * para evitar problemas de codificación en el PDF básico.
     *
     * @param texto texto original.
     * @return texto seguro para insertar en el PDF.
     */
    private static String escaparTexto(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("ñ", "n")
                .replace("Ñ", "N");
    }

    /**
     * Escribe el archivo PDF final.
     *
     * Este método arma la estructura mínima de un documento PDF:
     * - catálogo;
     * - páginas;
     * - página;
     * - fuente;
     * - contenido;
     * - tabla de referencias;
     * - trailer.
     *
     * @param archivo archivo PDF a generar.
     * @param contenidoPagina contenido textual de la página.
     * @throws IOException si ocurre un error al escribir el archivo.
     */
    private static void escribirPDF(File archivo, String contenidoPagina) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        /*
         * Lista que guarda las posiciones de cada objeto PDF.
         * Estas posiciones se utilizan luego para construir la tabla xref.
         */
        ArrayList<Integer> posiciones = new ArrayList<>();

        /*
         * Encabezado del archivo PDF.
         */
        escribir(salida, "%PDF-1.4\n");

        /*
         * Se convierte el contenido de la página a bytes.
         */
        byte[] contenidoBytes = contenidoPagina.getBytes(StandardCharsets.ISO_8859_1);

        /*
         * Objeto 1: catálogo principal del documento.
         */
        String objeto1 = "1 0 obj\n"
                + "<< /Type /Catalog /Pages 2 0 R >>\n"
                + "endobj\n";

        /*
         * Objeto 2: conjunto de páginas.
         */
        String objeto2 = "2 0 obj\n"
                + "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n"
                + "endobj\n";

        /*
         * Objeto 3: definición de la página.
         * MediaBox [0 0 595 842] representa una hoja A4 aproximada.
         */
        String objeto3 = "3 0 obj\n"
                + "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                + "/Resources << /Font << /F1 4 0 R >> >> "
                + "/Contents 5 0 R >>\n"
                + "endobj\n";

        /*
         * Objeto 4: fuente utilizada en el PDF.
         */
        String objeto4 = "4 0 obj\n"
                + "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\n"
                + "endobj\n";

        /*
         * Objeto 5: contenido de la página.
         */
        String objeto5Cabecera = "5 0 obj\n"
                + "<< /Length " + contenidoBytes.length + " >>\n"
                + "stream\n";

        String objeto5Cierre = "\nendstream\n"
                + "endobj\n";

        /*
         * Se escribe cada objeto y se guarda su posición inicial.
         */
        posiciones.add(salida.size());
        escribir(salida, objeto1);

        posiciones.add(salida.size());
        escribir(salida, objeto2);

        posiciones.add(salida.size());
        escribir(salida, objeto3);

        posiciones.add(salida.size());
        escribir(salida, objeto4);

        posiciones.add(salida.size());
        escribir(salida, objeto5Cabecera);
        salida.write(contenidoBytes);
        escribir(salida, objeto5Cierre);

        /*
         * Posición donde comienza la tabla de referencias cruzadas.
         */
        int inicioXref = salida.size();

        /*
         * Tabla xref: indica dónde empieza cada objeto dentro del archivo.
         */
        escribir(salida, "xref\n");
        escribir(salida, "0 6\n");
        escribir(salida, "0000000000 65535 f \n");

        for (Integer posicion : posiciones) {
            escribir(salida, String.format("%010d 00000 n \n", posicion));
        }

        /*
         * Trailer final del PDF.
         */
        escribir(salida, "trailer\n");
        escribir(salida, "<< /Size 6 /Root 1 0 R >>\n");
        escribir(salida, "startxref\n");
        escribir(salida, String.valueOf(inicioXref));
        escribir(salida, "\n%%EOF");

        /*
         * Se escribe el contenido del PDF en el archivo físico.
         */
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            salida.writeTo(fos);
        }
    }

    /**
     * Escribe texto dentro del flujo de bytes del PDF.
     *
     * @param salida flujo de salida en memoria.
     * @param texto texto a escribir.
     * @throws IOException si ocurre un error de escritura.
     */
    private static void escribir(ByteArrayOutputStream salida, String texto) throws IOException {
        salida.write(texto.getBytes(StandardCharsets.ISO_8859_1));
    }
}