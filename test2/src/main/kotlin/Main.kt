package org.example

import java.sql.SQLException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

data class Usuario(val nombre: String, val email: String)
data class Producto(val nombre: String, val precio: Double, val stock: Int)
data class Pedido(val precioTotal: Double, val idUsuario: Int)
data class LineaPedido(val idPedido: Int, val idProducto: Int, val cantidad: Int, val precio: Double)

fun main() {
    /*Ejercicios 1-5*/
    /*
    val url = "jdbc:h2:./BDCarperta2/BDPrueba2"
    val user = "user"
    val password = "password"
    var conexion: Connection? = null
     */
    /*Ejercicio 6*/
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:h2:./BDCarperta2/BDPrueba2"
        username = "user"
        password = "password"
        maximumPoolSize = 5
    }
    val dataSource = HikariDataSource(config)


    try {
        /*Ejercicios 1-5*/
        /*
        Class.forName("org.h2.Driver")
        conexion = DriverManager.getConnection(url, user, password)
        println("Conexión exitosa")
         */

        /*Ejercicio 2*/
        /*
        crearTablas(conexion)

        insertarUsuarios(conexion)
        insertarProductos(conexion)
        insertarPedidos(conexion)
        insertarLineasPedido(conexion)
        */
        /*Ejercicio 3*/
        /*
        mostrarLineasPedidoId1(conexion)
        mostrarTotalPedidosUsuario(conexion)
        mostrarUsuariosQueCompraronAbanico(conexion)
        */
        /*Ejecicio 4*/
        /*
        eliminarUsuarioCornelio(conexion)
        eliminarProducto25Euros(conexion)
        eliminarPedidoId3(conexion)

         */
        /*Ejercicio 5*/
        /*
        modificarPrecioDeAbanicoAOferta(conexion)
        modificarLineaPedidoId3(conexion)
         */
        /*Ejercicio 6*/
        /*
        insertarUsuarioReinaldo(dataSource)
        mostrarPedidosDeFacundo(dataSource)
         */

    } catch (e: SQLException) {
        println("Error de base de datos: ${e.message}")
    } catch (e: ClassNotFoundException) {
        println("No se encontró el driver JDBC: ${e.message}")
    } finally {
        /* Ejercicio 1-5*/
        /*
        conexion?.close()
         */
        /* Ejercicio 6*/
        /*
        dataSource.close()
         */
        println("Conexión cerrada")
    }
}

/*Ejercicio 2*/
fun crearTablas(conexion: Connection) {
    val stm = conexion.createStatement()

    stm.execute(
        """
        CREATE TABLE IF NOT EXISTS Usuario (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(255) NOT NULL,
            email VARCHAR(255) UNIQUE
        );
        """
    )

    stm.execute(
        """
        CREATE TABLE IF NOT EXISTS Producto (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(255) NOT NULL,
            precio DECIMAL NOT NULL,
            stock INT NOT NULL
        );
        """
    )

    stm.execute(
        """
        CREATE TABLE IF NOT EXISTS Pedido (
            id INT AUTO_INCREMENT PRIMARY KEY,
            precioTotal DECIMAL NOT NULL,
            idUsuario INT,
            FOREIGN KEY (idUsuario) REFERENCES Usuario(id)
        );
        """
    )

    stm.execute(
        """
        CREATE TABLE IF NOT EXISTS LineaPedido (
            id INT AUTO_INCREMENT PRIMARY KEY,
            cantidad INT NOT NULL,
            precio DECIMAL NOT NULL,
            idPedido INT,
            idProducto INT,
            FOREIGN KEY (idPedido) REFERENCES Pedido(id),
            FOREIGN KEY (idProducto) REFERENCES Producto(id)
        );
        """
    )

    stm.close()
    println("Tablas creadas exitosamente (si no existían de antes).")
}


fun insertarUsuarios(con: Connection) {
    val usuarios = listOf(
        Usuario("Facundo Pérez", "facuper@mail.com"),
        Usuario("Ataulfo Rodríguez", "ataurod@mail.com"),
        Usuario("Cornelio Ramírez", "Cornram@mail.com")
    )

    val sql = "INSERT INTO usuario (nombre, email) VALUES (?, ?)"
    val stmt: PreparedStatement = con.prepareStatement(sql)
    for (u in usuarios) {
        stmt.setString(1, u.nombre)
        stmt.setString(2, u.email)
        stmt.executeUpdate()
    }
    stmt.close()
}

