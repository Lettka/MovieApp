package com.example.mymovieapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovieapplication.model.Repository
import com.example.mymovieapplication.model.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class DetailViewModel()
    /*private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getMovieFromLocalSource() = getDataFromLocalSource()

    fun getMovieFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            if (true) {
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getMovieFromLocalStorage()))
            } else {
                liveDataToObserve.postValue((AppState.Error(Exception("check Internet"))))
            }

        }.start()
    }

}*/