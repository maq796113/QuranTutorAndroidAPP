//package com.example.qurantutor.mongodb
//
//import io.realm.RealmObject
//
//import io.realm.kotlin.types.annotations.PrimaryKey
//import org.bson.types.ObjectId
//import java.time.Instant
//import java.time.LocalDateTime
//
//open class RealmObjectDataModel(
//    var time: LocalDateTime? = null,
//    @PrimaryKey
//    var _id: ObjectId = ObjectId(),
//    var username: String = "",
//    var bleu_score: Float = 0.0f,
//    var surah_id: Int = 0
//): RealmObject()
