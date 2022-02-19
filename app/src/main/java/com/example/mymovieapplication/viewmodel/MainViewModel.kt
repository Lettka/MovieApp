package com.example.mymovieapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovieapplication.repository.Repository
import com.example.mymovieapplication.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getMovieFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getMovieFromRemoteSourceEng() = getDataFromLocalSource(false)

    fun getMovieFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRussian: Boolean = true) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(100)
            if (true) {
                liveDataToObserve.postValue(
                    AppState.Success(
                        if (isRussian)
                            repositoryImpl.getCategoryFromLocalStorageRus()
                        else
                            repositoryImpl.getCategoryFromLocalStorageEng()
                    )
                )
            } else {
                liveDataToObserve.postValue((AppState.Error(Exception("check Internet"))))
            }
        }.start()
    }

}