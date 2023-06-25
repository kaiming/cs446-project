package com.example.travelbook.signIn.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sign_in_state_table")
data class SignInState(
    @ColumnInfo(name = "state") val state: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)