package com.example.mymovieapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovieapplication.model.Repository
import com.example.mymovieapplication.model.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getMovieFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getMovieFromRemoteSourceEng() = getDataFromLocalSource(false)

    fun getMovieFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRussian: Boolean = true) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            if (true) {
                liveDataToObserve.postValue(
                    AppState.Success(
                        if (isRussian)
                            repositoryImpl.getMovieFromLocalStorageRus()
                        else
                            repositoryImpl.getMovieFromLocalStorageEng()
                    )
                )
            } else {
                liveDataToObserve.postValue((AppState.Error(Exception("check Internet"))))
            }
        }.start()
    }

}