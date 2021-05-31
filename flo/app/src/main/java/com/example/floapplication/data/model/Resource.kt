package com.example.floapplication.data.model

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun <T> error(data: T?, message: String): Resource<T> = Resource(Status.ERROR, data, message)

        fun <T> loading(data: T?): Resource<T> = Resource( Status.LOADING, data, null)
    }
}
