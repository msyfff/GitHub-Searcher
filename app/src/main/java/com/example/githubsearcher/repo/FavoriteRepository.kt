package com.example.githubsearcher.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubsearcher.database.Favorite
import com.example.githubsearcher.database.FavoriteDao
import com.example.githubsearcher.database.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteDao.getAllNotes()

    fun insert(favEntity: Favorite) {
        executorService.execute { mFavoriteDao.insert(favEntity) }
    }

    fun delete(favEntity: Favorite) {
        executorService.execute { mFavoriteDao.delete(favEntity) }
    }


}