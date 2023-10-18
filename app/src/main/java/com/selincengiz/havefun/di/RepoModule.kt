package com.selincengiz.havefun.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.data.repo.CategoryRepo
import com.selincengiz.havefun.data.repo.EventRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideRepo( db: FirebaseFirestore) =
        EventRepo(db = db)

    @Provides
    @Singleton
    fun provideCategoryRepo( db: FirebaseFirestore) =
        CategoryRepo(db = db)
}