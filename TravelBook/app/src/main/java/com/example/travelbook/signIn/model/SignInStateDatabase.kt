package com.example.travelbook.signIn.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SignInState::class],
    version = 1,
    exportSchema = false
)
abstract class SignInStateDatabase: RoomDatabase() {
    abstract fun SignInStateDao(): SignInStateDao

    companion object {
        @Volatile
        private var INSTANCE: SignInStateDatabase? = null

        fun getDatabase(context: Context): SignInStateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SignInStateDatabase::class.java,
                    "sign_in_state_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}