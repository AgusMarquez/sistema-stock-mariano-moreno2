Sistema de Gestión de Stock y Órdenes de Compra

Escuela de Chefs Mariano Moreno

Trabajo Práctico N.º 4 - Seminario de Práctica
Licenciatura en Informática
Universidad Siglo 21

Alumno: Agustín Exequiel Márquez Moyano

---

Descripción del proyecto

El presente proyecto consiste en el desarrollo de un prototipo funcional para la gestión de stock y generación de órdenes de compra de la Escuela de Chefs Mariano Moreno.

El sistema permite administrar ingredientes utilizados en las clases prácticas, registrar ingresos y egresos de stock, diferenciar ingredientes generales de ingredientes esenciales, detectar ingredientes esenciales con bajo stock y generar órdenes de compra en formato PDF para facilitar la reposición de insumos críticos.

El objetivo principal es reducir errores en el control manual del inventario y brindar una herramienta simple para mejorar la organización administrativa de la institución.

---

Problema detectado

La Escuela de Chefs Mariano Moreno utiliza distintos ingredientes en sus clases prácticas. Algunos de ellos son críticos para el desarrollo normal de las actividades, mientras que otros son complementarios o no esenciales.

El control manual del stock puede provocar problemas como:

* falta de registro actualizado de ingredientes disponibles;
* errores al calcular necesidades de reposición;
* dificultad para identificar ingredientes esenciales con bajo stock;
* generación manual de pedidos de compra;
* falta de historial de ingresos y egresos.

Por este motivo, se propone un sistema que permita gestionar el inventario y generar órdenes de compra solo cuando existan ingredientes esenciales que deban ser repuestos.

---

Regla de negocio principal

El sistema diferencia dos tipos de ingredientes:

Ingrediente general

Un ingrediente general es aquel que puede formar parte del inventario, pero no se considera crítico para la generación de órdenes de compra.

Características:

* no requiere stock mínimo;
* se guarda con stock mínimo igual a 0;
* puede quedarse sin stock sin ser considerado crítico;
* no aparece en la vista de bajo stock;
* no se incluye en órdenes de compra;
* su estado se muestra como `NO CRÍTICO`.

Ingrediente esencial

Un ingrediente esencial es aquel que resulta importante para el normal desarrollo de las clases prácticas.

Características:

* requiere stock mínimo obligatorio;
* se controla permanentemente;
* puede figurar como `STOCK NORMAL`, `STOCK BAJO` o `SIN STOCK`;
* aparece en la vista de bajo stock cuando su cantidad disponible es menor o igual al stock mínimo;
* se incluye automáticamente en la orden de compra cuando corresponde.

Esta regla permite que el sistema genere pedidos de compra únicamente para los ingredientes realmente importantes.

---

Tecnologías utilizadas

* Java
* Java Swing
* Programación Orientada a Objetos
* JDBC
* MySQL
* XAMPP
* phpMyAdmin
* Generación de archivos PDF
* Visual Studio Code

---

Patrón de diseño aplicado

El sistema aplica una organización basada en el patrón MVC:

* Modelo: contiene las clases que representan las entidades principales del sistema.
* Vista: contiene la interfaz gráfica.
* Controlador: actúa como intermediario entre la vista y la lógica del sistema.
* DAO: se encarga del acceso a la base de datos MySQL.

Esta separación permite organizar mejor el código y distribuir responsabilidades.

---

Estructura del proyecto

```text
AP4_SistemaStockMarianoMoreno/
│
├── src/
│   ├── controlador/
│   │   └── InventarioControlador.java
│   │
│   ├── dao/
│   │   ├── ConexionBD.java
│   │   └── IngredienteDAO.java
│   │
│   ├── modelo/
│   │   ├── ProductoInventario.java
│   │   ├── Ingrediente.java
│   │   ├── IngredienteEsencial.java
│   │   ├── InventarioService.java
│   │   ├── OrdenCompra.java
│   │   └── StockException.java
│   │
│   ├── util/
│   │   └── GeneradorPDFOrdenCompra.java
│   │
│   ├── vista/
│   │   └── InventarioVista.java
│   │
│   └── Main.java
│
├── lib/
│   └── mysql-connector-j-9.7.0.jar
│
├── sql/
│   └── base_datos_ap4.sql
│
├── ordenes/
│   └── PDF generados por el sistema
│
└── README.md
```

