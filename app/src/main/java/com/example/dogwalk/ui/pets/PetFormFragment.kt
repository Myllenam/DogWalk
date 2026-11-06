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

    // Guarda a raça atual enquanto Room e Retrofit
    // carregam de forma assíncrona.
    private var currentBreed: String? = null

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

        // -------------------------
        // ViewModel
        // -------------------------

        viewModel =
            ViewModelProvider(this)[
                PetFormViewModel::class.java
            ]

        petId =
            arguments?.getLong("petId") ?: 0

        // -------------------------
        // Views
        // -------------------------

        val btnDelete =
            view.findViewById<MaterialButton>(
                R.id.btnDelete
            )

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

        // -------------------------
        // Observers
        // -------------------------

        observePet()
        observeBreeds()

        // Retrofit
        viewModel.loadBreeds()

        // -------------------------
        // Cadastro ou edição
        // -------------------------

        if (petId == 0L) {

            tvTitle.text = "Novo Pet"
            btnSave.text = "Cadastrar Pet"

            btnDelete.visibility =
                View.GONE


        } else {

            tvTitle.text = "Editar Pet"
            btnSave.text = "Salvar alterações"

            btnDelete.visibility =
                View.VISIBLE

            // Agora o pet vem do Room.
            viewModel.loadPet(petId)
        }

        // -------------------------
        // Eventos
        // -------------------------

        btnBack.setOnClickListener {

            parentFragmentManager
                .popBackStack()
        }

        btnDelete.setOnClickListener {

            showDeleteConfirmation()
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

    // =========================================================
    // PET - ROOM
    // =========================================================

    private fun observePet() {

        viewModel.pet.observe(
            viewLifecycleOwner
        ) { pet ->

            pet ?: return@observe

            edtName.setText(
                pet.name
            )

            // Guardamos porque talvez o Retrofit ainda
            // não tenha carregado o Spinner.
            currentBreed = pet.breed

            if (pet.sex == "Macho") {

                radioMale.isChecked = true

            } else {

                radioFemale.isChecked = true
            }

            edtBirthDate.setText(
                pet.birthDate
            )

            pet.photoUri?.let { uri ->

                photoUri =
                    Uri.parse(uri)

                try {

                    imgPet.setImageURI(
                        photoUri
                    )

                } catch (e: Exception) {

                    imgPet.setImageResource(
                        R.drawable.ic_pets
                    )
                }
            }

            // Caso as raças já tenham chegado,
            // seleciona imediatamente.
            selectCurrentBreed(
                pet.breed
            )
        }
    }

    // =========================================================
    // Deletar pet
    // =========================================================
    private fun deletePet() {

        viewModel.deletePet()

        Toast.makeText(
            requireContext(),
            "Pet excluído com sucesso!",
            Toast.LENGTH_SHORT
        ).show()

        parentFragmentManager
            .popBackStack()
    }
    private fun showDeleteConfirmation() {

        androidx.appcompat.app.AlertDialog
            .Builder(requireContext())
            .setTitle("Excluir pet")
            .setMessage(
                "Deseja realmente excluir este pet? " +
                        "Todos os passeios relacionados a ele também serão excluídos."
            )
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                "Excluir"
            ) { _, _ ->

                deletePet()
            }
            .show()
    }

    // =========================================================
    // RAÇAS - RETROFIT
    // =========================================================

    private fun observeBreeds() {

        viewModel.breeds.observe(
            viewLifecycleOwner
        ) { breeds ->

            val breedList =
                mutableListOf(
                    "Selecione a raça"
                )

            breedList.addAll(
                breeds
            )

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    breedList
                )

            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            spinnerBreed.adapter =
                adapter

            // Se estamos editando e o pet já veio do Room,
            // seleciona a raça depois que o Retrofit terminar.
            currentBreed?.let { breed ->

                selectCurrentBreed(
                    breed
                )
            }
        }
    }

    private fun selectCurrentBreed(
        currentBreed: String
    ) {

        val adapter =
            spinnerBreed.adapter
                ?: return

        for (i in 0 until adapter.count) {

            val breed =
                adapter.getItem(i)
                    ?.toString()
                    ?: continue

            if (
                breed.equals(
                    currentBreed,
                    ignoreCase = true
                )
            ) {

                spinnerBreed.setSelection(i)
                break
            }
        }
    }

    // =========================================================
    // SALVAR
    // =========================================================

    private fun savePet() {

        val name =
            edtName.text
                .toString()
                .trim()

        val breed =
            spinnerBreed.selectedItem
                ?.toString()
                .orEmpty()

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

        // -------------------------
        // Validações
        // -------------------------

        if (name.isEmpty()) {

            edtName.error =
                "Informe o nome do pet"

            return
        }

        if (
            breed.isEmpty() ||
            breed == "Selecione a raça"
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

        // -------------------------
        // Room
        // -------------------------

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
            if (petId == 0L) {
                "Pet cadastrado com sucesso!"
            } else {
                "Pet atualizado com sucesso!"
            },
            Toast.LENGTH_SHORT
        ).show()

        parentFragmentManager
            .popBackStack()
    }

    // =========================================================
    // CÂMERA
    // =========================================================

    private fun openCamera() {

        photoUri =
            createImageUri()

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

    // =========================================================
    // DATA DE NASCIMENTO
    // =========================================================

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

        // Não permite nascimento no futuro.
        dialog.datePicker.maxDate =
            System.currentTimeMillis()

        dialog.show()
    }
}