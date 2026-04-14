# Sistema de Gestión de Productos de Almacén
**Estudiante:** Roy Ramirez  
**Matrícula:** 2024-1784  
**Proyecto Final — Programación I**

---

## Requisitos previos

Antes de correr el proyecto necesitas tener instalado:

| Herramienta | Versión recomendada | Descarga |
|---|---|---|
| JDK (Java Development Kit) | 11 o superior | https://adoptium.net |
| Eclipse IDE for Java | 2022 o superior | https://eclipse.org/downloads |
| MySQL Connector/J | 8.x | https://dev.mysql.com/downloads/connector/j/ |

> **Nota:** La base de datos ya está en la nube No necesitas instalar MySQL localmente.

---

## Cómo descargar el MySQL Connector 

1. Ve a: https://dev.mysql.com/downloads/connector/j/
2. En "Select Operating System" elige **Platform Independent**
3. Descarga el archivo **ZIP**
4. Extrae el ZIP — busca el archivo que termina en `.jar`, algo como `mysql-connector-j-8.x.x.jar`
5. Copia ese `.jar` a la carpeta `lib/` del proyecto

---

## Importar el proyecto en Eclipse

### Paso 1 — Abrir Eclipse y crear el workspace
1. Abre Eclipse
2. Cuando te pida el workspace, selecciona cualquier carpeta o deja la predeterminada
3. Haz clic en **Launch**

### Paso 2 — Crear el proyecto Java
1. `File > New > Java Project`
2. En **Project name** escribe el nombre que quieras
3. **Desactiva** "Use default location"
4. Haz clic en **Browse...** y navega hasta la carpeta donde esta el proyecto
5. Haz clic en **Finish**

### Paso 3 — Agregar el MySQL Connector al Build Path
Esta es la parte más importante. Sin el driver JDBC, la aplicación no puede conectarse a la base de datos.

1. En el **Package Explorer** (panel izquierdo), haz clic derecho en tu proyecto
2. Selecciona **Build Path > Configure Build Path...**
3. Ve a la pestaña **Libraries**
4. Haz clic en **Add External JARs...**
5. Navega hasta la carpeta **lib/** del proyecto donde esta el `.jar` del MySQL Connector
6. Selecciónalo y haz clic en **Open**
7. Haz clic en **Apply and Close**

### Paso 4 — Verificar la estructura del proyecto
En el Package Explorer deberías ver:
```
SistemaAlmacen/
├── src/
│   ├── Main.java
│   ├── database/
│   │   └── DatabaseConnection.java
│   ├── model/
│   │   ├── Entidad.java
│   │   ├── Usuario.java
│   │   └── Producto.java
│   ├── dao/
│   │   ├── GenericDAO.java
│   │   ├── UsuarioDAO.java
│   │   └── ProductoDAO.java
│   └── view/
│       ├── VentanaBase.java
│       ├── LoginFrame.java
│       ├── RegistroFrame.java
│       ├── PrincipalFrame.java
│       ├── GestionUsuariosFrame.java
│       ├── UsuarioDialogo.java
│       ├── GestionProductosFrame.java
│       └── ProductoDialogo.java
└── lib/
    └── mysql-connector-j-x.x.x.jar
```

---

## Ejecutar la aplicación

1. En el Package Explorer, haz clic en **`Main.java`**
2. Presiona **Ctrl+F11** (o clic derecho → **Run As > Java Application**)
3. La aplicación abrirá la pantalla de **Login**

### Primer uso
Si es la primera vez que corres el programa y no hay usuarios registrados:
1. En la pantalla de Login haz clic en **"Registrarse"**
2. Llena todos los campos y haz clic en **"Registrar"**
3. Vuelve al Login e inicia sesión con tu usuario y contraseña

---

## Conexión a la base de datos

El proyecto se conecta automáticamente al servidor MySQL remoto de Aiven:

```
Host:     almacenitla-db-itla-3837.e.aivencloud.com:25037
Base de datos: almacenitlafinal
Usuario:  avnadmin
```

La conexión usa **SSL** y no requiere ninguna configuración adicional. Solo necesitas estar conectado a internet.

### ¿Qué pasa si no conecta?
Revisa la consola de Eclipse (panel inferior). Si ves:
- `[DB] ERROR: No se encontro el driver MySQL` → El JAR del conector no está en el Build Path (ver Paso 3)
- `[DB] ERROR al conectar a la base de datos` → Verifica tu conexión a internet

---

## Estructura de la base de datos

### Tabla `usuarios`
| Campo | Tipo |
|---|---|
| idUser | int (PK, auto-increment) |
| UserName | varchar(140) |
| Nombre | varchar(140) |
| Apellido | varchar(140) |
| Telefono | varchar(140) |
| Email | varchar(140) |
| Password | varchar(140) |

### Tabla `productos`
| Campo | Tipo |
|---|---|
| idProducto | int (PK, auto-increment) |
| NombreProducto | varchar(140) |
| MarcaProducto | varchar(140) |
| CategoriaProducto | varchar(140) |
| PrecioProducto | int |
| StockProducto | int |

---

## Guía de uso del sistema

### Login
- Ingresa tu **nombre de usuario** y **contraseña**
- La contraseña se oculta automáticamente mientras escribes
- Si no tienes cuenta, haz clic en **"Registrarse"**
- Si algún campo está vacío al presionar "Entrar", aparece un mensaje de error

### Registro
- Todos los campos son **obligatorios**
- Las contraseñas deben **coincidir**
- El nombre de usuario debe ser **único**

### Menú Principal
- Dos botones grandes: **Usuarios** y **Productos**
- Botón **"Cerrar Sesion"** en la barra superior derecha

### Gestión de Usuarios
- La tabla muestra todos los usuarios registrados
- **+ Nuevo**: crea un nuevo usuario
- **Actualizar**: edita el usuario seleccionado en la tabla
- **Eliminar**: elimina el usuario seleccionado (pide confirmación)
- **← Volver**: regresa al menú principal

### Gestión de Productos
- La tabla muestra todos los productos con todos sus datos
- **+ Nuevo Producto**: abre formulario para registrar un producto
- **Clic en una fila**: abre el formulario del producto para editarlo o eliminarlo
  - **Guardar**: guarda los cambios y cierra el diálogo
  - **Eliminar**: elimina el producto y cierra el diálogo
- **← Volver**: regresa al menú principal
