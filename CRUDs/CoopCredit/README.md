# 🏦 CoopCredit - Sistema Integral de Solicitudes de Crédito

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple)

## 📋 Descripción del Proyecto

CoopCredit es una solución de software moderna y distribuida diseñada para automatizar y asegurar el proceso de solicitud y evaluación de créditos. El sistema implementa una **Arquitectura Hexagonal (Puertos y Adaptadores)** estricta para garantizar el desacoplamiento entre la lógica de negocio y la infraestructura tecnológica.

El ecosistema está compuesto por microservicios orquestados que manejan desde la autenticación segura hasta la evaluación de riesgos financieros mediante integración con sistemas externos.

---

## 🏗️ Arquitectura y Diseño

El proyecto sigue rigurosamente los principios de **Clean Architecture**:

### 💠 Arquitectura Hexagonal
El núcleo de la aplicación (`domain`) es agnóstico al framework y a la base de datos.
* **Domain (Núcleo):** Entidades (`CreditApplication`, `User`), Excepciones de negocio y Puertos (Interfaces).
* **Application (Capa de Servicio):** Implementación de casos de uso (`ManageCreditApplicationUseCase`) y orquestación.
* **Infrastructure (Adaptadores):**
    * *Input:* Controladores REST (`AuthController`, `CreditApplicationController`).
    * *Output:* Repositorios JPA (`PostgreSQL`), Adaptadores REST externos (`RiskServiceAdapter`) y Seguridad (`JwtService`).

### 🧩 Microservicios
1.  **`credit-application-service` (Core):**
    * Gestión de usuarios y afiliados.
    * Procesamiento de solicitudes de crédito.
    * Reglas de negocio (validación de salario vs. cuota).
    * Seguridad y autenticación.
2.  **`risk-central-mock-service` (Mock):**
    * Simulador de buró de crédito externo.
    * Algoritmo determinista basado en hash para retornar scores consistentes.

---

## 🛠️ Tecnologías y Métodos Empleados

| Categoría | Tecnología / Librería | Uso en el Proyecto |
| :--- | :--- | :--- |
| **Lenguaje** | **Java 17 (OpenJDK)** | Base del desarrollo backend. |
| **Framework** | **Spring Boot 3.3.0** | Inyección de dependencias, Web, Data. |
| **Base de Datos** | **PostgreSQL 15** | Persistencia relacional robusta. |
| **Migraciones** | **Flyway** | Control de versiones de base de datos (`V1__schema.sql`). |
| **Seguridad** | **Spring Security + JWT** | Autenticación Stateless y autorización por Roles. |
| **Mapeo** | **MapStruct** | Conversión eficiente entre Entidades, Dominio y DTOs. |
| **Cliente HTTP** | **Spring RestClient** | Comunicación sincrónica entre microservicios. |
| **Contenedores** | **Docker & Compose** | Orquestación y despliegue reproducible. |
| **Testing** | **JUnit 5 & Mockito** | Pruebas unitarias y de integración. |
| **Librerías** | **Lombok** | Reducción de código repetitivo (Boilerplate). |

---

## 👥 Roles y Flujos de Usuario

El sistema implementa seguridad RBAC (Role-Based Access Control) con los siguientes roles:

### 1. 🟢 ROLE_AFILIADO (Usuario Final)
* **Registro:** Puede registrarse creando un usuario y perfil financiero simultáneamente.
* **Solicitar Crédito:** Puede crear solicitudes de crédito (si está activo y cumple las reglas financieras).
* **Restricción:** Solo puede ver y gestionar sus propios datos.

### 2. 🔵 ROLE_ANALISTA / ROLE_ADMIN (Personal Interno)
* **Consultas:** Acceso a visualizar solicitudes de crédito por ID para auditoría o revisión.

### 🔄 Flujo de Solicitud de Crédito (Caso de Uso Principal)
1.  El **Afiliado** se autentica y obtiene un Token JWT.
2.  Envía una solicitud (`POST /api/v1/credits`) con el monto y plazo.
3.  **Validaciones de Negocio:**
    * ¿El afiliado existe y está activo?
    * **Regla Financiera:** ¿La cuota mensual estimada supera el 50% de su salario mensual?
4.  **Evaluación de Riesgo:** El sistema consulta al microservicio `risk-central`.
    * Si Score < 70 → RECHAZADO (Automático).
    * Si Score >= 70 → APROBADO.
5.  **Persistencia:** Se guarda la solicitud y la evaluación de riesgo de forma transaccional.

---

## 🚀 Guía de Instalación y Ejecución

### Prerrequisitos
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y corriendo.
* (Opcional) Java 17 y Maven si deseas ejecutarlo localmente sin Docker.

### 👣 Paso 1: Clonar y Preparar
Descarga el proyecto y navega a la carpeta raíz `CoopCreditSolution`.

```bash
cd CoopCreditSolution
```

### 👣 Paso 2: Ejecución con Docker (Recomendado)

Este comando compilará el código, construirá las imágenes Docker y levantará todo el ecosistema (Base de datos, App y Mock).

```bash
docker-compose up --build
```
Espera a ver el mensaje: Started CreditapplicationApplication en los logs.

## 🧪 Cómo Probar el Programa (Paso a Paso)

Usaremos **Postman** o cualquier cliente HTTP.

### 1. Registrarse (Obtener Acceso)

Crea un usuario con salario suficiente para pasar las reglas de negocio.

* **Método:** `POST`
* **URL:** `http://localhost:8080/auth/register`

**Body (JSON):**

```json
{
    "username": "usuario_rico",
    "password": "securePass123!",
    "fullName": "Elon Musk",
    "email": "elon@test.com",
    "address": "Silicon Valley",
    "salary": 10000000
}
```
Importante: De la respuesta que recibas, copia el token. Lo necesitarás para los siguientes pasos.

### 2. Solicitar un Crédito

* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/credits`
* **Headers:**
    * `Authorization: Bearer <PEGA_TU_TOKEN_AQUI>`

**Body (JSON):**

```json
{
    "amount": 150000,
    "termMonths": 12
}
```

Respuesta Esperada (200 OK):
```json
{
    "status": "APPROVED",
    "amount": 150000,
    "riskEvaluation": { "score": 85 }
}
```

### Prueba de Rechazo (Validación de Negocio)

Intenta pedir un crédito impagable.

**Body (JSON):**

```json
{ "amount": 100000000, "termMonths": 6 }
```
Respuesta Esperada (400 Bad Request):

```JSON
{
    "title": "Business Rule Violation",
    "detail": "Loan rejected: Calculated monthly quota (...) exceeds 50% of your monthly income..."
}
```