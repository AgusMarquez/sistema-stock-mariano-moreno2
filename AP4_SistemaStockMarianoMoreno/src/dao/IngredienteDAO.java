package dao;

// Clases del paquete modelo que representan los datos del inventario.
import modelo.Ingrediente;
import modelo.IngredienteEsencial;
import modelo.ProductoInventario;
import modelo.StockException;

// Clases necesarias para trabajar con JDBC y MySQL.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ArrayList permite manejar listas dinámicas de ingredientes.
import java.util.ArrayList;

/**
 * Clase DAO encargada de realizar operaciones sobre la tabla ingrediente
 * y otras tablas relacionadas con el inventario.
 *
 * DAO significa Data Access Object.
 * Su función es separar la lógica de acceso a datos de la lógica visual
 * y de la lógica de negocio.
 *
 * Esta clase utiliza JDBC para conectarse con MySQL.
 */
public class IngredienteDAO {

    /**
     * Inserta un nuevo ingrediente en la base de datos.
     *
     * Puede recibir un Ingrediente general o un IngredienteEsencial,
     * ya que ambos heredan de ProductoInventario.
     *
     * @param producto ingrediente que se desea guardar.
     * @throws SQLException si ocurre un error al insertar en MySQL.
     */
    public void insertarIngrediente(ProductoInventario producto) throws SQLException {
        String sql = "INSERT INTO ingrediente "
                + "(nombre, unidad_medida, cantidad_disponible, stock_minimo, categoria, tipo_producto, motivo_esencial) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        /*
         * try-with-resources permite cerrar automáticamente la conexión
         * y el PreparedStatement al finalizar la operación.
         */
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getUnidadMedida());
            ps.setDouble(3, producto.getCantidadDisponible());
            ps.setDouble(4, producto.getStockMinimo());

            /*
             * Si el producto es un Ingrediente, se obtiene su categoría.
             * Esto también incluye a IngredienteEsencial, porque hereda de Ingrediente.
             */
            if (producto instanceof Ingrediente) {
                Ingrediente ingrediente = (Ingrediente) producto;
                ps.setString(5, ingrediente.getCategoria());
            } else {
                ps.setString(5, "Sin categoría");
            }

            /*
             * Se guarda el tipo de producto usando polimorfismo.
             * El resultado puede ser "Ingrediente general" o "Ingrediente esencial".
             */
            ps.setString(6, producto.getTipoProducto());

            /*
             * Si el producto es esencial, se guarda el motivo.
             * Si no lo es, se guarda null.
             */
            if (producto instanceof IngredienteEsencial) {
                IngredienteEsencial esencial = (IngredienteEsencial) producto;
                ps.setString(7, esencial.getMotivoEsencial());
            } else {
                ps.setString(7, null);
            }

