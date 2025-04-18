# Microservicio de Autenticación con Jakarta EE y MicroProfile

## 📋 Descripción
En el presente proyecto es un ejemplo de la implementación de un microservicio de autenticación desarrollado con Jakarta EE 10 y MicroProfile, siguiendo los principios de la arquitectura hexagonal (ports and adapters). 
El cual brinda funcionalidades robustas de autenticación y autorización utilizando JWT (JSON Web Tokens) y seguridad basada en roles.

## ⚡ Características Principales

### 🔐 Seguridad
- Autenticación basada en JWT
    - Generación de tokens JWT al iniciar sesión
    - Validación de tokens en cada solicitud
    - Almacenamiento seguro de claves secretas
- Autorización basada en roles (RBAC) 
    - Roles predefinidos: "user" y "admin"
    -  Jakarta Security para gestión de roles
  - Configuración de roles en el archivo `web.xml`
- Control de acceso granular a nivel de método
    - Anotaciones `@PermitAll`, `@DenyAll`, `@RolesAllowed`
- Rate Limiting para prevenir ataques de fuerza bruta
    - Implementación de Rate Limiting mediante anotación `@RateLimit`
    - Configuración de límites por tiempo y peticiones

### 🏗️ Arquitectura
- Arquitectura Hexagonal (Ports and Adapters)
    - **Domain**: Entidades y lógica de negocio core
        - Entidad `Usuario` con atributos como nombre, email y contraseña
        - Entidad `Rol` para gestión de roles
        - Anotaciones personalizadas para marcar entidades y casos de uso
    - **Application**: Casos de uso e interfaces de puertos
        - Puertos de entrada (Input Ports)
        - Puertos de salida (Output Ports)
    - **Infrastructure**: Adaptadores y configuraciones 
        - Controladores REST
        - Persistencia (JPA)
        - Seguridad (JWT, Rate Limiting)

### 🛠️ Características Técnicas
- Framework: Jakarta EE 10
- Servidor de Aplicaciones: WildFly 35.0.1.Final
- Base de Datos: MySQL
- Gestión de Dependencias: Maven
- Java Version: 21

## 🔧 Estructura del Proyecto

### Domain Layer
- `Usuario.java`: Entidad principal del dominio
- `Rol.java`: Entidad para gestión de roles
- Anotaciones personalizadas: `@Domain`, `@UseCase`, `@Port`, `@Adapter`

### Application Layer
#### Ports (Input/Output)
- `RegistrarUseCase.java`: Puerto de entrada para registro
- `AutenticarUseCase.java`: Puerto de entrada para autenticación
- `UsuarioPersistenciaPort.java`: Puerto de salida para persistencia

#### Use Cases
- `UsuarioUseCaseImpl.java`: Implementación de casos de uso
    - Registro de usuarios
    - Autenticación de usuarios

### Infrastructure Layer
#### REST Controllers
- `AutenticacionController.java`
    - Endpoints para registro y login
    - Implementación de Rate Limiting
    - Gestión de tokens JWT

- `SeguridadController.java`
    - Endpoints protegidos por roles
    - Integración con SecurityContext

#### Persistence
- JPA Repositories
- Mappers para conversión DTO-Entity

## 🚀 Configuración y Despliegue

### Requisitos Previos
- JDK 21
- Maven 3.8+
- WildFly 35.0.1.Final
- MySQL 8.0+

### Configuración de WildFly
1. Configurar Standalone.xml para habilitar MicroProfile y JWT

- Ejecutar el servidor con las configuraciones de standalone-microprofile.xml

2. Configurar el driver MySQL en WildFly

- Descargar el driver MySQL Connector/J y colocarlo en el directorio de módulos de WildFly.
- Crear un módulo para el driver MySQL en `$WILDFLY_HOME\modules\system\layers\base\com\mysql\main` con el siguiente contenido:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module name="com.mysql" xmlns="urn:jboss:module:1.9">
  <resources>
    <resource-root path="mysql-connector-j-8.0.33.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
```
- Registrar el driver en el archivo `standalone-microprofile.xml` de WildFly:

```xml
<driver name="mysql" module="com.mysql">
  <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
  <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
</driver>
```

-  Configuración del DataSource en el standalone-microprofile.xml de Wildfly

```xml
<datasource jndi-name="java:/MySqlDS" pool-name="MySqlDS" statistics-enabled="true">
  <connection-url>jdbc:mysql://localhost:3306/database</connection-url>
  <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
  <driver>mysql</driver>
  <security user-name="root" password="password"/>
</datasource>
```

3. Configuración de la Unidad de Persistencia
- Crear el archivo `persistence.xml` en `src/main/resources/META-INF/` con la siguiente configuración:
```xml
<persistence-unit name="PrimaryPU" transaction-type="JTA"> 
  <jta-data-source>java:/MySqlDS</jta-data-source>      
</persistence-unit>
```

### Construcción y Despliegue

# Compilar el proyecto

```bash
mvn clean package
```
# Desplegar en WildFly

```bash
cp target/jakarta-microprofile-auth.war $WILDFLY_HOME/standalone/deployments/
```
## 📚 Dependencias Principales
- Jakarta EE 10.0.0
- MicroProfile JWT 2.1
- MicroProfile Config 3.1
- MicroProfile Rest Client 4.0
- Hibernate ORM 6.6.12.Final
- JJWT 0.12.3
- Lombok 1.18.34

## 🔒 Seguridad y Rate Limiting
- Implementación de Rate Limiting mediante anotación `@RateLimit`
- Configuración de límites por tiempo y peticiones
- Gestión de tokens JWT con firma y validación

## 📝 API Endpoints

### Autenticación

- POST /autenticacion/register - Registro de nuevos usuarios

- POST /autenticacion/login - Inicio de sesión

### Endpoints Protegidos

- GET /ruta/protegido - Acceso solo para admin 
- GET /ruta/saludo - Acceso para usuarios autenticados 
- GET /ruta/helloworld - Endpoint público

