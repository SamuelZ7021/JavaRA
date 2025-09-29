# MiniTienda - Un Proyecto Java para la Gestión de Inventario

## ¿Qué hace este proyecto?

Imagina que tienes una pequeña tienda y necesitas llevar un registro de los productos que vendes. Este programa te ayuda con eso. Es una aplicación de consola (se ejecuta en una ventana de texto) que te presenta un menú con opciones como:

*   **Agregar un nuevo producto:** Guarda un producto con su nombre, precio y cantidad en stock.
*   **Ver todos los productos:** Muestra una lista de todos los productos que tienes.
*   **Actualizar un producto:** Permite cambiar el precio o el stock de un producto existente.
*   **Eliminar un producto:** Quita un producto de tu inventario.
*   **Buscar un producto:** Encuentra un producto por su nombre.

## ¿Cómo está organizado el proyecto?

El proyecto está dividido en varios paquetes (carpetas) para mantener el código ordenado. Cada paquete tiene una responsabilidad específica:

### 1. `com.minitienda.Controller`

Este paquete se encarga de manejar la interacción con el usuario.

*   **`MenuController.java`:** Es el cerebro del menú. Cuando eliges una opción (como "Agregar producto"), este archivo contiene el código que se ejecuta. Llama a otros componentes del programa para hacer el trabajo real. Por ejemplo, si quieres agregar un producto, `MenuController` le pide al `InventoryServicio` que lo haga.

### 2. `com.minitienda.DataBase`

Aquí se configura la conexión a la base de datos.

*   **`ConfigDB.java`:** Este archivo contiene la información para conectarse a tu base de datos MySQL (la dirección, el usuario y la contraseña). Tiene un método `getConnection()` que otros archivos usan para obtener una conexión a la base de datos.

### 3. `com.minitienda.Entity`

Las "entidades" son los objetos del mundo real que queremos representar en nuestro programa.

*   **`Product.java`:** Define qué es un "Producto". Un producto tiene un `id` (un número único), un `nombre`, un `precio` y un `stock` (la cantidad que tienes). Es como una plantilla para crear objetos de tipo producto.

### 4. `com.minitienda.Interface`

Las interfaces son como contratos. Definen qué métodos (acciones) debe tener una clase, pero no cómo los hace.

*   **`CRUD.java`:** `CRUD` es un acrónimo de **C**reate (Crear), **R**ead (Leer), **U**pdate (Actualizar) y **D**elete (Eliminar). Esta interfaz define los métodos básicos que cualquier "Modelo" de datos debe tener.
*   **`ServicioInterface.java`:** Define las acciones que se pueden realizar en el inventario, como `agregarProducto`, `actualizarPrecio`, etc.

### 5. `com.minitienda.Model`

El "Modelo" es la parte del programa que interactúa directamente con la base de datos.

*   **`ProductoModel.java`:** Esta clase se encarga de ejecutar las consultas SQL para guardar, buscar, actualizar y eliminar productos en la base de datos.

    *   **¿Cómo se inicializa el `pstmt`?**
        El `PreparedStatement` (o `pstmt`) se inicializa dentro de un bloque `try-with-resources`. Este es un ejemplo de `ProductoModel.java`:

        ```java
        String sql = "INSERT INTO products(product_name, price, stock) VALUES (?, ?, ?)";
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // ... aquí se usa pstmt ...
        }
        ```

        Aquí, `conn.prepareStatement(sql)` crea el objeto `pstmt` con la consulta SQL que queremos ejecutar.

    *   **El `try-with-resources`:**
        Esta es una característica muy útil de Java. El bloque `try-with-resources` se asegura de que los "recursos" (como la conexión a la base de datos `conn` y el `PreparedStatement` `pstmt`) se cierren automáticamente al final del bloque, incluso si ocurre un error. Esto previene problemas y hace que el código sea más limpio y seguro.

        Sin `try-with-resources`, tendrías que cerrar todo manualmente en un bloque `finally`, lo cual es más propenso a errores.

### 6. `com.minitienda.Service`

El "Servicio" contiene la lógica de negocio. Actúa como un intermediario entre el `Controller` y el `Model`.

