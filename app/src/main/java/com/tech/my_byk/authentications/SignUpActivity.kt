package com.tech.my_byk

import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


import com.tech.my_byk.databinding.ActivitySignUpBinding

import java.util.*
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpBinding : ActivitySignUpBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()




    private lateinit var databaseReference: DatabaseReference

    lateinit var googleSignInClient : GoogleSignInClient

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(signUpBinding.root)

        registerActivityForGoogleSignin()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

      //  registerActivityforResult()

        signUpBinding.buttonSignUp.setOnClickListener {

            val email = signUpBinding.textInputSignUpEmail.text.toString()
            val password = signUpBinding.textInputSignUpPassword.text.toString()
            val firstName = signUpBinding.textInputSignUpFirstname.text.toString()
            val lastName = signUpBinding.textInputSignUpLastname.text.toString()


            signUpWithFirebase(email,password,firstName,lastName)
            intent = Intent(this@SignUpActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()



        }



        //getting user uid from firebase auth class

       // val uid = auth.currentUser?.uid




        signUpBinding.textViewSignIn.setOnClickListener {
            intent = Intent(this@SignUpActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        signUpBinding.buttonSigninGoogle.setOnClickListener{

            signInGoogle()
        }

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1 && grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            val intent =Intent()
            intent.type="image/*"
            intent.action =Intent.ACTION_GET_CONTENT

            activityResultLauncher.launch(intent)

        }
    }

    private fun registerActivityforResult(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback{ result->

            val resultCode =result.resultCode



        })
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

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
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
            Toast.makeText(applicationContext,"Welcome to Quizzyy",Toast.LENGTH_SHORT).show()
            val intent =Intent(this@SignUpActivity,MainActivity::class.java)
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
                        databaseReference.child(user.uid).child("Name").setValue(username)

                    }

                }







            }else{

            }
        }

    }

    fun signUpWithFirebase(Email :String ,Password : String,FirstName :String , LastName:String)
    {
        signUpBinding.progressBarSignUp.visibility = View.VISIBLE
        signUpBinding.buttonSignUp.isClickable=false

        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener {task->

        if(task.isSuccessful)
            {
                Toast.makeText(this, "Account Created Successfully",
                    Toast.LENGTH_SHORT).show();
                val user = auth.currentUser
                //it is not working need to be fixed
                if (user != null) {


                        databaseReference.child(user.uid).child("Name").setValue("$FirstName $LastName")
                   // Toast.makeText(this, "user added to firebase",
                     //   Toast.LENGTH_SHORT).show();
                     //   databaseReference.child(user.uid).child("name").setValue(true)



                }
                else
                {
                    Toast.makeText(this,"no user signed in" , Toast.LENGTH_SHORT).show()
                }
                finish()
                signUpBinding.progressBarSignUp.visibility = View.INVISIBLE
                signUpBinding.buttonSignUp.isClickable=true



            }
        else
            {
                Toast.makeText(this, task.exception?.localizedMessage,
                    Toast.LENGTH_SHORT).show();
            }

        }




    }
}