fun insertarProductos(con: Connection) {
    val productos = listOf(
        Producto("Ventilador", 10.0, 2),
        Producto("Abanico", 150.0, 47),
        Producto("Estufa", 24.99, 1)
    )

    val sql = "INSERT INTO producto (nombre, precio, stock) VALUES (?, ?, ?)"
    val stmt: PreparedStatement = con.prepareStatement(sql)
    for (p in productos) {
        stmt.setString(1, p.nombre)
        stmt.setDouble(2, p.precio)
        stmt.setInt(3, p.stock)
        stmt.executeUpdate()
    }
    stmt.close()
}

fun insertarPedidos(con: Connection) {
    val pedidos = listOf(
        Pedido(160.0, 2),
        Pedido(20.0, 1),
        Pedido(150.0, 2)
    )

    val sql = "INSERT INTO pedido (precioTotal, idUsuario) VALUES (?, ?)"
    val stmt: PreparedStatement = con.prepareStatement(sql)
    for (p in pedidos) {
        stmt.setDouble(1, p.precioTotal)
        stmt.setInt(2, p.idUsuario)
        stmt.executeUpdate()
    }
    stmt.close()
}

fun insertarLineasPedido(con: Connection) {
    val lineas = listOf(
        LineaPedido(1, 1, 1, 10.0),
        LineaPedido(1, 2, 1, 150.0),
        LineaPedido(2, 1, 2, 20.0),
        LineaPedido(3, 2, 1, 150.0)
    )

    val sql = "INSERT INTO lineaPedido (idPedido, idProducto, cantidad, precio) VALUES (?, ?, ?, ?)"
    val stmt: PreparedStatement = con.prepareStatement(sql)
    for (l in lineas) {
        stmt.setInt(1, l.idPedido)
        stmt.setInt(2, l.idProducto)
        stmt.setInt(3, l.cantidad)
        stmt.setDouble(4, l.precio)
        stmt.executeUpdate()
    }
    stmt.close()
}
/*Ejercicio 3*/
fun mostrarLineasPedidoId1(con: Connection) {
    println("Líneas de pedido del pedido con ID = 1:")
    val sql = "SELECT * FROM LINEAPEDIDO WHERE idPedido = ?"
    val stmt = con.prepareStatement(sql)
    stmt.setInt(1, 1)
    val rs = stmt.executeQuery()

    while (rs.next()) {
        println("ID: ${rs.getInt("id")}, Cantidad: ${rs.getInt("cantidad")}, Precio: ${rs.getDouble("precio")}, ID Producto: ${rs.getInt("idProducto")}")
    }

    stmt.close()
}
fun mostrarTotalPedidosUsuario(con: Connection) {
    println("\nSuma total de los pedidos de Ataulfo Rodríguez:")
    val sql = """
        SELECT SUM(PEDIDO.precioTotal) AS TOTAL 
        FROM PEDIDO
        JOIN USUARIO ON PEDIDO.idUsuario = USUARIO.id 
        WHERE USUARIO.nombre = ?
    """
    val stmt = con.prepareStatement(sql)
    stmt.setString(1, "Ataulfo Rodríguez")
    val rs = stmt.executeQuery()

    if (rs.next()) {
        println("Total: ${rs.getDouble("total")} €")
    }

    stmt.close()
}
fun mostrarUsuariosQueCompraronAbanico(con: Connection) {
    println("\nUsuarios que compraron un 'Abanico':")
    val sql = """
        SELECT DISTINCT USUARIO.nombre 
        FROM USUARIO
        JOIN PEDIDO ON USUARIO.id = PEDIDO.idUsuario
        JOIN LINEAPEDIDO LINEAPEDIDO ON USUARIO.id = LINEAPEDIDO.idPedido
        JOIN PRODUCTO ON LINEAPEDIDO.idProducto = PRODUCTO.id
        WHERE PRODUCTO.nombre = ?
    """
    val stmt = con.prepareStatement(sql)
    stmt.setString(1, "Abanico")
    val rs = stmt.executeQuery()

    while (rs.next()) {
        println(rs.getString("nombre"))
    }

    stmt.close()
}
/*Ejercicio 4*/
fun eliminarUsuarioCornelio(con: Connection){
    val query = "DELETE FROM USUARIO WHERE nombre = ?"
    val stmt = con.prepareStatement(query)
    stmt.setString(1, "Cornelio Ramírez")
    val rowsDeleted = stmt.executeUpdate()
    if (rowsDeleted > 0) {
        println("Usuario eliminado exitosamente.")
    } else {
        println("No se ha eliminado ningún usuario.")
    }
    stmt.close()
}


