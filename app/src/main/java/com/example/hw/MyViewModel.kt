package com.example.hw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw.api.DataRepository
import com.example.hw.api.PlantInfo
import com.example.hw.api.PlantReq

class MyViewModel  : ViewModel() {
    private val repository = DataRepository()
    private val _records = MutableLiveData<List<PlantInfo>>()
    val records: LiveData<List<PlantInfo>> = _records

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchData(req: PlantReq) {
        repository.fetchData(req, object : DataRepository.ResponseCallback<List<PlantInfo>> {
            override fun onSuccess(data: List<PlantInfo>) {
                _records.postValue(data)
            }

            override fun onError(message: String) {
                _error.postValue(message)
            }
        })
    }
}