---

Clases principales

ProductoInventario

Clase abstracta que representa un producto del inventario. Contiene atributos comunes como identificador, nombre, unidad de medida, cantidad disponible y stock mínimo.

También define métodos de negocio como:

* ingresar stock;
* egresar stock;
* detectar bajo stock;
* obtener estado del stock;
* obtener tipo de producto mediante un método abstracto.

Ingrediente

Clase que representa un ingrediente general. Hereda de `ProductoInventario` y agrega la categoría del ingrediente.

Los ingredientes generales no requieren stock mínimo y no se incluyen en órdenes de compra.

IngredienteEsencial

Clase que representa un ingrediente esencial. Hereda de `Ingrediente` y agrega el motivo por el cual se considera esencial.

Los ingredientes esenciales requieren stock mínimo y pueden generar órdenes de compra si se encuentran en bajo stock.

InventarioControlador

Clase que actúa como intermediaria entre la interfaz gráfica y el acceso a datos.

Valida reglas de negocio y delega operaciones al DAO.

IngredienteDAO

Clase encargada de comunicarse con la base de datos MySQL mediante JDBC.

Permite:

* insertar ingredientes;
* listar ingredientes;
* buscar ingredientes por ID;
* actualizar cantidades;
* registrar movimientos;
* listar ingredientes esenciales con bajo stock;
* generar órdenes de compra;
* eliminar ingredientes.

InventarioVista

Clase que contiene la interfaz gráfica del sistema desarrollada con Java Swing.

Permite interactuar con el usuario mediante formularios, botones, tablas y ventanas emergentes.

GeneradorPDFOrdenCompra

Clase utilitaria encargada de generar un archivo PDF imprimible con la orden de compra.

---

Funcionalidades principales

El sistema permite:

* agregar ingredientes generales;
* agregar ingredientes esenciales;
* exigir stock mínimo solo para ingredientes esenciales;
* registrar ingresos de stock;
* registrar egresos de stock;
* evitar egresos mayores al stock disponible;
* mostrar ingredientes generales como `NO CRÍTICO`;
* consultar ingredientes esenciales con bajo stock;
* generar órdenes de compra únicamente para ingredientes esenciales;
* generar automáticamente un PDF de la orden de compra;
* abrir automáticamente el PDF generado;
* eliminar ingredientes desde la tabla;
* registrar movimientos de stock en MySQL;
* visualizar el inventario en una interfaz gráfica.

---

Base de datos

El sistema utiliza una base de datos MySQL llamada:

```sql
gestion_stock_mm
```

Las tablas principales son:

* `ingrediente`
* `movimiento_stock`
* `orden_compra`
* `detalle_orden_compra`

La base de datos puede crearse ejecutando el script ubicado en:

```text
sql/base_datos_ap4.sql
```

---

Configuración de conexión

La conexión a MySQL se encuentra en la clase:

```text
src/dao/ConexionBD.java
```

