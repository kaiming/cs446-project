package com.example.travelbook

import android.content.Context

data class User(
    val id: String,
    val name: String?,
    val email: String?
)

interface UserDataSource {
    fun saveUser(user: User)
    fun getUser(): User?
    fun deleteUser()
}

class SharedPreferencesUserDataSource(private val context: Context) : UserDataSource {

    private val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    override fun saveUser(user: User) {
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

    override fun deleteUser() {
        val editor = sharedPreferences.edit()
        editor.remove("USER_ID")
        editor.remove("USER_NAME")
        editor.remove("USER_EMAIL")
        editor.apply()
    }
}