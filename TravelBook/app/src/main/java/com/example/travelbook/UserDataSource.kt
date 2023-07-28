package com.example.travelbook

import android.content.Context
import android.util.Log

data class User(
    val id: String,
    val name: String?,
    val email: String?
)

interface UserDataSource {
    fun saveUser(user: User)
    fun getUser(): User?
    fun deleteUser()
    fun getUserId(): String?
    fun getUserName(): String?
    fun getUserEmail(): String?
}

class SharedPreferencesUserDataSource(private val context: Context) : UserDataSource {
    // Singleton
    companion object {
        
        @Volatile
        private var INSTANCE: SharedPreferencesUserDataSource? = null

        fun getInstance(context: Context): SharedPreferencesUserDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = SharedPreferencesUserDataSource(context)
                INSTANCE = instance
                instance
            }
        }

    }

    private val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    override fun saveUser(user: User) {
        Log.d("SharedPreferencesUserDataSource", "Saving user: $user")
        val editor = sharedPreferences.edit()
        editor.putString("USER_ID", user.id)
        editor.putString("USER_NAME", user.name)
        editor.putString("USER_EMAIL", user.email)
        editor.apply()
    }

    override fun getUser(): User? {
        val userId = sharedPreferences.getString("USER_ID", null) ?: return null
        val userName = sharedPreferences.getString("USER_NAME", null)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)
        return User(userId, userName, userEmail)
    }

    override fun getUserId(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }

    override fun getUserName(): String? {
        return sharedPreferences.getString("USER_NAME", null)
    }

    override fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", null)
    }

    override fun deleteUser() {
        val editor = sharedPreferences.edit()
        editor.remove("USER_ID")
        editor.remove("USER_NAME")
        editor.remove("USER_EMAIL")
        editor.apply()
    }
}