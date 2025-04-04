package org.example

import java.sql.SQLException
import java.sql.Connection
import java.sql.DriverManager
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val url = "jdbc:h2:./BDCarperta2/BDPrueba2"
    val user = "user"
    val password = "password"
    try{
        Class.forName("org.h2.Driver")
        val conexion = DriverManager.getConnection(url, user, password)
        println("Conexión exitosa")
        conexion.close()
    } catch (e: SQLException) {
        println("Error en la conexión: ${e.message}")
    }
}
/*Manejo de errores en la conexión*/
