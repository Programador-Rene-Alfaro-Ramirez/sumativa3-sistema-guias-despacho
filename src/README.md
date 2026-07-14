# Sistema de Guías de Despacho - Sumativa 3

## Descripción
Proyecto de arquitectura Cloud Native desarrollado para el módulo de programación. Implementa un flujo de procesamiento asíncrono utilizando Spring Boot, RabbitMQ, Oracle Cloud y AWS S3.

## Configuración de Seguridad
Por razones de seguridad, este proyecto utiliza variables de entorno para gestionar las credenciales de conexión a la base de datos. No se incluyen contraseñas reales en el código fuente.

### Variables requeridas
Antes de ejecutar la aplicación, debes configurar la siguiente variable de entorno en tu sistema:

* `DB_PASSWORD`: La contraseña de acceso a la base de datos Oracle configurada.

### Ejemplo de ejecución (Windows PowerShell)
Para ejecutar el proyecto en tu entorno local, abre la terminal en la raíz del proyecto y utiliza los siguientes comandos:

1. Configurar la variable (reemplaza 'tu_contraseña_aqui' con tu clave real):
   $env:DB_PASSWORD="tu_contraseña_aqui"

2. Ejecutar la aplicación:
   ./mvnw spring-boot:run