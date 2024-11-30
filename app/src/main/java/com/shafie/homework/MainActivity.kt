package com.shafie.homework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.google.android.material.snackbar.Snackbar
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private val channelId = "notification_channel"

    // Permission request launcher for POST_NOTIFICATIONS
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request POST_NOTIFICATIONS permission if targeting Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the permission is already granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Show Snackbar
        findViewById<Button>(R.id.btn_show_snackbar).setOnClickListener {
            Snackbar.make(it, "Your request has been sent successfully.", Snackbar.LENGTH_LONG)
                .setAction("OK") {
                    Toast.makeText(this, "OK clicked", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Show AlertDialog
        findViewById<Button>(R.id.btn_show_alertdialog).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("Are you sure you want to save?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { _, _ ->
                    Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("Maybe") { _, _ ->
                    Toast.makeText(this, "Remind me later", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Show Notification
        findViewById<Button>(R.id.btn_show_notification).setOnClickListener {
            // Before showing notification, check if permission is granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                    showNotification()
                } else {
                    Toast.makeText(this, "Android ATC Notification", Toast.LENGTH_SHORT).show()
                }
            } else {
                showNotification() // For Android versions below 13
            }
        }

        // Show WebView Activity
        findViewById<Button>(R.id.btn_show_webview).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to show a Notification
    private fun showNotification() {
        try {
            // Create Notification Channel for Android 8.0+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "test description"
                }
                val manager = getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }

            // Build the notification
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Android ATC Notification")
                .setContentText("Check Android ATC New Course!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Notify the user
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(1, builder.build())
        } catch (e: SecurityException) {
            // Handle the case when the permission is not granted
            Toast.makeText(this, "Notification failed: Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
