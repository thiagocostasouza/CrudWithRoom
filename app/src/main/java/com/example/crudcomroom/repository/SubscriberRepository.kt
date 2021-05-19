package com.example.crudcomroom.repository

import androidx.lifecycle.LiveData
import com.example.crudcomroom.data.db.entity.SubscriberEntity

interface SubscriberRepository {

    suspend fun insertSubscriber(name: String, email: String): Long

    suspend fun updateSubscriber(id: Long, name: String, email: String)

    suspend fun deleteSubscriber(id: Long)

    suspend fun deleteAllSubscribers()

    suspend fun getAllSubscribers(): List<SubscriberEntity>
}