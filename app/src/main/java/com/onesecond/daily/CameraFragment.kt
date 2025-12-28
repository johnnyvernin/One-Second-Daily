package com.onesecond.daily

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private lateinit var previewView: PreviewView
    private lateinit var recordButton: FloatingActionButton
    private lateinit var statusText: TextView
    private lateinit var recordingIndicator: View
    private lateinit var switchCameraButton: ImageButton
    private lateinit var deleteButton: ImageButton

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var isRecording = false
    private var isFrontCamera = false
    private var currentCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar views
        previewView = view.findViewById(R.id.previewView)
        recordButton = view.findViewById(R.id.recordButton)
        statusText = view.findViewById(R.id.statusText)
        recordingIndicator = view.findViewById(R.id.recordingIndicator)
        switchCameraButton = view.findViewById(R.id.switchCameraButton)
        deleteButton = view.findViewById(R.id.deleteButton)

        startCamera()

        recordButton.setOnClickListener {
            if (!isRecording) {
                startRecording()
            }
        }

        switchCameraButton.setOnClickListener {
            switchCamera()
        }

        deleteButton.setOnClickListener {
            deleteTodayVideo()
        }

        checkIfAlreadyRecordedToday()
    }

    private fun switchCamera() {
        isFrontCamera = !isFrontCamera
        currentCameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Configuração de qualidade: usa 1080p (Full HD)
            // Se quiser 4K, mude para Quality.UHD
            // Se quiser 720p, mude para Quality.HD
            val qualitySelector = QualitySelector.fromOrderedList(
                listOf(Quality.FHD, Quality.HD, Quality.SD),
                FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
            )

            val recorder = Recorder.Builder()
                .setQualitySelector(qualitySelector)
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    currentCameraSelector,
                    preview,
                    videoCapture
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao iniciar câmera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("MissingPermission")
    private fun startRecording() {
        val videoCapture = this.videoCapture ?: return

        // Criar diretório OneSecondDaily dentro de DCIM
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val appDir = File(dcimDir, "OneSecondDaily")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        // Nome do arquivo com data (formato: 20260101_120000.mp4)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val fileName = "${dateFormat.format(Date())}.mp4"
        val videoFile = File(appDir, fileName)

        val outputOptions = FileOutputOptions.Builder(videoFile).build()

        recording = videoCapture.output
            .prepareRecording(requireContext(), outputOptions)
            .withAudioEnabled()
            .start(ContextCompat.getMainExecutor(requireContext())) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        isRecording = true
                        recordButton.isEnabled = false
                        switchCameraButton.isEnabled = false
                        recordingIndicator.visibility = View.VISIBLE
                        statusText.text = "Gravando..."

                        // Parar após 1 segundo
                        Handler(Looper.getMainLooper()).postDelayed({
                            stopRecording()
                        }, 1000)
                    }
                    is VideoRecordEvent.Finalize -> {
                        isRecording = false
                        recordButton.isEnabled = false
                        switchCameraButton.isEnabled = true
                        recordingIndicator.visibility = View.GONE

                        if (!recordEvent.hasError()) {
                            statusText.text = "Vídeo de hoje gravado! ✓"
                            PreferenceHelper.markTodayAsRecorded(requireContext(), videoFile.absolutePath)

                            // Scanear arquivo para aparecer na galeria
                            MediaScannerHelper.scanFile(requireContext(), videoFile)

                            checkIfAlreadyRecordedToday()
                        } else {
                            statusText.text = "Erro ao gravar"
                            Toast.makeText(requireContext(), "Erro ao salvar vídeo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
    }

    private fun checkIfAlreadyRecordedToday() {
        val videoPath = PreferenceHelper.getTodayVideoPath(requireContext())

        if (videoPath != null) {
            val videoFile = File(videoPath)

            // Verificar se o arquivo ainda existe
            if (videoFile.exists()) {
                statusText.text = "Vídeo de hoje já foi gravado! ✓"
                recordButton.isEnabled = false
                deleteButton.visibility = View.VISIBLE
            } else {
                // Arquivo foi deletado, permitir regravar
                PreferenceHelper.clearTodayRecord(requireContext())
                statusText.text = "Grave seu vídeo de 1 segundo!"
                recordButton.isEnabled = true
                deleteButton.visibility = View.GONE
            }
        } else {
            statusText.text = "Grave seu vídeo de 1 segundo!"
            recordButton.isEnabled = true
            deleteButton.visibility = View.GONE
        }
    }

    private fun deleteTodayVideo() {
        val videoPath = PreferenceHelper.getTodayVideoPath(requireContext())

        if (videoPath != null) {
            val videoFile = File(videoPath)

            if (videoFile.exists()) {
                if (videoFile.delete()) {
                    PreferenceHelper.clearTodayRecord(requireContext())
                    MediaScannerHelper.scanFile(requireContext(), videoFile)
                    Toast.makeText(requireContext(), "Vídeo deletado", Toast.LENGTH_SHORT).show()
                    checkIfAlreadyRecordedToday()
                } else {
                    Toast.makeText(requireContext(), "Erro ao deletar vídeo", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Arquivo já não existe
                PreferenceHelper.clearTodayRecord(requireContext())
                checkIfAlreadyRecordedToday()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Verificar sempre que voltar pra tela
        checkIfAlreadyRecordedToday()
    }
}