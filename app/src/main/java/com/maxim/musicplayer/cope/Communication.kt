package com.maxim.musicplayer.cope

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication {
    interface Update<T> {
        fun update(value: T)
    }

    interface Observe<T> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
    }

    interface Mutable<T> : Update<T>, Observe<T>
    abstract class Regular<T>(
        private val liveData: MutableLiveData<T> = MutableLiveData<T>()
    ) : Mutable<T> {
        override fun update(value: T) {
            liveData.value = value
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }

    abstract class Single<T>: Regular<T>(SingleLiveEvent())
}