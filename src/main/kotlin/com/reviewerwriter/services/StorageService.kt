package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.PhotoUploadResponse
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


@Service
class FileSystemStorageService {
    private var rootLocation: Path = Paths.get("C:\\Users\\KONSTANTIN\\Desktop")

    fun store(file: MultipartFile): Info {
        val info = Info()

        if (file.isEmpty) {
            info.errorInfo = ErrorMessages.FILE_IS_EMPTY
            return info
        }
        val filename = UUID.randomUUID().toString()
        val destinationFile: Path? = filename.let { Paths.get(it) }.let {
            rootLocation.resolve(it).normalize().toAbsolutePath()
        }
        file.inputStream.use { inputStream ->
            if (destinationFile != null) {
                Files.copy(
                    inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }
        info.response = PhotoUploadResponse(filename)

        return info
    }

    private fun load(filename: String?): Path? {
        return filename?.let { rootLocation.resolve(it) }
    }

    fun loadAsResource(filename: String): Info {
        val info = Info()

        val file = load(filename);
        val resource = file?.let { UrlResource(it.toUri()) };
        if (resource != null) {
            if (resource.exists() || resource.isReadable) {
                info.response = resource
            } else {
                info.errorInfo = ErrorMessages.ERROR_READING_FILE
            }
        }

        return info
    }
}