package com.example.databasetesting

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var myDB: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val txtName : EditText = findViewById(R.id.txtName)
        val txtID : EditText = findViewById(R.id.txtID)
        val txtProgramme : EditText = findViewById(R.id.txtProgramme)
        val btnGet : Button = findViewById(R.id.btnGet)
        val btnSave : Button = findViewById(R.id.btnSave)
        val btnSignIn : Button = findViewById(R.id.btnSignIn)

        myDB = FirebaseDatabase.getInstance()
        //reference = database table
        myRef = myDB.getReference("Student")
        auth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener(){
            login("benedictAssignment@gmail.com","123qwe123")
        }


        btnGet.setOnClickListener (){
            val id = txtID.text.toString()
            myRef.child(id).get().addOnSuccessListener {record->
                //the name of default data object is "it"
                //now change the "it" to "record"
                txtName.setText(record.child("studentName").value.toString())
                txtProgramme.setText(record.child("studentProgramme").value.toString())
            }.addOnFailureListener{
                Toast.makeText(applicationContext,"Failed to Retrieve Record",Toast.LENGTH_LONG).show()
            }


        }


        btnSave.setOnClickListener(){
            val id = txtID.text.toString()
            val name = txtName.text.toString()
            val programme = txtProgramme.text.toString()
            val student = Student(id,name,programme)
            myRef.child(student.studentId).setValue(student).addOnSuccessListener {
                Toast.makeText(applicationContext,"Success to Add Record",Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(applicationContext,"Failed to Add Record",Toast.LENGTH_LONG).show()}
        }


    }

    override fun onStop() {
        super.onStop()
        auth.signOut()
    }

    fun register(email: String, psw: String) {

        auth.createUserWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val user = auth.currentUser
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_LONG).show()
            }
    }

    fun login(email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val user = auth.currentUser

            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_LONG).show()
            }
    }

    fun signOut() {
        auth.signOut()

    }
}