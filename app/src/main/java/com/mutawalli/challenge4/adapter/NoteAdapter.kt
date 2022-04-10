package com.mutawalli.challenge4.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mutawalli.challenge4.MainActivity
import com.mutawalli.challenge4.R
import com.mutawalli.challenge4.data.local.NoteEntity
import com.mutawalli.challenge4.database.NoteTakingDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class NoteAdapter(private val list: List<NoteEntity>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    private var database: NoteTakingDatabase? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvItems: TextView = holder.itemView.findViewById(R.id.tvItems)
        val tvItemsDesc: TextView = holder.itemView.findViewById(R.id.tvItemsDesc)
        val btnEdit: ImageButton = holder.itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = holder.itemView.findViewById(R.id.btnDelete)

        tvItems.text = list[position].judul
        tvItemsDesc.text = list[position].note

        database = NoteTakingDatabase.getInstance(holder.itemView.context)

        btnDelete.setOnClickListener {
            val dialogInflate =
                LayoutInflater.from(it.context).inflate(R.layout.dialog_delete, null, false)
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
                GlobalScope.async {
                    val result = database?.noteDao()?.deleteData(list[position])

                    (holder.itemView.context as MainActivity).runOnUiThread {
                        if (result != 0) {
                            Toast.makeText(it.context, "Berhasil Hapus Data, Refresh Sekarang !", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(it.context, "Ada yang Salah!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                dialog.dismiss()
            }
        }

        btnEdit.setOnClickListener {
            val dialogInflate = LayoutInflater.from(it.context).inflate(R.layout.dialog_edit, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(dialogInflate)

            val dialog = dialogBuilder.create()
            val etJudulEdit: EditText = dialogInflate.findViewById(R.id.etJudulEdit)
            val etCatatanEdit: EditText = dialogInflate.findViewById(R.id.etCatatanEdit)
            val btnSimpanEdit: Button = dialogInflate.findViewById(R.id.btnSimpanEdit)

            etJudulEdit.setText(list[position].judul)
            etCatatanEdit.setText(list[position].note)

            dialog.show()
            dialog.setCancelable(true)

            btnSimpanEdit.setOnClickListener {
//                if (etJudulEdit.text.toString().isEmpty()) {
//                    etJudulEdit.error = "Kolom tidak boleh kosong"
//                    etJudulEdit.requestFocus()
//                    return@setOnClickListener
//                } else if (etCatatanEdit.text.toString().isEmpty()) {
//                    etCatatanEdit.error = "Kolom tidak boleh kosong"
//                    etCatatanEdit.requestFocus()
//                    return@setOnClickListener
//                }

                val objectNote = NoteEntity(
                    id = list[position].id,
                    judul = etJudulEdit.text.toString(),
                    note = etCatatanEdit.text.toString()
                )


                GlobalScope.async {
                    val result = database?.noteDao()?.updateData(objectNote)

                    (holder.itemView.context as MainActivity).runOnUiThread {
                        if (result != 0) {
                            Toast.makeText(it.context, "Berhasil Edit Data, Refresh Sekarang !", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(it.context, "Ada yang Salah!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                dialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}