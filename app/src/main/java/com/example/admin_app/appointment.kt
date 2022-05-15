package com.example.admin_app

data class appointment(
    var appointment_id: String ?=null,
    var name:String?=null,
    var spec:String?=null,
    var patient_emial: String?=null,
    var date:String?=null,
    var timeslot:String?=null,
    var status:String?=null,
)