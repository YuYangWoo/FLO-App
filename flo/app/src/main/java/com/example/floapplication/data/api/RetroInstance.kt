package com.example.floapplication.data.api

object RetroInstance {
    val baseUrl = "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com"
    val client = BaseRetro.getClient(baseUrl).create(RetroService::class.java)
}