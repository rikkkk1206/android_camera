package com.example.android_camera.camera.http

data class RequestFormat (
    var key: String,
    var pic: String )
val requestPanel = RequestFormat (
    key = "panel",
    pic = "" )
val requestBin = RequestFormat (
    key = "bin",
    pic = "" )
val requestInvert = RequestFormat (
    key = "invert",
    pic = "" )