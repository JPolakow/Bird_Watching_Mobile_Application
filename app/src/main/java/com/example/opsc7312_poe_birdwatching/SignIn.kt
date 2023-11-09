//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Date

class SignIn : Fragment() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var btnSignIn: Button
    private lateinit var pbWaitToSignIn: ProgressBar

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    //==============================================================================================
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button by ID
        pbWaitToSignIn = view.findViewById(R.id.pbWaitToSignIn)
        btnSignIn = view.findViewById(R.id.btnSignIn)
        emailInput = view.findViewById(R.id.txtUserSignInEmail)
        passwordInput = view.findViewById(R.id.txtUserSignInPassword)

        // Set an OnClickListener to the button
        btnSignIn.setOnClickListener {

            var email = emailInput.text.toString().trim()
            var pword = passwordInput.text.toString().trim()

            // Setting progress bar to visible when user attempts to sign in
            pbWaitToSignIn.visibility = View.VISIBLE

            authenticateUser(email, pword)
        }
    }

    //==============================================================================================
    //attempt to find user in list, if found check the password is correct
    private fun authenticateUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    val errToast = Toast.makeText(
                        requireContext(), "Signed In! UID: $uid", Toast.LENGTH_LONG
                    )

                    errToast.setGravity(Gravity.BOTTOM, 0, 25)
                    errToast.show()

                    if (uid != null) {
                        // Retrieve user data from Firestore
                        val usersCollection = db.collection("users")

                        usersCollection.document(uid)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    // Document exists, so you can access its data
                                    val userData = documentSnapshot.toObject(UsersModel::class.java)

                                    if (userData != null) {

                                        // Authentication successful
                                        ToolBox.users.clear()
                                        ToolBox.users.add(userData)

                                        ChallengeModel.getChallenges()

                                        ToolBox.fetchUserObservations()

                                        val intent = Intent(activity, Hotpots::class.java)
                                        startActivity(intent)

                                    } else {
                                        // Handle the case where the data couldn't be converted to UsersModel
                                    }
                                } else {
                                    // Handle the case where the document doesn't exist (user data not found)
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle the error if retrieving data from Firestore fails
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to retrieve user data from Firestore: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // Handle the case where UID is null
                    }
                } else {
                    val errToast = Toast.makeText(
                        requireContext(), "Incorrect email or password", Toast.LENGTH_LONG
                    )

                    errToast.setGravity(Gravity.BOTTOM, 0, 25)
                    errToast.show()
                }
            }
    }



}