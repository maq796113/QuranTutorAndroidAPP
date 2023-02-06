package com.example.qurantutor.mongodb

import io.realm.RealmObject

import io.realm.kotlin.types.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.time.Instant

open class RealmObjectDataModel(
    var time: Instant,
    @PrimaryKey
    var _id: ObjectId,
    var username: String,
    var bleu_score: Int,
    var surah_id: Int
): RealmObject()
