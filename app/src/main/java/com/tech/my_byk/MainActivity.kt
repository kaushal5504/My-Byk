package com.tech.my_byk

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button:Button = findViewById(R.id.button)
        button.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            //google signout

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(this,gso)

            googleSignInClient.signOut().addOnCompleteListener { task->
                if(task.isSuccessful)
                    Toast.makeText(this,"Signed out succesfully", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"Not signed out", Toast.LENGTH_SHORT).show()


            }



            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
    }
}