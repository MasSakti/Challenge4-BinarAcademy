package com.mutawalli.challenge4.ui

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutawalli.challenge4.Constant
import com.mutawalli.challenge4.R
import com.mutawalli.challenge4.adapter.NoteAdapter
import com.mutawalli.challenge4.data.local.NoteEntity
import com.mutawalli.challenge4.database.NoteTakingDatabase
import com.mutawalli.challenge4.databinding.FragmentListBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    private var database: NoteTakingDatabase? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = NoteTakingDatabase.getInstance(view.context.applicationContext)
        binding.rvListData.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        fetchData()

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            Constant
                .Preferences.PREF_NAME, Context.MODE_PRIVATE
        )
        val prefUser = sharedPreferences.getString(Constant.Preferences.KEY.user, "")
        "Welcome, $prefUser!".also { binding.tvUserDisplay.text = it }

        binding.tvLogoutButton.setOnClickListener {
            val dialogInflate =
                LayoutInflater.from(it.context).inflate(R.layout.dialog_logout, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(dialogInflate)

            val btnNo: Button = dialogInflate.findViewById(R.id.btnNo)
            val btnYes: Button = dialogInflate.findViewById(R.id.btnYes)
            val dialog = dialogBuilder.create()

            btnNo.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()

            btnYes.setOnClickListener {
                dialog.dismiss()
                logoutnow()
            }
        }

        binding.btnRefresh.setOnClickListener {
            fetchData()
        }

        binding.fabAdd.setOnClickListener {
            val dialogInflate =
                LayoutInflater.from(it.context).inflate(R.layout.dialog_add, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(dialogInflate)

            val dialog = dialogBuilder.create()
            val btnSimpan: Button = dialogInflate.findViewById(R.id.btnSimpanData)
            val etJudul: EditText = dialogInflate.findViewById(R.id.etJudulData)
            val etCatatan: EditText = dialogInflate.findViewById(R.id.etCatatanData)

            dialog.show()
            dialog.setCancelable(true)
            btnSimpan.setOnClickListener {
                val objectNote = NoteEntity(
                    null,
                    etJudul.text.toString(),
                    etCatatan.text.toString()
                )

                activity?.runOnUiThread {
                    GlobalScope.async {
                        val result = database?.noteDao()?.insertData(objectNote)
                        requireActivity().runOnUiThread {
                            if (result != 0L) {
                                Toast.makeText(
                                    it.context,
                                    "Berhasil Menambahkan Data",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                fetchData()
                            } else {
                                Toast.makeText(it.context, "Ada yang Salah!", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
                dialog.dismiss()
            }
        }
        fetchData()
    }

    private fun logoutnow() {
        val action = ListFragmentDirections.listLogoutToLogin()
        view?.findNavController()?.navigate(action)
    }

    fun fetchData() {
        GlobalScope.launch {
            val listNote = database?.noteDao()?.getAllData()

            activity?.runOnUiThread {
                listNote?.let {
                    val adapter = NoteAdapter(it)
                    binding.rvListData.adapter = adapter
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fetchData()
    }


    override fun onPause() {
        super.onPause()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onDestroy() {
        super.onDestroy()
        NoteTakingDatabase.destroyInstance()
    }
}