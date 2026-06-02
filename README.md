# 🌵 Tuta Wayta — Sistema de Gestión

## Requisitos
- Java 17+
- Maven 3.8+
- Docker Desktop
- IntelliJ IDEA

## 1. Levantar Docker MySQL (puerto 3307)

```bash
docker run -d \
  --name pitahaya_mysql \
  -e MYSQL_ROOT_PASSWORD=root1234 \
  -p 3307:3306 \
  mysql:8.0
```

## 2. Crear la base de datos

Abre MySQL Workbench → conecta a localhost:3307 con user `root` / pass `root1234`  
Ejecuta el archivo `database.sql`

## 3. Abrir en IntelliJ IDEA

1. File → Open → selecciona la carpeta `TutaWaytaApp`
2. IntelliJ detecta el `pom.xml` automáticamente → **Trust Project**
3. Espera que descargue dependencias (Maven sync)
4. Arriba a la derecha verás la config **TutaWaytaApp** → click en ▶ **Run**

## 4. Ejecutar manualmente (alternativa)

```bash
mvn javafx:run
```

## Estructura del proyecto

```
TutaWaytaApp/
├── pom.xml
├── database.sql
└── src/main/
    ├── java/vallegrande/edu/pe/tutawayta/
    │   ├── MainApp.java
    │   ├── controller/  (ClienteController, PedidoController, ProductoController, WelcomeController)
    │   ├── dao/         (ClienteDAO, PedidoDAO, ProductoDAO)
    │   ├── model/       (Cliente, Pedido, Producto)
    │   └── util/        (DatabaseConnection)
    └── resources/vallegrande/edu/pe/tutawayta/view/
        ├── Welcome.fxml
        ├── Clientes.fxml
        ├── Productos.fxml
        ├── Pedidos.fxml
        └── styles.css
```

## Conexión DB (DatabaseConnection.java)

- Host: `localhost`
- Puerto: `3307`
- DB: `tuta_wayta_db`
- User: `root`
- Password: `root1234`
