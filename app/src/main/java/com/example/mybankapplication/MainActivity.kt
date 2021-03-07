package com.example.mybankapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.*

class MainActivity : AppCompatActivity() {

    init {
        System.loadLibrary("urlC")
    }

    external fun geturl(): String
    external fun getMasterKEY(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BuildConfig.APPLICATION_ID


        //database
        val factory = SupportFactory(SQLiteDatabase.getBytes(getMasterKEY().toCharArray()))
        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database_de_la_mort_qui_tue"
        ).openHelperFactory(factory)
                .build()


        //tls
        val request = Request.Builder()
            .url(geturl())
            .build()
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            )
            .build()

        val client = OkHttpClient.Builder()
            .connectionSpecs(Collections.singletonList(spec))
            .certificatePinner(
                CertificatePinner.Builder()
                    .add(
                        "mockapi.io",
                        "sha256/a60d6f05f1a27d3dfe6f43a7822ddd752b7e0964"
                    )
                    .build()
            )
            .build()


        //retrofit
        val rf = Retrofit.Builder()
            .baseUrl(geturl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val API = rf.create(RetrofitInterface::class.java)
        val call = API.getposts()

        call?.enqueue(object: Callback<List<PostModel?>?> {

            override fun onFailure(call: Call<List<PostModel?>?>, t: Throwable) {
                val toast = Toast.makeText(applicationContext, "Not online",Toast.LENGTH_SHORT)
                toast.show()

            }

            override fun onResponse(
                call: Call<List<PostModel?>?>,
                response: Response<List<PostModel?>?>
            ) {
                runOnUiThread{
                    //val postlist : List<PostModel>? = response.body() as List<PostModel>

                    //val test:TextView = findViewById(R.id.textView)
                    //test.text = "id :" + postlist?.get(1)?.id.toString() + " | iban : " + postlist?.get(1)?.iban.toString()

                }


            }
        })
    }
}
