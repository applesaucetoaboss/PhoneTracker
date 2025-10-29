package com.example.whatsapponlineviewer

import android.app.Application
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val stack = sw.toString()

                val dir = getExternalFilesDir(null) ?: filesDir
                val file = File(dir, "crash_log.txt")
                val header = "=== Crash: ${System.currentTimeMillis()} (thread=${thread.name}) ===\n"
                file.appendText(header)
                file.appendText(stack)
                file.appendText("\n\n")
            } catch (e: Exception) {
                // ignore failures writing the log
            }

            try { Thread.sleep(700) } catch (_: Exception) {}
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
        }
    }
}
