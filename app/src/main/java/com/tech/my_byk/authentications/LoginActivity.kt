package com.tech.my_byk

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.tech.my_byk.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var googleSignInClient :GoogleSignInClient

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        //register activity result launcher in this method

        registerActivityForGoogleSignin()


        val textOfGoogleButton = loginBinding.buttonSigninGoogle.getChildAt(0)as TextView
        textOfGoogleButton.text ="Contiue With Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 18F



        loginBinding.buttonSignin.setOnClickListener {
            val userEmail = loginBinding.textInputLoginEmail.text.toString()
            val userPassword = loginBinding.textInputLoginPassword.text.toString()

            signInUser(userEmail,userPassword)

        }
        loginBinding.buttonSigninGoogle.setOnClickListener {
            signInGoogle()
        }

        loginBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)



        }
        loginBinding.textViewForgotPassword.setOnClickListener {

            val intent = Intent(this , ForgotPasswordActivity::class.java)
            startActivity(intent)


        }
    }

    //to sign in user called in signinbutton click listner
    fun signInUser(email:String , password:String)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task->

        if(task.isSuccessful)
        {
            Toast.makeText(applicationContext,"welcome to MyByk",Toast.LENGTH_SHORT).show()

            val user = auth.currentUser
            if (user != null) {
                val username= user.displayName
                if (username != null) {
                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    databaseReference.child(user.uid).child("Name").setValue(username)

                }

            }

            val intent =Intent(this@LoginActivity,MainActivity2::class.java)
            startActivity(intent)
            finish()
        }
            else
            {
            Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun signInGoogle()
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("764580707725-iksf97gifm0ns7vfr8hb8q8dmh12nd53.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)


        signIn()
    }

    private fun signIn()
    {
        val intent :Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(intent)


    }

    private  fun registerActivityForGoogleSignin()
    {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result->

                val resultCode = result.resultCode
                val data = result.data

                if(resultCode == RESULT_OK && data !=null){

                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    
                    firebaseSignInWithGoogle(task)
                }

            })

    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

            Toast.makeText(applicationContext,"Welcome to My-Byk",Toast.LENGTH_SHORT).show()
            val intent =Intent(this@LoginActivity,MainActivity2::class.java)
            startActivity(intent)
            finish()

            firebaseGoogleAccount(account)
        }catch (e:Exception){
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()

        }


    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task->


            if(task.isSuccessful){
                val user = auth.currentUser
                if (user != null) {
                    val username= user.displayName
                    if (username != null) {
                        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        databaseReference.child(user.uid).child("Name").setValue(username)

                    }

                }


            }else{

            }
        }

    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user!=null)
        {
            Toast.makeText(applicationContext,"welcome to My-Byk",Toast.LENGTH_SHORT).show()

            val intent =Intent(this@LoginActivity,MainActivity2::class.java)
            startActivity(intent)
            finish()
        }

    }
}