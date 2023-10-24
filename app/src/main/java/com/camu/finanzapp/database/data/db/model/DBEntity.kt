package com.camu.finanzapp.database.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camu.finanzapp.util.Constants

// Anotación @Entity indica que esta clase representa una entidad de la base de datos
@Entity(tableName = Constants.DATABASE_NAME_TABLE)
data class DBEntity (

    // Anotación @PrimaryKey indica que 'id' es la clave primaria y se genera automáticamente (autoGenerate = true)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Long = 0,

    // Columna 'nickname' que almacena el apodo del usuario
    @ColumnInfo(name = "user_nickname")
    val nickname: String,

    // Columna 'name' que almacena el nombre del usuario
    @ColumnInfo(name = "user_name")
    val name: String,

    // Columna 'lastname' que almacena el apellido del usuario
    @ColumnInfo(name = "user_lastname")
    val lastname: String,

    // Columna 'sex' que almacena el género del usuario
    @ColumnInfo(name = "user_sex")
    val sex: String,

    // Columna 'email' que almacena el correo electrónico del usuario
    @ColumnInfo(name = "user_email")
    val email: String,

    // Columna 'key' que almacena una clave del usuario
    @ColumnInfo(name = "user_key")
    val key: String
)
