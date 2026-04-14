package com.example.bikenav

data class RouteVector(
    val time: Double,
    val distance: Double,
    val elevation: Double,
    val lights: Int,
    val crossings: Int,
    val bikeRatio: Double,
    val stress: Double
)