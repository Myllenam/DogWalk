package com.example.dogwalk.ui.pets

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dogwalk.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.Calendar

class PetFormFragment : Fragment() {

    private lateinit var viewModel: PetFormViewModel

    private var petId: Long = 0
    private var photoUri: Uri? = null

    private lateinit var imgPet: ImageView
    private lateinit var edtName: EditText
    private lateinit var spinnerBreed: Spinner
    private lateinit var radioMale: RadioButton
    private lateinit var radioFemale: RadioButton
    private lateinit var edtBirthDate: EditText

    private val takePictureLauncher =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->

            if (success && photoUri != null) {

                imgPet.setImageURI(photoUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_pet_form,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        viewModel =
            ViewModelProvider(this)[
                PetFormViewModel::class.java
            ]

        petId =
            arguments?.getLong("petId") ?: 0

        val tvTitle =
            view.findViewById<TextView>(
                R.id.tvFormTitle
            )

        val btnBack =
            view.findViewById<ImageButton>(
                R.id.btnBack
            )

        val btnCamera =
            view.findViewById<FloatingActionButton>(
                R.id.btnCamera
            )

        val btnSave =
            view.findViewById<MaterialButton>(
                R.id.btnSave
            )

        imgPet =
            view.findViewById(
                R.id.imgPet
            )

        edtName =
            view.findViewById(
                R.id.edtName
            )

        spinnerBreed =
            view.findViewById(
                R.id.spinnerBreed
            )

        radioMale =
            view.findViewById(
                R.id.radioMale
            )

        radioFemale =
            view.findViewById(
                R.id.radioFemale
            )

        edtBirthDate =
            view.findViewById(
                R.id.edtBirthDate
            )

        setupBreedSpinner()

        if (petId == 0L) {

            tvTitle.text = "Novo Pet"
            btnSave.text = "Cadastrar Pet"

        } else {

            tvTitle.text = "Editar Pet"
            btnSave.text = "Salvar alterações"

            loadPet()
        }

        btnBack.setOnClickListener {

            parentFragmentManager
                .popBackStack()
        }

        btnCamera.setOnClickListener {

            openCamera()
        }

        edtBirthDate.setOnClickListener {

            openDatePicker()
        }

        btnSave.setOnClickListener {

            savePet()
        }
    }

    private fun setupBreedSpinner() {

        val breeds = listOf(
            "Selecione a raça",
            "Golden Retriever",
            "Labrador Retriever",
            "Border Collie",
            "Shih-tzu",
            "Poodle",
            "Beagle",
            "Bulldog",
            "Pastor Alemão",
            "Vira-lata",
            "Outra"
        )

        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                breeds
            )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerBreed.adapter = adapter
    }

    private fun loadPet() {

        val pet =
            viewModel.getPet(petId)
                ?: return

        edtName.setText(pet.name)

        val adapter =
            spinnerBreed.adapter

        for (i in 0 until adapter.count) {

            if (
                adapter.getItem(i)
                    .toString() == pet.breed
            ) {

                spinnerBreed.setSelection(i)
                break
            }
        }

        if (pet.sex == "Macho") {

            radioMale.isChecked = true

        } else {

            radioFemale.isChecked = true
        }

        edtBirthDate.setText(
            pet.birthDate
        )

        pet.photoUri?.let {

            photoUri = Uri.parse(it)

            imgPet.setImageURI(
                photoUri
            )
        }
    }

    private fun savePet() {

        val name =
            edtName.text.toString().trim()

        val breed =
            spinnerBreed.selectedItem
                .toString()

        val sex =
            when {

                radioMale.isChecked ->
                    "Macho"

                radioFemale.isChecked ->
                    "Fêmea"

                else ->
                    ""
            }

        val birthDate =
            edtBirthDate.text
                .toString()
                .trim()

        if (name.isEmpty()) {

            edtName.error =
                "Informe o nome do pet"

            return
        }

        if (
            breed ==
            "Selecione a raça"
        ) {

            Toast.makeText(
                requireContext(),
                "Selecione a raça",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (sex.isEmpty()) {

            Toast.makeText(
                requireContext(),
                "Selecione o sexo",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (birthDate.isEmpty()) {

            edtBirthDate.error =
                "Informe a data de nascimento"

            return
        }

        viewModel.savePet(
            id = petId,
            name = name,
            breed = breed,
            sex = sex,
            birthDate = birthDate,
            photoUri =
                photoUri?.toString()
        )

        Toast.makeText(
            requireContext(),
            if (petId == 0L)
                "Pet cadastrado com sucesso!"
            else
                "Pet atualizado com sucesso!",
            Toast.LENGTH_SHORT
        ).show()

        parentFragmentManager
            .popBackStack()
    }

    private fun openCamera() {

        photoUri = createImageUri()

        takePictureLauncher.launch(
            photoUri
        )
    }

    private fun createImageUri(): Uri {

        val imageDirectory =
            File(
                requireContext().filesDir,
                "pets"
            )

        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs()
        }

        val file =
            File(
                imageDirectory,
                "pet_${System.currentTimeMillis()}.jpg"
            )

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }

    private fun openDatePicker() {

        val calendar =
            Calendar.getInstance()

        val dialog =
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    val date =
                        "%02d/%02d/%04d".format(
                            day,
                            month + 1,
                            year
                        )

                    edtBirthDate.setText(
                        date
                    )
                },
                calendar.get(
                    Calendar.YEAR
                ),
                calendar.get(
                    Calendar.MONTH
                ),
                calendar.get(
                    Calendar.DAY_OF_MONTH
                )
            )

        dialog.datePicker.maxDate =
            System.currentTimeMillis()

        dialog.show()
    }
}