package com.example.travelbook.signIn.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SignInStateDao {
    @Query("SELECT * FROM sign_in_state_table ORDER BY id ASC")
    fun currentSignInState(): Flow<SignInState>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignInState(signInState: SignInState)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSignInState(signInState: SignInState)
}