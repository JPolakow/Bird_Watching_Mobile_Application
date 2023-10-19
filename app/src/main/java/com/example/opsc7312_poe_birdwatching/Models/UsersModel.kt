package com.example.opsc7312_poe_birdwatching.Models

// Data class used to store users
data class UsersModel(
    var UserID: Int = -1,
    var Name: String = "",
    var Surname: String = "",
    var Email: String = "",
    var Hash: String = "",
    var isUnitKM: Boolean = true,
    var MaxDistance: Double = 5.0,
    var ChallengePoints: Int = 0,
    var mapStyleIsDark: Boolean = false
)