Configuración utilizada:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock_mm";
private static final String USUARIO = "root";
private static final String PASSWORD = "";
```

Esta configuración corresponde a una instalación local de MySQL mediante XAMPP.

---

Compilación del proyecto

Desde la carpeta raíz del proyecto, ejecutar:

```bash
javac -cp "lib/*" -d bin src/Main.java src/modelo/*.java src/dao/*.java src/controlador/*.java src/vista/*.java src/util/*.java
```

---

Ejecución del sistema

Luego de compilar, ejecutar:

```bash
java -cp "bin;lib/*" Main
```

---

## Funcionamiento general

Al iniciar el sistema, se muestra una ventana principal con:

* formulario de carga de ingredientes;
* botones de acciones principales;
* tabla de inventario;
* columna para eliminar ingredientes;
* estados visuales de stock.

El usuario puede cargar ingredientes generales o esenciales.

Si el ingrediente no es esencial, el sistema asigna automáticamente stock mínimo 0 y lo muestra como `NO CRÍTICO`.

Si el ingrediente es esencial, el sistema solicita un stock mínimo obligatorio. Cuando su cantidad disponible es menor o igual a ese mínimo, el ingrediente aparece como bajo stock.

---

Generación de órdenes de compra

Cuando existen ingredientes esenciales con bajo stock, el sistema permite generar una orden de compra.

Al generar la orden:

1. Se registran los datos de la orden en MySQL.
2. Se registran los ingredientes incluidos en la orden.
3. Se calcula una cantidad sugerida de compra.
4. Se genera automáticamente un archivo PDF.
5. El PDF se guarda en la carpeta `ordenes`.
6. El PDF se abre automáticamente para su visualización o impresión.

Los ingredientes generales no se incluyen en la orden de compra.

---
Manejo de errores

El sistema utiliza una excepción personalizada llamada `StockException`.

Esta excepción permite controlar errores propios de la lógica del sistema, por ejemplo:

* nombre vacío;
* cantidades inválidas;
* egresos mayores al stock disponible;
* ingrediente inexistente;
* stock mínimo inválido;
* intento de crear un ingrediente esencial sin stock mínimo;
* ausencia de ingredientes esenciales con bajo stock.

También se manejan excepciones de base de datos mediante `SQLException`.

---

Programación Orientada a Objetos aplicada

El proyecto aplica conceptos de Programación Orientada a Objetos:

Abstracción

La clase `ProductoInventario` representa los datos y comportamientos comunes de los productos del inventario.

Encapsulamiento

Los atributos de las clases se definen como privados y se accede a ellos mediante métodos get y set.

Herencia

`Ingrediente` hereda de `ProductoInventario`.

`IngredienteEsencial` hereda de `Ingrediente`.

Polimorfismo

El método `getTipoProducto()` se comporta de manera diferente según la clase que lo implemente.

Excepciones

Se utiliza `StockException` para representar errores propios del dominio del sistema.

---

Consideraciones finales

Este sistema constituye un prototipo funcional que integra Programación Orientada a Objetos, interfaz gráfica, conexión con base de datos MySQL, generación de archivos PDF y una estructura organizada basada en MVC.

El prototipo no representa un sistema comercial completo, pero permite demostrar las funcionalidades centrales necesarias para resolver el problema planteado: mejorar el control de stock y facilitar la generación de órdenes de compra para la Escuela de Chefs Mariano Moreno.

La incorporación de la distinción entre ingredientes generales e ingredientes esenciales permite que el sistema responda mejor a la realidad del negocio, ya que no todos los insumos requieren el mismo nivel de control ni deben generar pedidos de reposición.
Sistema de Gestión de Stock y Órdenes de Compra

Escuela de Chefs Mariano Moreno

Trabajo Práctico N.º 4 - Seminario de Práctica
Licenciatura en Informática
Universidad Siglo 21

Alumno: Agustín Exequiel Márquez Moyano

---

Descripción del proyecto

El presente proyecto consiste en el desarrollo de un prototipo funcional para la gestión de stock y generación de órdenes de compra de la Escuela de Chefs Mariano Moreno.

El sistema permite administrar ingredientes utilizados en las clases prácticas, registrar ingresos y egresos de stock, diferenciar ingredientes generales de ingredientes esenciales, detectar ingredientes esenciales con bajo stock y generar órdenes de compra en formato PDF para facilitar la reposición de insumos críticos.

El objetivo principal es reducir errores en el control manual del inventario y brindar una herramienta simple para mejorar la organización administrativa de la institución.

---

Problema detectado

La Escuela de Chefs Mariano Moreno utiliza distintos ingredientes en sus clases prácticas. Algunos de ellos son críticos para el desarrollo normal de las actividades, mientras que otros son complementarios o no esenciales.

El control manual del stock puede provocar problemas como:

* falta de registro actualizado de ingredientes disponibles;
* errores al calcular necesidades de reposición;
* dificultad para identificar ingredientes esenciales con bajo stock;
* generación manual de pedidos de compra;
* falta de historial de ingresos y egresos.

Por este motivo, se propone un sistema que permita gestionar el inventario y generar órdenes de compra solo cuando existan ingredientes esenciales que deban ser repuestos.

---

Regla de negocio principal

El sistema diferencia dos tipos de ingredientes:

Ingrediente general

Un ingrediente general es aquel que puede formar parte del inventario, pero no se considera crítico para la generación de órdenes de compra.

Características:

* no requiere stock mínimo;
* se guarda con stock mínimo igual a 0;
* puede quedarse sin stock sin ser considerado crítico;
* no aparece en la vista de bajo stock;
* no se incluye en órdenes de compra;
* su estado se muestra como `NO CRÍTICO`.

Ingrediente esencial

Un ingrediente esencial es aquel que resulta importante para el normal desarrollo de las clases prácticas.

Características:

* requiere stock mínimo obligatorio;
* se controla permanentemente;
* puede figurar como `STOCK NORMAL`, `STOCK BAJO` o `SIN STOCK`;
* aparece en la vista de bajo stock cuando su cantidad disponible es menor o igual al stock mínimo;
* se incluye automáticamente en la orden de compra cuando corresponde.

Esta regla permite que el sistema genere pedidos de compra únicamente para los ingredientes realmente importantes.

---
Tecnologías utilizadas

* Java
* Java Swing
* Programación Orientada a Objetos
* JDBC
* MySQL
* XAMPP
* phpMyAdmin
* Generación de archivos PDF
* Visual Studio Code

---

Patrón de diseño aplicado

El sistema aplica una organización basada en el patrón MVC:

* Modelo: contiene las clases que representan las entidades principales del sistema.
* Vista: contiene la interfaz gráfica.
* Controlador: actúa como intermediario entre la vista y la lógica del sistema.
* DAO: se encarga del acceso a la base de datos MySQL.

Esta separación permite organizar mejor el código y distribuir responsabilidades.

---

Estructura del proyecto

```text
AP4_SistemaStockMarianoMoreno/
│
├── src/
│   ├── controlador/
│   │   └── InventarioControlador.java
│   │
│   ├── dao/
│   │   ├── ConexionBD.java
│   │   └── IngredienteDAO.java
│   │
│   ├── modelo/
│   │   ├── ProductoInventario.java
│   │   ├── Ingrediente.java
│   │   ├── IngredienteEsencial.java
│   │   ├── InventarioService.java
│   │   ├── OrdenCompra.java
│   │   └── StockException.java
│   │
│   ├── util/
│   │   └── GeneradorPDFOrdenCompra.java
│   │
│   ├── vista/
│   │   └── InventarioVista.java
│   │
│   └── Main.java
│
├── lib/
│   └── mysql-connector-j-9.7.0.jar
│
├── sql/
│   └── base_datos_ap4.sql
│
├── ordenes/
│   └── PDF generados por el sistema
│
└── README.md
```

---

Clases principales

ProductoInventario

Clase abstracta que representa un producto del inventario. Contiene atributos comunes como identificador, nombre, unidad de medida, cantidad disponible y stock mínimo.

También define métodos de negocio como:

* ingresar stock;
* egresar stock;
* detectar bajo stock;
* obtener estado del stock;
* obtener tipo de producto mediante un método abstracto.

Ingrediente

Clase que representa un ingrediente general. Hereda de `ProductoInventario` y agrega la categoría del ingrediente.

Los ingredientes generales no requieren stock mínimo y no se incluyen en órdenes de compra.

ingredienteEsencial

Clase que representa un ingrediente esencial. Hereda de `Ingrediente` y agrega el motivo por el cual se considera esencial.

Los ingredientes esenciales requieren stock mínimo y pueden generar órdenes de compra si se encuentran en bajo stock.

InventarioControlador

Clase que actúa como intermediaria entre la interfaz gráfica y el acceso a datos.

Valida reglas de negocio y delega operaciones al DAO.

IngredienteDAO

Clase encargada de comunicarse con la base de datos MySQL mediante JDBC.

Permite:

* insertar ingredientes;
* listar ingredientes;
* buscar ingredientes por ID;
* actualizar cantidades;
* registrar movimientos;
* listar ingredientes esenciales con bajo stock;
* generar órdenes de compra;
* eliminar ingredientes.

InventarioVista

Clase que contiene la interfaz gráfica del sistema desarrollada con Java Swing.

Permite interactuar con el usuario mediante formularios, botones, tablas y ventanas emergentes.

GeneradorPDFOrdenCompra

Clase utilitaria encargada de generar un archivo PDF imprimible con la orden de compra.

---

Funcionalidades principales

El sistema permite:

* agregar ingredientes generales;
* agregar ingredientes esenciales;
* exigir stock mínimo solo para ingredientes esenciales;
* registrar ingresos de stock;
* registrar egresos de stock;
* evitar egresos mayores al stock disponible;
* mostrar ingredientes generales como `NO CRÍTICO`;
* consultar ingredientes esenciales con bajo stock;
* generar órdenes de compra únicamente para ingredientes esenciales;
* generar automáticamente un PDF de la orden de compra;
* abrir automáticamente el PDF generado;
* eliminar ingredientes desde la tabla;
* registrar movimientos de stock en MySQL;
* visualizar el inventario en una interfaz gráfica.

---

Base de datos

El sistema utiliza una base de datos MySQL llamada:

```sql
gestion_stock_mm
```

Las tablas principales son:

* `ingrediente`
* `movimiento_stock`
* `orden_compra`
* `detalle_orden_compra`

La base de datos puede crearse ejecutando el script ubicado en:

```text
sql/base_datos_ap4.sql
```

---

Configuración de conexión

La conexión a MySQL se encuentra en la clase:

```text
src/dao/ConexionBD.java
```

Configuración utilizada:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock_mm";
private static final String USUARIO = "root";
private static final String PASSWORD = "";
```

Esta configuración corresponde a una instalación local de MySQL mediante XAMPP.

---

Compilación del proyecto

Desde la carpeta raíz del proyecto, ejecutar:

```bash
javac -cp "lib/*" -d bin src/Main.java src/modelo/*.java src/dao/*.java src/controlador/*.java src/vista/*.java src/util/*.java
```

---

Ejecución del sistema

Luego de compilar, ejecutar:

```bash
java -cp "bin;lib/*" Main
```

---

Funcionamiento general

Al iniciar el sistema, se muestra una ventana principal con:

* formulario de carga de ingredientes;
* botones de acciones principales;
* tabla de inventario;
* columna para eliminar ingredientes;
* estados visuales de stock.

El usuario puede cargar ingredientes generales o esenciales.

Si el ingrediente no es esencial, el sistema asigna automáticamente stock mínimo 0 y lo muestra como `NO CRÍTICO`.

Si el ingrediente es esencial, el sistema solicita un stock mínimo obligatorio. Cuando su cantidad disponible es menor o igual a ese mínimo, el ingrediente aparece como bajo stock.

---

Generación de órdenes de compra

Cuando existen ingredientes esenciales con bajo stock, el sistema permite generar una orden de compra.

Al generar la orden:

1. Se registran los datos de la orden en MySQL.
2. Se registran los ingredientes incluidos en la orden.
3. Se calcula una cantidad sugerida de compra.
4. Se genera automáticamente un archivo PDF.
5. El PDF se guarda en la carpeta `ordenes`.
6. El PDF se abre automáticamente para su visualización o impresión.

Los ingredientes generales no se incluyen en la orden de compra.

---

Manejo de errores

El sistema utiliza una excepción personalizada llamada `StockException`.

Esta excepción permite controlar errores propios de la lógica del sistema, por ejemplo:

* nombre vacío;
* cantidades inválidas;
* egresos mayores al stock disponible;
* ingrediente inexistente;
* stock mínimo inválido;
* intento de crear un ingrediente esencial sin stock mínimo;
* ausencia de ingredientes esenciales con bajo stock.

También se manejan excepciones de base de datos mediante `SQLException`.

---
Programación Orientada a Objetos aplicada

El proyecto aplica conceptos de Programación Orientada a Objetos:

Abstracción

La clase `ProductoInventario` representa los datos y comportamientos comunes de los productos del inventario.

Encapsulamiento

Los atributos de las clases se definen como privados y se accede a ellos mediante métodos get y set.

Herencia

`Ingrediente` hereda de `ProductoInventario`.

`IngredienteEsencial` hereda de `Ingrediente`.

Polimorfismo

El método `getTipoProducto()` se comporta de manera diferente según la clase que lo implemente.

Excepciones

Se utiliza `StockException` para representar errores propios del dominio del sistema.

---

 Consideraciones finales

Este sistema constituye un prototipo funcional que integra Programación Orientada a Objetos, interfaz gráfica, conexión con base de datos MySQL, generación de archivos PDF y una estructura organizada basada en MVC.

El prototipo no representa un sistema comercial completo, pero permite demostrar las funcionalidades centrales necesarias para resolver el problema planteado: mejorar el control de stock y facilitar la generación de órdenes de compra para la Escuela de Chefs Mariano Moreno.

La incorporación de la distinción entre ingredientes generales e ingredientes esenciales permite que el sistema responda mejor a la realidad del negocio, ya que no todos los insumos requieren el mismo nivel de control ni deben generar pedidos de reposición.
