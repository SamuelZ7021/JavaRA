# Sistema de Gestión de Roles y Usuarios

Esta es una aplicación de escritorio desarrollada en Java que simula un sistema de gestión de usuarios con diferentes roles (Administrador y Cliente). La aplicación utiliza una interfaz de usuario simple basada en `JOptionPane` y se conecta a una base de datos MySQL para la persistencia de datos.

## ✨ Características Principales

*   **Autenticación de Usuarios:** Sistema de inicio de sesión seguro.
*   **Gestión de Roles:** Menús y funcionalidades diferentes para Administradores y Clientes.
*   **Persistencia en Base de Datos:** Todos los datos se almacenan en una base de datos MySQL.
*   **Operaciones CRUD:** Funcionalidad completa para Crear, Leer, Actualizar y Eliminar usuarios.
*   **Lógica Transaccional:** Las operaciones críticas (registro, actualización, bloqueo) son transaccionales para garantizar la integridad de los datos.
*   **Bitácora de Acciones:** Los administradores registran automáticamente sus acciones de bloqueo/desbloqueo en una tabla de bitácora.

## 🛠️ Tecnologías Utilizadas

*   **Java (JDK 11 o superior)**
*   **Swing** (para la interfaz de usuario con `JOptionPane`)
*   **JDBC** (para la conexión con la base de datos)
*   **MySQL** (como sistema de gestión de base de datos)

## 📂 Estructura del Proyecto

El código fuente está organizado en paquetes según su responsabilidad, siguiendo las mejores prácticas de la programación orientada a objetos.

### 📦 paquete `app`

> Contiene las clases responsables de iniciar la aplicación y gestionar la interfaz de usuario. Es el punto de entrada y el controlador de la navegación.

*   **`Main.java`**: Es el punto de entrada de la aplicación. Su única responsabilidad es crear una instancia de la clase `Menu` y llamar a su método principal para iniciar el programa.
*   **`Menu.java`**: Gestiona toda la interacción con el usuario a través de diálogos `JOptionPane`. Contiene la lógica para mostrar el menú principal, los menús de administrador y cliente, y procesar las opciones seleccionadas por el usuario.

### 📦 paquete `config`

> Centraliza la configuración de la aplicación, principalmente la conexión a la base de datos.

*   **`Conexion.java`**: Clase de utilidad que proporciona un método estático (`getConnection()`) para establecer y devolver una conexión a la base de datos MySQL. Aquí se definen la URL de la base de datos, el usuario y la contraseña.

### 📦 paquete `model`

> Define las entidades de negocio de la aplicación. Estas clases representan los datos con los que trabaja el sistema.

*   **`User.java`**: Clase `abstract` que sirve como base para todos los tipos de usuarios. Define los atributos y métodos comunes como `id`, `nombre`, `email`, `password`, `estado` y la lógica de `login`.
*   **`Administrador.java`**: Clase que hereda de `User` y representa a un usuario con privilegios de administrador.
*   **`Cliente.java`**: Clase que hereda de `User` y representa a un cliente. Añade atributos adicionales como `telefono` y `direccion`.

### 📦 paquete `security`

> Define las interfaces y contratos relacionados con la seguridad de la aplicación.

*   **`Autenticable.java`**: Interfaz que define un único método, `login(String password)`. Cualquier clase que la implemente (como `User`) debe proporcionar una lógica para validar una contraseña.

### 📦 paquete `service`

> Contiene la lógica de negocio de la aplicación. Actúa como un intermediario entre la interfaz de usuario (`app`) y el modelo de datos (`model`), y es responsable de todas las interacciones con la base de datos.

*   **`UsuarioInterface.java`**: Define el contrato que debe seguir cualquier servicio de gestión de usuarios. Especifica los métodos para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y de autenticación.
*   **`UserService.java`**: Es el "cerebro" de la aplicación. Implementa `UsuarioInterface` y contiene toda la lógica para interactuar con la base de datos. Utiliza JDBC para ejecutar consultas SQL, maneja transacciones para garantizar la integridad de los datos y construye los objetos del modelo a partir de los resultados de la base de datos.

