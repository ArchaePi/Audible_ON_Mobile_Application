package edu.temple.audibleonmobileapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.HandlerThread
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.impl.ImageAnalysisConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import edu.temple.audibleonmobileapplication.databinding.ActivityCameraBinding
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit

class CameraActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private lateinit var viewBinding: ActivityCameraBinding

    private lateinit var cameraProvider: ProcessCameraProvider

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val back_button = findViewById<Button>(R.id.back_to_home)

        // Request camera permissions
        if (allPermissionsGranted()) {
            viewBinding.cameraButton.setOnCheckedChangeListener { _, b ->
                if(b){
                    startCamera()
                }else{
                    stopCamera()
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        back_button.setOnClickListener {
            finish()
        }

        // Set up the listeners for take photo and video capture buttons
        //viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        //viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {}

    private fun captureVideo() {}

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    @SuppressLint("ResourceAsColor")
    private fun stopCamera(){
       ProcessCameraProvider.getInstance(this).get().unbindAll()
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

   companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun analyze(image: ImageProxy) {
        TODO("Not yet implemented")
    }

    private fun setImageAnalysis(): ImageAnalysis? {

        // Setup image analysis pipeline that computes average pixel luminance
        val analyzerThread = HandlerThread("OpenCVAnalysis")
        analyzerThread.start()
        val imageAnalysisConfig: ImageAnalysisConfig = Builder()
            .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            .setCallbackHandler(Handler(analyzerThread.looper))
            .setImageQueueDepth(1).build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)
        imageAnalysis.setAnalyzer(
            object : ImageAnalysis.Analyzer {
                fun analyze(image: ImageProxy?, rotationDegrees: Int) {
                    //Analyzing live camera feed begins.
                    val bitmap: Bitmap = textureView.getBitmap() ?: return
                    val mat = Mat()
                    Utils.bitmapToMat(bitmap, mat)
                    Imgproc.cvtColor(mat, mat, currentImageType)
                    Utils.matToBitmap(mat, bitmap)
                    runOnUiThread { ivBitmap.setImageBitmap(bitmap) }
                }
            })
        return imageAnalysis
    }
}