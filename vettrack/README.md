# 🐾 VetTrack - Sistema Integral de Gestión Veterinaria

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker)
![Postgres](https://img.shields.io/badge/Postgres-16-blue?style=for-the-badge&logo=postgresql)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple?style=for-the-badge)

**VetTrack** es una solución de backend moderna y escalable diseñada para transformar la gestión operativa de clínicas veterinarias. Construido bajo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**, este sistema garantiza un dominio puro, agnóstico al framework y altamente testeable.

---

## 🚀 Características Principales

### 🏗️ Arquitectura & Diseño
* **Arquitectura Hexagonal Pura:** Aislamiento total del dominio (`domain/model`) respecto a frameworks y librerías externas.
* **Microservicios:** Comunicación REST síncrona entre el núcleo de gestión y servicios satélite.
* **Patrones de Diseño:** Uso de **Strategy** para seguridad dinámica y **Adapter** para integración externa.

### 🛡️ Seguridad Avanzada
* **Autenticación JWT Stateless:** Implementación robusta con Spring Security 6.
* **Row Level Security (RLS) Lógico:**
    * 🕵️ **ADMIN:** Acceso total al sistema.
    * 👨‍⚕️ **VETERINARIO:** Acceso restringido a su agenda y pacientes asignados.
    * 🐶 **DUEÑO:** Acceso exclusivo a la información de sus propias mascotas.

### 🩺 Funcionalidad de Negocio
* **Gestión de Citas:** Flujo transaccional completo (Solicitud -> Validación Disponibilidad -> Confirmación/Rechazo).
* **Gestión de Mascotas:** Validación estricta de estado (Solo mascotas `ACTIVAS` pueden agendar).
* **Integración Externa:** Conexión con `vet-availability-mock` para validar agendas en tiempo real mediante algoritmos deterministas.
* **Historial Clínico:** Registro de diagnósticos (Relación 1-1) con validación de autoría veterinaria.

### ⚙️ Infraestructura & Calidad
* **Base de Datos Evolutiva:** Versionado de esquema con **Flyway** (Migraciones V1 a V6).
* **Observabilidad:** Métricas expuestas vía **Actuator + Prometheus** para monitoreo en tiempo real.
* **Testing Profesional:** Pruebas de integración con **Testcontainers** (Base de datos real en Docker, no H2).

---

## 🛠️ Stack Tecnológico

| Componente | Tecnología |
| :--- | :--- |
| **Lenguaje** | Java 17 (LTS) |
| **Framework** | Spring Boot 3.4.0 |
| **Base de Datos** | PostgreSQL 16 |
| **Persistencia** | Spring Data JPA + Hibernate (Optimizado con `@EntityGraph`) |
| **Seguridad** | Spring Security + JJWT |
| **Mapeo** | MapStruct |
| **Migraciones** | Flyway |
| **Contenerización** | Docker & Docker Compose |
| **Testing** | JUnit 5, Mockito, Testcontainers |

---

## 📦 Estructura del Proyecto (Hexagonal)

El código sigue estrictamente la separación de responsabilidades:

```text
src/main/java/com/riwi/vettrack/appointmentService
├── application/          # Casos de Uso (Orquestación)
│   └── service/          # Implementación de la lógica de aplicación
├── domain/               # EL NÚCLEO (Sin dependencias de Spring)
│   ├── model/            # Entidades de Negocio (POJOs puros)
│   ├── ports/            # Interfaces (Puertos de Entrada/Salida)
│   └── exception/        # Excepciones de Dominio
└── infrastructure/       # Adaptadores (Implementación técnica)
    ├── adapters/
    │   ├── in/rest/      # Controladores REST (Driving Adapter)
    │   ├── out/persistence/ # Repositorios JPA (Driven Adapter)
    │   └── out/external/    # Clientes HTTP (Driven Adapter)
    └── security/         # Configuración de Seguridad
```
---

## 🚀 Guía de Inicio Rápido

### Prerrequisitos
* **Docker** y **Docker Compose** instalados (Requerido).
* **Java 17** (Opcional si usas Docker).
* **Maven** (Opcional si usas Docker).

### 1. Clonar y Desplegar (Recomendado)
Levanta todo el ecosistema (Base de Datos, Servicio Mock, Aplicación) con un solo comando:

```bash
git clone [https://github.com/tu-usuario/vettrack.git](https://github.com/tu-usuario/vettrack.git)
cd vettrack
docker-compose up --build
```

El sistema estará listo cuando en los logs aparezca:
```
Started VettrackApplication in X seconds.
```

## 2. Acceso a Servicios

- **Appointment Service (API Principal):**  
  http://localhost:8080

- **Mock Availability Service:**  
  http://localhost:8081

- **Base de Datos (Postgres):**  
  `localhost:5432`  
  **User:** postgres  
  **Password:** password

## 📡 Endpoints Principales (API Reference)

### 🔐 Autenticación

| Método | Endpoint        | Descripción                                                |
|--------|------------------|------------------------------------------------------------|
| POST   | `/auth/register` | Registrar nuevo usuario (Rol por defecto: **DUENO**).      |
| POST   | `/auth/login`    | Obtener Token JWT.                                         |
Ejemplo: POST   | `/auth/register`
Roles disponibles: DUENO, VETERINARIO, ADMIN
```
{
"name": "Juan Perez",
"email": "juan@email.com",
"password": "securePassword123",
"phone": "555-1234",
"role": "DUENO"
}
```
POST   | `/auth/login`
```
{
  "email": "juan@email.com",
  "password": "securePassword123"
}
```

### 📅 Citas (Appointments)

| Método | Endpoint        | Rol Requerido | Descripción                                                                 |
|--------|------------------|----------------|-----------------------------------------------------------------------------|
| POST   | `/appointments`  | AUTH           | Solicita una cita. Valida disponibilidad externa automáticamente.           |
| GET    | `/appointments`  | AUTH           | RLS Activo: Lista citas filtradas según quién pregunte.                     |
Ejemplo: POST   | `/appointments`
```
{
  "petId": 1,
  "veterinarianId": 1,
  "dateTime": "2025-12-25T10:30:00",
  "reason": "Control general y vacunas"
}
```
## Gestión de Mascotas (Pets)

### 🐾 Registrar Mascota
**POST** `/pets`

La mascota se crea automáticamente con estado **ACTIVA** y se asigna al usuario que realiza la petición.

#### Ejemplo (JSON)

```json
{
  "name": "Zeus",
  "ownerName": "Juan Perez",
  "ownerDocument": "10203040",
  "species": "PERRO",
  "race": "Golden Retriever",
  "age": 2
}
```
**Especies soportadas:** `PERRO`, `GATO`, `AVE`, `OTRO`.

### 📋 Listar Mis Mascotas
**GET** `/pets`

- **Dueño:** Ve solo sus propias mascotas.
- **Admin/Veterinario:** Ven todas las mascotas registradas.

### 🩺 Diagnósticos

| Método | Endpoint                    | Rol Requerido | Descripción                                                   |
|--------|------------------------------|----------------|---------------------------------------------------------------|
| POST   | `/appointments/diagnosis`    | VETERINARIO    | Registra diagnóstico. Solo el veterinario asignado puede hacerlo. |
```
{
"appointmentId": 1,
"description": "Otitis canina leve",
"treatment": "Limpieza diaria y gotas óticas cada 12 horas",
"recommendations": "Evitar mojar las orejas durante el baño"
}
```

### 📊 Observabilidad

| Método | Endpoint              | Descripción                                          |
|--------|------------------------|------------------------------------------------------|
| GET    | `/actuator/health`    | Estado del sistema y conexión a BD.                 |
| GET    | `/actuator/metrics`   | Métricas de la JVM y rendimiento.                   |
| GET    | `/actuator/prometheus`| Métricas en formato Prometheus para Grafana.        |

## 🧪 Ejecución de Pruebas

Para ejecutar las pruebas unitarias y de integración  
*(requiere Docker corriendo para Testcontainers)*:

```bash
./mvnw clean test
```

Esto validará:
- Lógica de dominio.
- Integración con Base de Datos real (creación/destrucción de contenedores Postgres).
- Seguridad de endpoints.

