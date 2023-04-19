package com.fitnytech.influencerApp.development.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fitnytech.influencerApp.development.R
import com.fitnytech.influencerApp.development.preference.SavedPreference
import com.fitnytech.influencerApp.development.databinding.ActivityLoginBinding
import com.fitnytech.influencerApp.development.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)


        database = Firebase.database.reference

        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.Signin.setOnClickListener{ view: View? ->
            Toast.makeText(this,"Logging In",Toast.LENGTH_SHORT).show()
            signInGoogle()
        }
    }

    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                saveUser(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUser(account: GoogleSignInAccount){

        val reference = FirebaseDatabase.getInstance().getReference("influencer1")
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {

                SavedPreference.setEmail(this@LoginActivity,account.email.toString())
                SavedPreference.setUsername(this@LoginActivity,account.displayName.toString())

                reference.child("influencers").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (ds in dataSnapshot.children) {
                            val key = ds.key

                            val email: String? = dataSnapshot.child(key.toString()).child("influencerEmailId").getValue(String::class.java)

                            if (email.equals(account.email.toString())){
                                SavedPreference.setInfluencerCode(this@LoginActivity,key.toString())
                                database.child("influencer1").child("influencers").child(key.toString()).child("influencerProfilePicture").setValue(account.photoUrl.toString())
                                break
                            }
                            else{
                                SavedPreference.setInfluencerCode(this@LoginActivity,"2")
                                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                                val user = User(account.displayName.toString(), account.email.toString(), null, account.photoUrl.toString(),"null")
                                database.child("influencer1").child("users").child(uid).setValue(user)
                                SavedPreference.setUserId(this@LoginActivity,uid)
                            }
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                            overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);

                        }




                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("HELLOO", databaseError.message)//prints "Do you have data? You'll love Firebase."
                    }
                })



            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}