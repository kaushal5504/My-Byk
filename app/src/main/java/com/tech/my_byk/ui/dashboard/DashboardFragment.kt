package com.tech.my_byk.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tech.my_byk.LoginActivity
import com.tech.my_byk.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
    val auth = FirebaseAuth.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val current_user = auth.currentUser

        if (current_user != null) {
            binding.textViewDisplayName.text = current_user.displayName.toString()
        }

        binding.buttonLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            //google signout

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(root.context,gso)

            googleSignInClient.signOut().addOnCompleteListener { task->
                if(task.isSuccessful)
                    Toast.makeText(root.context,"Signed out succesfully", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(root.context,"Not signed out", Toast.LENGTH_SHORT).show()


            }



            val intent = Intent(root.context, LoginActivity::class.java)
            startActivity(intent)



        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}