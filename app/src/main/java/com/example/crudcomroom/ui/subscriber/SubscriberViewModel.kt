package com.example.crudcomroom.ui.subscriber

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudcomroom.R
import com.example.crudcomroom.repository.SubscriberRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SubscriberViewModel(
    private val repository: SubscriberRepository
) : ViewModel() {

    private val _subscriberStateEventData = MutableLiveData<SubscriberState>()
    val subscriberStateEventData: LiveData<SubscriberState>
        get() = _subscriberStateEventData

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData: LiveData<Int>
        get() = _messageStateEventData

    fun addOrUpadateSubscriber(name:String, email:String, id:Long = 0){
        if(id >0 ){
                updateSubscriber(id, name, email)
        }else {
            insertSubscriber(name,email)
        }

    }

    private fun updateSubscriber(id: Long, name: String,email: String) = viewModelScope.launch {
        try {
            repository.updateSubscriber(id, name, email)
            _subscriberStateEventData.value = SubscriberState.Updated
            _messageStateEventData.value = R.string.subscriber_updated_sucessfully
        }catch (ex: Exception){
            _messageStateEventData.value = R.string.subscriber_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

   private fun insertSubscriber(name: String, email: String) = viewModelScope.launch {
        try {
            val id = repository.insertSubscriber(name, email)
            if (id > 0) {
                _subscriberStateEventData.value = SubscriberState.Inserted
                _messageStateEventData.value = R.string.subscriber_inserted_sucessfully
            }
        } catch (ex: Exception) {
            _messageStateEventData.value = R.string.subscriber_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }

    fun removeSubscriber(id: Long) = viewModelScope.launch {
        try {
            if (id > 0){
                repository.deleteSubscriber(id)
                _subscriberStateEventData.value = SubscriberState.Deleted
                _messageStateEventData.value = R.string.subscriber_deleted_sucessfully
            }
        }catch (ex: Exception){
            _messageStateEventData.value = R.string.subscriber_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    sealed class SubscriberState {
        object Inserted : SubscriberState()
        object Updated: SubscriberState()
        object Deleted: SubscriberState()
    }

    companion object {
        private val TAG = SubscriberViewModel::class.java.simpleName
    }
}