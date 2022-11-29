package space.beka.newpasport.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import space.beka.newpasport.adapters.MyData
import space.beka.newpasport.adapters.RvAdapter
import space.beka.newpasport.databinding.FragmentAddBinding
import space.beka.newpasport.db.AppDatabase
import space.beka.newpasport.db.Person
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var list: ArrayList<Person>
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: RvAdapter
    private lateinit var person: Person

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)
        binding.addImage.setOnClickListener {
            getImageContent.launch("image/*")

        }

        appDatabase = AppDatabase.getInstance(binding.root.context)
        list = ArrayList()
        list.addAll(appDatabase.myDao().getAllPasports())
        adapter = RvAdapter(list)

        binding.apply {
            rvList.adapter = adapter
            binding.btnSave.setOnClickListener {
                val myCard = Person(
                    "Ismi :" + edtName.text.toString(),
                    "Familyasi :" + edtLastName.text.toString(),
                    path
                )
                appDatabase.myDao().addPasport(myCard)
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                list.add(myCard)
                adapter.notifyItemInserted(list.size - 1)
            }
        }

        return binding.root
    }

    private fun seriyaRaqam(listSeriya: List<String>): String {
        var seriya = ""
        var a = Random().nextInt(25)
        var b = Random().nextInt(25)
        var q = 0
        for (x in 'A'..'Z') {
            if (q == a) {
                seriya += x
            }
            if (q == b) {
                seriya += x
            }
            q++
        }

        for (i in 0..6) {
            seriya += Random().nextInt(10)
        }
        for (s in listSeriya) {
            if (s == seriya) {
                seriyaRaqam(listSeriya)
                break
            }
        }
        return seriya
    }


    var path = ""
    @SuppressLint("SimpleDateFormat")
    val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it ?: return@registerForActivityResult
            binding.addImage.setImageURI(it)
            val inputStream = requireActivity().contentResolver.openInputStream(it)
            val title = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
            val file = File(requireActivity().filesDir, "$title.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            path = file.absolutePath
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            MyData.path = path
        }
    val getitemContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.addImage.setImageURI(uri)
        }

}