## 🚀 Configuración y Puesta en Marcha

Sigue estos pasos para configurar y ejecutar el proyecto en tu entorno local.

### Prerrequisitos

*   Tener instalado un **JDK (Java Development Kit)**, versión 11 o superior.
*   Tener instalado y en ejecución un **servidor de MySQL**.
*   Un IDE de Java como **IntelliJ IDEA** o **Eclipse**.

### Paso 1: Crear la Base de Datos

Ejecuta el siguiente script SQL en tu cliente de MySQL. Creará la base de datos, las tablas, las relaciones y los datos iniciales.

```sql
-- PASO 1: CREAR LA BASE DE DATOS Y LAS TABLAS
CREATE DATABASE IF NOT EXISTS usuarios_db;
USE usuarios_db;

-- Tabla de Roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de Usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    rol_id INT NOT NULL
);

-- Tabla de Clientes
CREATE TABLE clientes (
    usuario_id INT PRIMARY KEY,
    telefono VARCHAR(20),
    direccion VARCHAR(255)
);

-- Tabla de Bitácora de Bloqueos
CREATE TABLE bitacora_bloqueos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    usuario_afectado_id INT NOT NULL,
    accion VARCHAR(20) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- PASO 2: DEFINIR LAS RELACIONES (CLAVES FORÁNEAS)
ALTER TABLE usuarios
ADD CONSTRAINT fk_usuario_rol
FOREIGN KEY (rol_id) REFERENCES roles(id);

ALTER TABLE clientes
ADD CONSTRAINT fk_cliente_usuario
FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE CASCADE;

ALTER TABLE bitacora_bloqueos
ADD CONSTRAINT fk_bitacora_admin
FOREIGN KEY (admin_id) REFERENCES usuarios(id);

ALTER TABLE bitacora_bloqueos
ADD CONSTRAINT fk_bitacora_usuario_afectado
FOREIGN KEY (usuario_afectado_id) REFERENCES usuarios(id);

-- PASO 3: INSERTAR DATOS INICIALES
-- Insertar los roles disponibles (asegúrate de que la tabla esté vacía)
INSERT INTO roles (nombre_rol) VALUES ('ADMIN'), ('CLIENTE') ON DUPLICATE KEY UPDATE nombre_rol=nombre_rol;

-- Insertar un usuario Administrador (rol_id = 1)
INSERT INTO usuarios (nombre, email, password, rol_id) 
VALUES ('Administrador del Sistema', 'admin@sistema.com', 'admin123', 1) ON DUPLICATE KEY UPDATE email=email;

-- Insertar un usuario Cliente (rol_id = 2)
INSERT INTO usuarios (nombre, email, password, rol_id) 
VALUES ('Juan Perez', 'juan.perez@email.com', 'cliente456', 2) ON DUPLICATE KEY UPDATE email=email;

-- Crear el perfil de cliente para Juan Perez (cuyo id es 2)
INSERT INTO clientes (usuario_id, telefono, direccion) 
VALUES (2, '3001234567', 'Calle Falsa 123, Medellin') ON DUPLICATE KEY UPDATE usuario_id=usuario_id;
```

### Paso 2: Configurar el Proyecto

1.  **Añadir el Driver JDBC de MySQL:** Este es el paso más importante. Debes descargar el conector JDBC de MySQL (un archivo `.jar`) y añadirlo al **Build Path** (o Classpath) de tu proyecto en tu IDE. Sin este driver, la aplicación no podrá conectarse a la base de datos.

2.  **Verificar las Credenciales:** Abre el archivo `src/config/Conexion.java` y asegúrate de que el usuario y la contraseña coincidan con los de tu servidor de MySQL.

    ```java
    // Archivo: src/config/Conexion.java
    private static final String USER = ""; // <-- Reemplaza con tu usuario de MySQL
    private static final String PASSWORD = ""; // <-- Reemplaza con tu contraseña
    ```

### Paso 3: Ejecutar la Aplicación

Una vez configurada la base de datos y el proyecto, ejecuta el método `main` en la clase **`app/Main.java`**. Esto iniciará la aplicación y mostrará el menú principal.