*   **`InventoryServicio.java`:** Implementa la `ServicioInterface`. Cuando el `MenuController` dice "agrega un producto", `InventoryServicio` toma los datos, crea un objeto `Product` y le dice al `ProductoModel` que lo guarde en la base de datos.

### 7. `com.minitienda` (Paquete principal)

*   **`Main.java`:** Es el punto de entrada de la aplicación. Lo único que hace es crear una instancia de `MenuUI` y llamar a su método `iniciar()`.
*   **`MenuUI.java`:** Se encarga de mostrar el menú al usuario y de leer la opción que elige. Llama a los métodos correspondientes en `MenuController`.

## Flujo de una acción: Agregar un producto

Para que te hagas una idea de cómo funciona todo junto, aquí tienes el flujo de lo que pasa cuando agregas un producto:

1.  **`Main.java`** inicia la aplicación, que muestra el menú a través de **`MenuUI.java`**.
2.  Eliges la opción "Agregar producto".
3.  **`MenuUI.java`** llama al método `agregarProducto()` en **`MenuController.java`**.
4.  **`MenuController.java`** te pide el nombre, el precio y el stock del producto.
5.  Luego, llama al método `agregarProducto()` en **`InventoryServicio.java`**, pasándole los datos que introdujiste.
6.  **`InventoryServicio.java`** crea un nuevo objeto `Product` con esos datos.
7.  Llama al método `crear()` en **`ProductoModel.java`**, pasándole el objeto `Product`.
8.  **`ProductoModel.java`** se conecta a la base de datos (usando `ConfigDB`), prepara la consulta SQL y la ejecuta para insertar el nuevo producto.
9.  Si todo sale bien, se muestra un mensaje de éxito.

## Dudas Frecuentes

### ¿Cuál es la diferencia entre el CRUD de `InventoryServicio` y el de `ProductoModel`?

*   **`ProductoModel` (El Obrero Especializado):** Imagina que `ProductoModel` es un obrero en un taller que solo sabe hacer una cosa: trabajar con la maquinaria pesada (la base de datos). Sus herramientas son las sentencias SQL (`INSERT`, `UPDATE`, `DELETE`, `SELECT`). Si le das un producto, lo guarda. Si le pides un producto, te lo da. No toma decisiones, solo sigue órdenes directas y técnicas. Su única preocupación es hablar el "idioma" de la base de datos.

*   **`InventoryServicio` (El Gerente de Taller):** `InventoryServicio` es el gerente. No opera la maquinaria directamente, pero sabe lo que hay que hacer. Cuando el `Controller` (el cliente) le pide "necesito agregar este nuevo producto", el gerente (`InventoryServicio`) hace lo siguiente:
    1.  **Toma los datos:** Recibe el nombre, precio y stock.
    2.  **Prepara el material:** Crea un objeto `Product` con esos datos. Esto es parte de la "lógica de negocio".
    3.  **Da la orden al obrero:** Le dice al `ProductoModel`: "Oye, toma este objeto `Product` y guárdalo en la base de datos".
    4.  **Maneja lógica adicional:** Por ejemplo, si quisiéramos enviar un correo cada vez que se agrega un producto, esa lógica iría aquí, en el servicio, no en el modelo.

**En resumen:**

| Característica | `ProductoModel` (Capa de Acceso a Datos) | `InventoryServicio` (Capa de Servicio/Negocio) |
| :--- | :--- | :--- |
| **Responsabilidad** | Hablar directamente con la base de datos. | Contener la lógica de negocio de la aplicación. |
| **Qué hace** | Ejecuta sentencias SQL. | Coordina las tareas. Toma los datos de la capa de control, los procesa y utiliza el `Model` para persistirlos. |
| **Conocimiento** | Sabe SQL y cómo manejar `Connection` y `PreparedStatement`. | Sabe qué pasos seguir para completar una tarea (ej. "para crear un producto, primero creo el objeto y luego llamo al modelo"). |
| **Analogía** | Obrero | Gerente |

Separar estas responsabilidades hace que el código sea mucho más fácil de mantener. Si en el futuro cambias de base de datos (de MySQL a otra), solo tendrías que modificar el `ProductoModel`. El `InventoryServicio` y el resto de la aplicación no se verían afectados porque la "lógica de negocio" está separada del "acceso a datos".
