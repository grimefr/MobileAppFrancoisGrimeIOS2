package com.example.mybankapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class SecondFragment : Fragment() {

    init {
        System.loadLibrary("urlC")
    }

    external fun getMasterKEY(): String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_second, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actualise(view)
        actualise(view)
        view.findViewById<Button>(R.id.button_refresh).setOnClickListener{
            actualise(view)
            actualise(view)
        }

        view.findViewById<Button>(R.id.button_disconnect).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    external fun geturl(): String

    fun actualise(view: View){

        val factory = SupportFactory(SQLiteDatabase.getBytes(getMasterKEY().toCharArray()))
        val db = Room.databaseBuilder(
            requireContext(),
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
                val toast = Toast.makeText(requireContext(), "You need to be online to refresh your accounts", Toast.LENGTH_SHORT)
                toast.show()

            }

            override fun onResponse(
                call: Call<List<PostModel?>?>,
                response: Response<List<PostModel?>?>
            ) {
                val postlist : List<PostModel>? = response.body() as List<PostModel>
                Thread {
                    val userDao = db.userDao()
                    var data = view.findViewById<TextView>(R.id.textView)
                    data.text = ""
                    if (postlist != null) {
                        for(item in postlist){
                            if(userDao.searchId(item.id)!=null){
                                userDao.update(Entities(item.id,item.account_name,item.iban,item.currency,item.amount))
                                data.text = userDao.getAll().toString()
                            }
                            else{
                                userDao.insert(Entities(item.id,item.account_name,item.iban,item.currency,item.amount))
                                data.text = userDao.getAll().toString()
                            }
                        }
                    }
                }.start()
            }
        })



    }




}