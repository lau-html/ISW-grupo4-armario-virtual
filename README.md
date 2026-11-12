# dressB

dressB es una aplicación de escritorio en Java para tener tu armario en el ordenador. Puedes guardar tus prendas con foto, filtrarlas, crear tus propias categorías y hacer outfits combinando prendas.

Es el proyecto del Grupo 4 de Ingeniería del Software (ICAI): Irene Guzmán, Martina Cantalapiedra, Sakshi, Laura Rodenas y Cristina Álvarez-Buiza.

## Qué hace ahora mismo

- Registro e inicio de sesión
- Añadir, editar y borrar prendas, con foto (JPG, JPEG o PNG)
- Ver el detalle de una prenda con doble clic y marcarla como favorita
- Filtrar las prendas con varios filtros a la vez
- Crear categorías personalizadas
- Crear outfits, verlos y borrarlos

Para el Sprint 3 queremos añadir el outfit aleatorio, los outfits favoritos y poder borrar categorías.

## Cómo ejecutarlo

Hace falta tener instalado el JDK 17 y Maven.

1. Clonar el repositorio y abrirlo en IntelliJ.
2. Ejecutar la clase `JVentana`, que está en `src/main/java/icai/dtc/isw/ui`. Se abre la ventana de inicio de sesión.

No hace falta arrancar el servidor, la aplicación se conecta directamente a la base de datos.

También se puede generar el .jar y ejecutarlo:

```
mvn clean package
java -jar target/client_isw.jar
```

## Tests

Se ejecutan con `mvn test`. También se lanzan solos con GitHub Actions cada vez que se sube algo a `dev` o `main`, o se abre un Pull Request.

## Ramas

Trabajamos con `main` (lo que se entrega) y `dev` (desarrollo). Para cada tarea creamos una rama `feature/...` desde `dev`, abrimos un Pull Request y, cuando se hace el merge, borramos la rama.

## Cuenta de prueba

- Correo: correovictoria@dressB.com
- Contraseña: victorialamejorponnosun10!