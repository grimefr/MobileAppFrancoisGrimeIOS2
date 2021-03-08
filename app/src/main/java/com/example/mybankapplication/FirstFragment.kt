package com.example.mybankapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import okhttp3.internal.wait
import java.lang.Thread.sleep

class FirstFragment : Fragment() {

    init {
        System.loadLibrary("urlC")
    }

    external fun getPassword(): String
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_login).setOnClickListener {

            val password = view.findViewById(R.id.password) as EditText
            val value = password.text.toString()


            login(sanitize(value))
        }
    }

    fun login(password : String){

        if(password.equals(getPassword())){
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        else {
            sleep(5000)
            val toast = Toast.makeText(context,"Error in the password",Toast.LENGTH_SHORT)
            toast.show()
        }

    }

    fun sanitize(value:String): String {
        return value.trim('/','.','\'',' ','`','(',')','=','+','\"')
    }
}