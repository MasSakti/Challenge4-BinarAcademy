package com.mutawalli.challenge4.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mutawalli.challenge4.Constant
import com.mutawalli.challenge4.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            Constant
                .Preferences.PREF_NAME, MODE_PRIVATE
        )

        val etEmail = binding.etEmail
        val etPassword = binding.etPassword

        binding.tvDaftar.setOnClickListener {
            val action = LoginFragmentDirections.loginToSignup()
            it.findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener {
            if (etEmail.text.isEmpty()) {
                etEmail.error = "Kolom Harus di Isi !"
                etEmail.requestFocus()
                return@setOnClickListener
            } else if (etPassword.text.isNullOrEmpty()) {
                etPassword.error = "Kolom Harus di Isi !"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (validateAccount(sharedPreferences)) {
                val action = LoginFragmentDirections.loginToList()
                it.findNavController().navigate(action)
            } else {
                return@setOnClickListener
            }
        }
    }

    fun validateAccount(preferences: SharedPreferences): Boolean {
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        val prefEmail =
            preferences.getString(Constant.Preferences.KEY.email, etEmail.text.toString())
        val prefPassword =
            preferences.getString(Constant.Preferences.KEY.password, etPassword.text.toString())

        if (prefEmail == etEmail.text.toString() && prefPassword == etPassword.text.toString()) {
            Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Toast.makeText(requireContext(), "Ada Yang Salah! Coba Input Email dan Password Ulang.", Toast.LENGTH_LONG).show()
            return false
        }
    }

}