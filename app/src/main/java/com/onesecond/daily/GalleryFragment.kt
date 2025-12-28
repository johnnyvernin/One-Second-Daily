package com.onesecond.daily

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class GalleryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var cleanButton: com.google.android.material.floatingactionbutton.FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        emptyText = view.findViewById(R.id.emptyText)
        cleanButton = view.findViewById(R.id.cleanButton)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        cleanButton.setOnClickListener {
            cleanAllTrashedFiles()
        }

        loadVideos()
    }

    private fun loadVideos() {
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val appDir = File(dcimDir, "OneSecondDaily")

        if (!appDir.exists()) {
            emptyText.visibility = View.VISIBLE
            return
        }

        // Limpar arquivos .trashed
        cleanTrashedFiles(appDir)

        val videos = appDir.listFiles { file ->
            file.extension.lowercase() == "mp4" && !file.name.contains(".trashed")
        }?.sortedBy { it.name } ?: emptyList()

        if (videos.isEmpty()) {
            emptyText.visibility = View.VISIBLE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.adapter = VideoAdapter(videos.toList())
        }
    }

    private fun cleanTrashedFiles(directory: File) {
        directory.listFiles()?.forEach { file ->
            if (file.name.contains(".trashed") || file.name.startsWith(".")) {
                file.delete()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadVideos()
    }

    private fun cleanAllTrashedFiles() {
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val appDir = File(dcimDir, "OneSecondDaily")

        if (!appDir.exists()) return

        var count = 0
        appDir.listFiles()?.forEach { file ->
            if (file.name.contains(".trashed") || (file.name.startsWith(".") && file.isFile)) {
                if (file.delete()) {
                    count++
                }
            }
        }

        if (count > 0) {
            Toast.makeText(requireContext(), "$count arquivo(s) limpo(s)", Toast.LENGTH_SHORT).show()
            loadVideos()
        } else {
            Toast.makeText(requireContext(), "Nenhum arquivo para limpar", Toast.LENGTH_SHORT).show()
        }
    }
}

class VideoAdapter(private val videos: List<File>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]

        // Extrair data do nome do arquivo (formato: 20260101_120000.mp4)
        val fileName = video.nameWithoutExtension
        val datePart = fileName.substringBefore("_")
        val formattedDate = if (datePart.length == 8) {
            "${datePart.substring(6, 8)}/${datePart.substring(4, 6)}/${datePart.take(4)}"
        } else {
            fileName
        }

        holder.dateText.text = formattedDate
        holder.itemView.setOnClickListener {
            playVideo(video, holder.itemView.context)
        }
    }

    override fun getItemCount() = videos.size

    private fun playVideo(file: File, context: android.content.Context) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "video/mp4")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Reproduzir vídeo"))
    }
}