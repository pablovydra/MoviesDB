package com.example.movies.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.io.*

class ImageUtils {

    companion object {

        fun getUriFromBitmap(context: Context, bitmap: Bitmap?): Uri {
            var file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "bitmap"),
                System.currentTimeMillis().toString() + "_pic.jpg"
            )
            val uri = Uri.fromFile(file)
            try {
                val stream: OutputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return uri
        }

        fun getBitmapFromUri(context: Context, path: Uri): Bitmap {
            return MediaStore.Images.Media.getBitmap(context.contentResolver, path)
        }

        fun getBitmapFromVectorDrawable(
            context: Context?,
            drawableId: Int
        ): Bitmap? {
            var drawable = ContextCompat.getDrawable(context!!, drawableId)
            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        fun getTempFile(context: Context): File {
            val tempFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "temp"),
                System.currentTimeMillis().toString() + ".jpg"
            )
            tempFile.createNewFile()
            return tempFile
        }

        @Throws(IOException::class)
        fun saveBitmapToGallery(
            context: Context, bitmap: Bitmap, quality: Int,
            displayName: String
        ): Uri {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "Fanbase")
            }
            var uri: Uri? = null
            return runCatching {
                with(context.contentResolver) {
                    insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                        uri = it // Keep uri reference so it can be removed on failure
                        openOutputStream(it)?.use { stream ->
                            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream))
                                throw IOException("Failed to save bitmap.")
                        } ?: throw IOException("Failed to open output stream.")
                    } ?: throw IOException("Failed to create new MediaStore record.")
                }
            }.getOrElse {
                uri?.let { orphanUri ->
                    // Don't leave an orphan entry in the MediaStore
                    context.contentResolver.delete(orphanUri, null, null)
                }
                throw it
            }
        }
    }

}