package com.mutawalli.challenge4.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mutawalli.challenge4.Constant
import com.mutawalli.challenge4.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUser = binding.etUsernameSign
        val etEmail = binding.etEmailSign
        val etPassword = binding.etPasswordSign
        val etPasswordVerif = binding.etVerifSign
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            Constant
            .Preferences.PREF_NAME, MODE_PRIVATE)

        binding.btnDaftar.setOnClickListener{
            if (etUser.text.isEmpty()){
                etUser.error = "Kolom Harus di Isi !"
                etUser.requestFocus()
                return@setOnClickListener
            } else if (etEmail.text.isEmpty()){
                etEmail.error = "Kolom Harus di Isi !"
                etEmail.requestFocus()
                return@setOnClickListener
            } else if (etPassword.text.isNullOrEmpty()){
                etPassword.error = "Kolom Harus di Isi !"
                etPassword.requestFocus()
                return@setOnClickListener
            }else if (etPasswordVerif.text.isNullOrEmpty()){
                etPasswordVerif.error = "Kolom Harus di Isi !"
                etPasswordVerif.requestFocus()
                return@setOnClickListener
            }

            if (etPassword.text.toString() == etPasswordVerif.text.toString()){
                sharedpreferences(sharedPreferences)
                Toast.makeText(requireContext(), "Register Berhasil", Toast.LENGTH_SHORT).show()
                val action = SignupFragmentDirections.signupToLogin()
                it.findNavController().navigate(action)
            } else {
                etPasswordVerif.error = "Password konfirmasi berbeda"
                etPasswordVerif.requestFocus()
                Toast.makeText(requireContext(), "Register Gagal", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }
    }

    fun sharedpreferences(pref:SharedPreferences){
        val etUser = binding.etUsernameSign
        val etEmail = binding.etEmailSign
        val etPassword = binding.etPasswordSign

        pref.edit{
            putString(Constant.Preferences.KEY.user, etUser.text.toString())
            putString(Constant.Preferences.KEY.email, etEmail.text.toString())
            putString(Constant.Preferences.KEY.password, etPassword.text.toString())
            apply()
        }

        view?.let { Snackbar.make(it, "Berhasil Daftar. User: ${etUser.text} | Email: ${etEmail.text}", Snackbar.LENGTH_LONG).show() }
    }

}