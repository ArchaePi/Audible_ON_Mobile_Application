package edu.temple.audibleonmobileapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mediapipe.solutions.hands.Hands
import com.google.mediapipe.solutions.hands.HandsOptions
import edu.temple.audibleonmobileapplication.databinding.ActivityCameraBinding
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit

class CameraActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private var hands: Hands? = null

    // Run the pipeline and the model inference on GPU or CPU.
    private val RUN_ON_GPU = false

    private lateinit var viewBinding: ActivityCameraBinding

    private lateinit var cameraProvider: ProcessCameraProvider

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        OpenCVLoader.initDebug()

        val back_button = findViewById<Button>(R.id.back_to_home)

        // Request camera permissions
        if (allPermissionsGranted()) {
            viewBinding.cameraButton.setOnCheckedChangeListener { _, b ->
                if(b){
                    startCamera()
                    viewBinding.wordMode.setOnClickListener {
                        viewBinding.image.visibility = View.VISIBLE
                    }
                    viewBinding.fingerSpell.setOnClickListener {
                        viewBinding.image.visibility = View.GONE
                    }
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
            // Image analysis use case
            // Image analysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor, this)

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis)

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
        Log.d("TAG", "analyze: got the frame at: " + image.imageInfo.timestamp)
        runOnUiThread {
            val viewFinder = findViewById<PreviewView>(R.id.viewFinder)
            val bitmap: Bitmap? = viewFinder.bitmap

            image.close()

            if (bitmap == null)
                return@runOnUiThread

            val bitmap1: Bitmap? = bitmap?.let { makeGray(it) }

            viewBinding.image.setImageBitmap(bitmap1)
        }
    }



    private fun makeGray(bitmap: Bitmap) : Bitmap {

        // Create OpenCV mat object and copy content from bitmap
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        // Convert to grayscale
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)

        // Make a mutable bitmap to copy grayscale image
        val grayBitmap = bitmap.copy(bitmap.config, true)
        Utils.matToBitmap(mat, grayBitmap)

        return grayBitmap
    }

    private fun makeHands(bitmap: Bitmap) : Bitmap{
        hands = Hands(
            this,
            HandsOptions.builder()
                .setStaticImageMode(false)
                .setMaxNumHands(2)
                .setRunOnGpu(RUN_ON_GPU)
                .build())

        hands!!.setErrorListener { message: String, e: RuntimeException? ->
            Log.e(TAG,
                "MediaPipe Hands error:$message")
        }
        hands!!.send(bitmap)

        return bitmap

    }
}