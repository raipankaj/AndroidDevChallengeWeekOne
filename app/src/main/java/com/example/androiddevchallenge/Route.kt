package com.example.androiddevchallenge

sealed class Route(val path: String) {
    object Main : Route("Main")
    object Details : Route("Details/{dogImageId}/{dogAge}/{dogName}/{dogDetails}/{dogGender}")

    class DetailsFillPath(
        imageId: Int, dogAge: String,
        dogName: String,dogGender: String,
        dogDetails: String
    ) : Route("Details/$imageId/$dogAge/$dogName/$dogDetails/$dogGender")
}