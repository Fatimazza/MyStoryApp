package xyz.codingwithza.mystoryapp.view.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import xyz.codingwithza.mystoryapp.R
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.databinding.ActivityAddStoryBinding
import xyz.codingwithza.mystoryapp.util.createCustomTempFile
import xyz.codingwithza.mystoryapp.util.reduceFileImage
import xyz.codingwithza.mystoryapp.util.rotateFile
import xyz.codingwithza.mystoryapp.util.uriToFile
import xyz.codingwithza.mystoryapp.view.ViewModelFactory
import java.io.File
import java.util.*
import kotlin.concurrent.schedule


class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
        initViewModel()
        setupButtonAction()
    }

    private fun requestPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        addStoryViewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
    }

    private fun setupButtonAction() {
        binding.btnAddGallery.setOnClickListener {
            openIntentGallery()
        }
        binding.btnAddCamera.setOnClickListener {
            openIntentCamera()
        }
        binding.btnAddStory.setOnClickListener {
            addStory()
        }
    }

    private fun addStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                binding.etAddStoryDesc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            addStoryViewModel.getUserData().observe(this) {
                addStoryViewModel.uploadStory(imageMultipart, description)
                    .observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    Snackbar.make(
                                        binding.root,
                                        result.data,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    showLoading(false)
                                    Timer().schedule(1000) {
                                        finish()
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Snackbar.make(
                                        binding.root,
                                        getString(R.string.story_add_error) + result.error,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
            }
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.story_add_warning),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun openIntentGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.story_choose_gallery))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.ivAddPreview.setImageURI(uri)
            }
        }
    }

    private fun openIntentCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "xyz.codingwithza.mystoryapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                rotateFile(file)
                getFile = file
                binding.ivAddPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this@AddStoryActivity,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                btnAddGallery.visibility = View.GONE
                btnAddCamera.visibility = View.GONE
                btnAddStory.visibility = View.GONE
                pbAddStory.visibility = View.VISIBLE
            } else {
                btnAddGallery.visibility = View.VISIBLE
                btnAddCamera.visibility = View.VISIBLE
                btnAddStory.visibility = View.VISIBLE
                pbAddStory.visibility = View.GONE
            }
        }
    }
}
