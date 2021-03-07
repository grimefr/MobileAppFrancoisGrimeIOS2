package com.example.mybankapplication

import android.os.Bundle
import android.util.Base64
import android.widget.TextView
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

import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.DESKeySpec

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
        val factory = SupportFactory(SQLiteDatabase.getBytes(getMasterKEY().toCharArray()))
        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database_de_la_mort_qui_tue"
        ).openHelperFactory(factory)
                .build()




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
                    val postlist : List<PostModel>? = response.body() as List<PostModel>

                    val test:TextView = findViewById(R.id.textView)
                    test.text = "id :" + postlist?.get(1)?.id.toString() + " | iban : " + postlist?.get(1)?.iban.toString()

                }


            }
        })
    }

    private val cryptoPass = "Vfd57h5wv4kfMy81hzgziejdnezd"

    fun encryptIt(value: String): String? {

        try {
            val keySpec = DESKeySpec(cryptoPass.toByteArray(charset("UTF8")))
            val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("DES")
            val key: SecretKey = keyFactory.generateSecret(keySpec)
            val clearText = value.toByteArray(charset("UTF8"))
            // Cipher is not thread safe
            val cipher: Cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return value
    }
    fun decryptIt(value: String): String? {
        try {
            val keySpec = DESKeySpec(cryptoPass.toByteArray(charset("UTF8")))
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val key = keyFactory.generateSecret(keySpec)
            val encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT)
            // cipher is not thread safe
            val cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decrypedValueBytes = cipher.doFinal(encrypedPwdBytes)
            return String(decrypedValueBytes)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return value
    }
}