fun eliminarProducto25Euros(con: Connection){
    val query = "DELETE FROM PRODUCTO WHERE precio = ?"
    val stmt = con.prepareStatement(query)
    stmt.setDouble(1, 24.99)
    val rowsDeleted = stmt.executeUpdate()
    if (rowsDeleted > 0) {
        println("Producto eliminado exitosamente.")
    } else {
        println("No se ha eliminado ningún producto.")
    }
    stmt.close()
}

fun eliminarPedidoId3(con: Connection){
    val deleteLineas = "DELETE FROM LINEAPEDIDO WHERE idPedido = ?"
    val stmtLineas = con.prepareStatement(deleteLineas)
    stmtLineas.setInt(1, 3)
    stmtLineas.executeUpdate()
    stmtLineas.close()

    val deletePedido = "DELETE FROM PEDIDO WHERE id = ?"
    val stmtPedido = con.prepareStatement(deletePedido)
    stmtPedido.setInt(1, 3)
    val rowsDeleted = stmtPedido.executeUpdate()
    if (rowsDeleted > 0) {
        println("Pedido eliminado exitosamente.")
    } else {
        println("No se ha eliminado ningún pedido.")
    }
    stmtPedido.close()
}

/*Ejercicio 5*/
fun modificarPrecioDeAbanicoAOferta(con: Connection) {
    val sql = "UPDATE Producto SET precio = precio * 0.7 WHERE nombre = ?"
    val stmt = con.prepareStatement(sql)
    stmt.setString(1, "Abanico")
    val rowsUpdated = stmt.executeUpdate()
    if (rowsUpdated > 0) {
        println("Precio del 'Abanico' actualizado con un 30% de descuento.")
    } else {
        println("No se encontró el producto 'Abanico'.")
    }
    stmt.close()
}


fun modificarLineaPedidoId3(con: Connection) {
    try {
        val selectPrecio = "SELECT precio FROM Producto WHERE nombre = ?"
        val stmtSelect = con.prepareStatement(selectPrecio)
        stmtSelect.setString(1, "Abanico")
        val rs = stmtSelect.executeQuery()

        if (rs.first()) {
            val precioAbanico = rs.getDouble("precio")
            val nuevoPrecio = precioAbanico * 2

            val updateLinea = "UPDATE LineaPedido SET idProducto = ?, precio = ? WHERE id = ?"
            val stmtUpdate = con.prepareStatement(updateLinea)
            stmtUpdate.setInt(1, 2)
            stmtUpdate.setDouble(2, nuevoPrecio)
            stmtUpdate.setInt(3, 3)

            val updated = stmtUpdate.executeUpdate()
            if (updated > 0) {
                println("Línea de pedido con ID 3 actualizada correctamente.")
            } else {
                println("No se actualizó ninguna línea de pedido.")
            }

            stmtUpdate.close()
        } else {
            println("No se encontró el producto 'Abanico'.")
        }

        rs.close()
        stmtSelect.close()
    } catch (e: SQLException) {
        println("Error al modificar la línea de pedido: ${e.message}")
    }
}

/*Ejercicio 6*/
fun insertarUsuarioReinaldo(ds: HikariDataSource) {
    val sql = "INSERT INTO USUARIO (nombre, email) VALUES (?, ?)"
    val con = ds.connection
    val stmt = con.prepareStatement(sql)
    stmt.setString(1, "Reinaldo Girúndez")
    stmt.setString(2, "reingir@mail.com")
    val rows = stmt.executeUpdate()
    if (rows > 0) println("Usuario Reinaldo insertado correctamente.")
    stmt.close()
    con.close()
}

fun mostrarPedidosDeFacundo(ds: HikariDataSource) {
    val sql = """
        SELECT PEDIDO.id, PEDIDO.precioTotal 
        FROM PEDIDO 
        JOIN USUARIO ON PEDIDO.idUsuario = USUARIO.id 
        WHERE USUARIO.nombre = ?
    """
    val con = ds.connection
    val stmt = con.prepareStatement(sql)
    stmt.setString(1, "Facundo Pérez")
    val rs = stmt.executeQuery()
    println("Pedidos de Facundo Pérez:")
    while (rs.next()) {
        println("Pedido ID: ${rs.getInt("id")}, Total: ${rs.getDouble("precioTotal")} €")
    }
    rs.close()
    stmt.close()
    con.close()
}
