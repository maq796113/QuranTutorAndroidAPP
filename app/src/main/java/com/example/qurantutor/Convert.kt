package com.example.qurantutor

import android.os.Bundle
import io.realm.Realm.getApplicationContext
import io.realm.kotlin.Realm
import org.bson.Document

class Convert : AppCompatActivity() {
    var Appid = "mongodbcourse-ftebf"
    private var app: App? = null
    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null
    var editText: EditText? = null
    var button: Button? = null
    var button1: Button? = null
    var button2: Button? = null
    var textView: TextView? = null
    var data: String? = null
    var user: User? = null
    var mongoCollection: MongoCollection<Document>? = null
    var strings: ArrayList<String>? = ArrayList()
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.data)
        button = findViewById(R.id.addData)
        button1 = findViewById(R.id.findDataButton)
        textView = findViewById(R.id.findData)
        button2 = findViewById(R.id.signin)
        Realm.init(getApplicationContext())
        app = App(Builder(Appid).build())
        button2.setOnClickListener(object : OnClickListener() {
            fun onClick(v: View?) {
                app.currentUser().logOutAsync { result ->
                    if (result.isSuccess()) {
                        Log.v("Logged Out", "Logged Out")
                        app.loginAsync(Credentials.anonymous(), object : Callback<User?>() {
                            fun onResult(result: App.Result<User?>) {
                                if (result.isSuccess()) {
                                    Log.v("User", "Logged In Successfully")
                                    user = app.currentUser()
                                    mongoClient = user.getMongoClient("mongodb-atlas")
                                    mongoDatabase = mongoClient.getDatabase("CourseData")
                                    mongoCollection = mongoDatabase.getCollection("TestData")
                                    Toast.makeText(
                                        getApplicationContext(),
                                        "Login Succesful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Log.v("User", "Failed to Login")
                                }
                            }
                        })
                    }
                }
            }
        })
        button.setOnClickListener(object : OnClickListener() {
            fun onClick(v: View?) {
//                Log.v("adding","adding");
//                Document document = new Document().append("data",editText.getText().toString()).append("myid","1234").append("userid",user.getId());
//                mongoCollection.insertOne(document).getAsync(result -> {
//                    if(result.isSuccess())
//                    {
//                        Log.v("adding","result");
//                        Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Log.v("adding","result failed"+result.getError().toString());
//                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
//                    }
//                });
                val queryFilter = Document().append("uniqueId", "1234")
                val findTask: RealmResultTask<MongoCursor<Document>> =
                    mongoCollection.find(queryFilter).iterator()
                findTask.getAsync { task ->
                    if (task.isSuccess()) {
                        val results: MongoCursor<Document> = task.get()
                        if (results.hasNext()) {
                            Log.v("FindFunction", "Found Something")
                            val result: Document = results.next()
                            strings = result["strings"] as ArrayList<String>?
                            if (strings == null) {
                                strings = ArrayList()
                            }
                            val data: String = editText.getText().toString()
                            strings!!.add(data)
                            result.append("strings", strings)
                            mongoCollection.updateOne(queryFilter, result).getAsync { result1 ->
                                if (result1.isSuccess()) {
                                    Log.v("UpdateFunction", "Updated Data")
                                } else {
                                    Log.v("UpdateFunction", "Error" + result1.getError().toString())
                                }
                            }
                        } else {
                            val data: String = editText.getText().toString()
                            if (strings == null) {
                                strings = ArrayList()
                            }
                            strings!!.add(data)
                            Log.v("FindFunction", "Found Nothing")
                            mongoCollection.insertOne(
                                Document().append("uniqueId", "1234").append("strings", strings)
                            ).getAsync { result ->
                                if (result.isSuccess()) {
                                    Log.v("AddFunction", "Inserted Data")
                                } else {
                                    Log.v("AddFunction", "Error" + result.getError().toString())
                                }
                            }
                        }
                    } else {
                        Log.v("Error", task.getError().toString())
                    }
                }
            }
        })
        button1.setOnClickListener(object : OnClickListener() {
            fun onClick(v: View?) {
                user = app.currentUser()
                data = ""
                val queryFilter = Document().append("uniqueId", "1234")
                val findTask: RealmResultTask<MongoCursor<Document>> =
                    mongoCollection.find(queryFilter).iterator()
                //                 mongoCollection.findOne(queryFilter).getAsync(result -> {
//                     if(result.isSuccess())
//                     {
//                         Toast.makeText(getApplicationContext(),"Found",Toast.LENGTH_LONG).show();
//                         Log.v("Data",result.toString());
//                         Document resultdata = result.get();
//                         textView.setText(resultdata.getString("data"));
//                     }
//                     else
//                     {
//                         Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_LONG).show();
//                         Log.v("Data",result.getError().toString());
//                     }
//                 });
                findTask.getAsync { task ->
                    if (task.isSuccess()) {
                        val results: MongoCursor<Document> = task.get()
                        if (!results.hasNext()) {
                            Log.v("Result", "Couldnt Find")
                        }
                        while (results.hasNext()) {
                            val currentDoc: Document = results.next()
                            strings = currentDoc["strings"] as ArrayList<String>?
                            if (strings == null) {
                                Log.v("FindTask", "Strings has size 0")
                            } else {
                                var i = 0
                                i = 0
                                while (i < strings!!.size) {
                                    if (data == null) {
                                        data = ""
                                        data = data + " & " + strings!![i]
                                        textView.setText(data)
                                    } else {
                                        data = data + " & " + strings!![i]
                                        textView.setText(data)
                                    }
                                    i++
                                }
                            }
                        }
                    } else {
                        Log.v("Task Error", task.getError().toString())
                    }
                }
            }
        })
    }
}