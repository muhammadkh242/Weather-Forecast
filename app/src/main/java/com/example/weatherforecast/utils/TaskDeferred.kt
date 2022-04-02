package com.example.weatherforecast.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred


//extension function for Task class
//T -> generic parameter

fun <T> Task<T>.asDeferred(): Deferred<T>{
    //create my own deferred
    val deferred = CompletableDeferred<T>()
    //this is from type task because we extending Task
    this.addOnSuccessListener {
        deferred.complete(it as T)
    }
    this.addOnFailureListener {
        deferred.completeExceptionally(it)
    }

    return deferred
}