package com.example.qurantutor.mongodb



import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.time.Instant

open class BaseRealmObject(
    var time: Instant = Instant.now(),
    @PrimaryKey
    var _id: ObjectId = ObjectId.get(),
    var username: String = "",
    var bleu_score: Float = 0.0f,
    var surah_id: Int = 0
): RealmObject