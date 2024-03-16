package com.example.githubsearcher.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearcher.database.Favorite
import com.example.githubsearcher.database.FavoriteDao
import com.example.githubsearcher.database.FavoriteDatabase
import com.example.githubsearcher.repo.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository =FavoriteRepository(application)
    private var favDatabase: FavoriteDatabase? = FavoriteDatabase.getDatabase(application)
    private var favoritesDao: FavoriteDao? = favDatabase?.favoriteDao()

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()

    fun insert(favEntity: Favorite){
        mFavoriteRepository.insert(favEntity)
    }
    fun delete(favEntity: Favorite){
        mFavoriteRepository.delete(favEntity)
    }

    fun check(id: Int) = favoritesDao?.check(id)
}