            ps.executeUpdate();
        }
    }

    /**
     * Lista todos los ingredientes almacenados en la base de datos.
     *
     * Lee los registros de MySQL y los transforma nuevamente en objetos Java.
     *
     * @return lista de productos del inventario.
     * @throws SQLException si ocurre un error al consultar MySQL.
     */
    public ArrayList<ProductoInventario> listarIngredientes() throws SQLException {
        ArrayList<ProductoInventario> lista = new ArrayList<>();

        String sql = "SELECT * FROM ingrediente ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            /*
             * ResultSet contiene los registros devueltos por la consulta.
             * Se recorre fila por fila.
             */
            while (rs.next()) {
                int id = rs.getInt("id_ingrediente");
                String nombre = rs.getString("nombre");
                String unidad = rs.getString("unidad_medida");
                double cantidad = rs.getDouble("cantidad_disponible");
                double stockMinimo = rs.getDouble("stock_minimo");
                String categoria = rs.getString("categoria");
                String tipo = rs.getString("tipo_producto");
                String motivo = rs.getString("motivo_esencial");

                ProductoInventario producto;

                /*
                 * Según el tipo guardado en la base, se reconstruye el objeto
                 * como IngredienteEsencial o como Ingrediente general.
                 */
                if ("Ingrediente esencial".equalsIgnoreCase(tipo)) {
                    producto = new IngredienteEsencial(id, nombre, unidad, cantidad, stockMinimo, categoria, motivo);
                } else {
                    producto = new Ingrediente(id, nombre, unidad, cantidad, stockMinimo, categoria);
                }

                lista.add(producto);
            }
        }

        return lista;
    }

    /**
     * Busca un ingrediente por su ID.
     *
     * Este método se utiliza en operaciones como ingresos, egresos,
     * actualización de stock mínimo y validaciones.
     *
     * @param idBuscado identificador del ingrediente.
     * @return ingrediente encontrado o null si no existe.
     * @throws SQLException si ocurre un error al consultar MySQL.
     */
    public ProductoInventario buscarPorId(int idBuscado) throws SQLException {
        String sql = "SELECT * FROM ingrediente WHERE id_ingrediente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idBuscado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_ingrediente");
                    String nombre = rs.getString("nombre");
                    String unidad = rs.getString("unidad_medida");
                    double cantidad = rs.getDouble("cantidad_disponible");
                    double stockMinimo = rs.getDouble("stock_minimo");
                    String categoria = rs.getString("categoria");
                    String tipo = rs.getString("tipo_producto");
                    String motivo = rs.getString("motivo_esencial");

                    if ("Ingrediente esencial".equalsIgnoreCase(tipo)) {
                        return new IngredienteEsencial(id, nombre, unidad, cantidad, stockMinimo, categoria, motivo);
                    }

                    return new Ingrediente(id, nombre, unidad, cantidad, stockMinimo, categoria);
                }
            }
        }

        return null;
    }

    /**
     * Actualiza la cantidad disponible de un ingrediente.
     *
     * Se utiliza después de registrar un ingreso o un egreso.
     *
     * @param idIngrediente identificador del ingrediente.
     * @param nuevaCantidad nueva cantidad disponible.
     * @throws SQLException si ocurre un error al actualizar MySQL.
     */
    public void actualizarCantidad(int idIngrediente, double nuevaCantidad) throws SQLException {
        String sql = "UPDATE ingrediente SET cantidad_disponible = ? WHERE id_ingrediente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, nuevaCantidad);
            ps.setInt(2, idIngrediente);
            ps.executeUpdate();
        }
    }

    /**
     * Registra un movimiento de stock en la tabla movimiento_stock.
     *
     * Esto permite dejar historial de ingresos y egresos realizados.
     *
     * @param idIngrediente ingrediente afectado.
     * @param tipoMovimiento tipo de movimiento: Ingreso o Egreso.
     * @param cantidad cantidad movida.
     * @param observacion texto descriptivo del movimiento.
     * @throws SQLException si ocurre un error al insertar el movimiento.
     */
    public void registrarMovimiento(int idIngrediente, String tipoMovimiento, double cantidad, String observacion)
            throws SQLException {

        String sql = "INSERT INTO movimiento_stock (id_ingrediente, tipo_movimiento, cantidad, observacion) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idIngrediente);
            ps.setString(2, tipoMovimiento);
            ps.setDouble(3, cantidad);
            ps.setString(4, observacion);
            ps.executeUpdate();
        }
    }

    /**
     * Lista únicamente los ingredientes que tienen stock bajo.
     *
     * Un ingrediente se considera con stock bajo cuando su cantidad disponible
     * es menor o igual al stock mínimo definido.
     *
     * @return lista de ingredientes con bajo stock.
     * @throws SQLException si ocurre un error al consultar MySQL.
     */
    public ArrayList<ProductoInventario> listarBajoStock() throws SQLException {
        ArrayList<ProductoInventario> todos = listarIngredientes();
        ArrayList<ProductoInventario> bajoStock = new ArrayList<>();

        for (ProductoInventario producto : todos) {
            if (producto.tieneStockBajo()) {
                bajoStock.add(producto);
            }
        }

        return bajoStock;
    }

    /**
     * Registra un ingreso de stock.
     *
     * Primero valida la cantidad, luego busca el ingrediente,
     * calcula la nueva cantidad, actualiza la base de datos y registra
     * el movimiento en el historial.
     *
     * @param idIngrediente ingrediente al que se le sumará stock.
     * @param cantidad cantidad a ingresar.
     * @throws SQLException si ocurre un error en MySQL.
     * @throws StockException si la cantidad es inválida o el ingrediente no existe.
     */
    public void registrarIngreso(int idIngrediente, double cantidad) throws SQLException, StockException {
        if (cantidad <= 0) {
            throw new StockException("La cantidad a ingresar debe ser mayor a cero.");
        }

        ProductoInventario producto = buscarPorId(idIngrediente);

        if (producto == null) {
            throw new StockException("No existe un ingrediente con ese ID.");
        }

        double nuevaCantidad = producto.getCantidadDisponible() + cantidad;

        actualizarCantidad(idIngrediente, nuevaCantidad);
        registrarMovimiento(idIngrediente, "Ingreso", cantidad, "Ingreso registrado desde el sistema");
    }

    /**
     * Registra un egreso de stock.
     *
     * Valida que exista el ingrediente, que la cantidad sea positiva
     * y que haya stock suficiente antes de descontar.
     *
     * @param idIngrediente ingrediente al que se le descontará stock.
     * @param cantidad cantidad a egresar.
     * @throws SQLException si ocurre un error en MySQL.
     * @throws StockException si la cantidad es inválida, el ingrediente no existe
     *                        o no hay stock suficiente.
     */
    public void registrarEgreso(int idIngrediente, double cantidad) throws SQLException, StockException {
        if (cantidad <= 0) {
            throw new StockException("La cantidad a egresar debe ser mayor a cero.");
        }

        ProductoInventario producto = buscarPorId(idIngrediente);

        if (producto == null) {
            throw new StockException("No existe un ingrediente con ese ID.");
        }

        if (cantidad > producto.getCantidadDisponible()) {
            throw new StockException("No hay stock suficiente para realizar el egreso.");
        }

        double nuevaCantidad = producto.getCantidadDisponible() - cantidad;

        actualizarCantidad(idIngrediente, nuevaCantidad);
        registrarMovimiento(idIngrediente, "Egreso", cantidad, "Egreso registrado desde el sistema");
    }

    /**
     * Genera una orden de compra en la base de datos con todos los ingredientes
     * que actualmente tienen stock bajo.
     *
     * La operación utiliza una transacción:
     * - primero crea la cabecera de la orden;
     * - luego inserta los detalles de cada ingrediente;
     * - si todo sale bien, confirma con commit;
     * - si ocurre un error, revierte con rollback.
     *
     * @return ID de la orden de compra generada.
     * @throws SQLException si ocurre un error al insertar la orden.
     * @throws StockException si no hay ingredientes con bajo stock.
     */
    public int generarOrdenCompraPorStockBajo() throws SQLException, StockException {
        ArrayList<ProductoInventario> bajoStock = listarBajoStock();

        if (bajoStock.isEmpty()) {
            throw new StockException("No hay ingredientes con bajo stock para generar una orden de compra.");
        }

        String sqlOrden = "INSERT INTO orden_compra (estado) VALUES ('Pendiente')";
        String sqlDetalle = "INSERT INTO detalle_orden_compra (id_orden, id_ingrediente, cantidad_solicitada) "
                + "VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement psOrden = conexion.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                psOrden.executeUpdate();

                int idOrden = 0;

                /*
                 * Se obtiene el ID autogenerado por MySQL para la orden creada.
                 */
                try (ResultSet keys = psOrden.getGeneratedKeys()) {
                    if (keys.next()) {
                        idOrden = keys.getInt(1);
                    }
                }

                /*
                 * Se registran los ingredientes incluidos en la orden.
                 * Como cantidad sugerida se utiliza el doble del stock mínimo.
                 */
                try (PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle)) {
                    for (ProductoInventario producto : bajoStock) {
                        double cantidadSugerida = producto.getStockMinimo() * 2;

                        psDetalle.setInt(1, idOrden);
                        psDetalle.setInt(2, producto.getId());
                        psDetalle.setDouble(3, cantidadSugerida);
                        psDetalle.addBatch();
                    }

                    psDetalle.executeBatch();
                }

                conexion.commit();
                return idOrden;

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    /**
     * Actualiza el stock mínimo de un ingrediente.
     *
     * Esta operación se utiliza cuando se intenta realizar un egreso mayor
     * al stock disponible y el usuario decide marcar ese ingrediente
     * para una futura orden de compra.
     *
     * @param idIngrediente ingrediente a modificar.
     * @param nuevoStockMinimo nuevo valor de stock mínimo.
     * @throws SQLException si ocurre un error al actualizar MySQL.
     */
    public void actualizarStockMinimo(int idIngrediente, double nuevoStockMinimo) throws SQLException {
        String sql = "UPDATE ingrediente SET stock_minimo = ? WHERE id_ingrediente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, nuevoStockMinimo);
            ps.setInt(2, idIngrediente);
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un ingrediente de la base de datos.
     *
     * Antes de eliminarlo de la tabla ingrediente, se eliminan los registros
     * relacionados en detalle_orden_compra y movimiento_stock para evitar
     * errores por claves foráneas.
     *
     * La operación se realiza dentro de una transacción.
     *
     * @param idIngrediente identificador del ingrediente a eliminar.
     * @throws SQLException si ocurre un error al eliminar registros.
     */
    public void eliminarIngrediente(int idIngrediente) throws SQLException {
        String sqlDetalle = "DELETE FROM detalle_orden_compra WHERE id_ingrediente = ?";
        String sqlMovimiento = "DELETE FROM movimiento_stock WHERE id_ingrediente = ?";
        String sqlIngrediente = "DELETE FROM ingrediente WHERE id_ingrediente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle);
                 PreparedStatement psMovimiento = conexion.prepareStatement(sqlMovimiento);
                 PreparedStatement psIngrediente = conexion.prepareStatement(sqlIngrediente)) {

                psDetalle.setInt(1, idIngrediente);
                psDetalle.executeUpdate();

                psMovimiento.setInt(1, idIngrediente);
                psMovimiento.executeUpdate();

                psIngrediente.setInt(1, idIngrediente);
                int filasAfectadas = psIngrediente.executeUpdate();

                if (filasAfectadas == 0) {
                    throw new SQLException("No se encontró el ingrediente a eliminar.");
                }

                conexion.commit();

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }
}