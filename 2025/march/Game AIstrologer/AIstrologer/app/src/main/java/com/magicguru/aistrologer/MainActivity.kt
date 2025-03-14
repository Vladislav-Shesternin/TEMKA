package com.magicguru.aistrologer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.utils.Array
import com.magicguru.aistrologer.databinding.ActivityMainBinding
import com.magicguru.aistrologer.util.KeyboardHeightListener
import com.magicguru.aistrologer.util.OneTime
import com.magicguru.aistrologer.util.WebViewHelper
import com.magicguru.aistrologer.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.io.root
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    companion object {
        var statusBarHeight = 0
    }

    val coroutine  = CoroutineScope(Dispatchers.Default)
    private val onceExit   = OneTime()

    private val onceStatusBarHeight = OneTime()

    lateinit var binding : ActivityMainBinding

    val blockKeyboardHeight = Array<(Int) -> Unit>()

    lateinit var webViewHelper: WebViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        hideNavigationBar()

        initialize()

        startKeyboardHeightListener()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            onceStatusBarHeight.use {
                statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                hideStatusBar()
            }

            if (binding.webView.isVisible) {
                val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
                binding.root.setPadding(0, 0, 0, imeInsets.bottom)
            }

            insets
        }
    }

    override fun exit() {
        onceExit.use {
            log("exit")
            coroutine.launch(Dispatchers.Main) {
                finishAndRemoveTask()
                delay(100)
                exitProcess(0)
            }
        }
    }

    private fun initialize() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webViewHelper = WebViewHelper(this)
    }

    // UI Logic -----------------------------------------------------------------------------------------

    private fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 і вище
            window.insetsController?.let {
                it.hide(WindowInsets.Type.navigationBars()) // Ховаємо панель навігації
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Для API нижче 30
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 і вище
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars()) // Ховаємо панель навігації
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Для API нижче 30
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    fun hideWebView() {
        runOnUiThread {
            binding.webView.visibility         = View.GONE
            binding.navHostFragment.visibility = View.VISIBLE
            binding.navHostFragment.requestFocus()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            GLOBAL_isPauseGame = false
        }
    }

    fun showWebView() {
        runOnUiThread {
            binding.navHostFragment.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            binding.webView.requestFocus()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER

            GLOBAL_isPauseGame = true
        }
    }

    // Keyboard ----------------------------------------------------------------------------------

    private fun startKeyboardHeightListener() = KeyboardHeightListener(binding.root) { keyboardHeight ->
        blockKeyboardHeight.onEach { it(keyboardHeight) }
    }.startListening()

    // Dialog ----------------------------------------------------------------------------------------

    @SuppressLint("DefaultLocale")
    fun showBirthDatePicker(onDateSelected: (String) -> Unit) {
        runOnUiThread {
            val calendar    = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            // Дата за замовчуванням (наприклад, 18 років назад)
            calendar.set(currentYear - 18, 0, 1)

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.CustomDatePickerDialog, // ✅ Використовуємо кастомну тему
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format("%02d.%02d.%d", dayOfMonth, month + 1, year)
                    onDateSelected(formattedDate) // ✅ Передаємо вибрану дату
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Обмеження: мінімальна дата - 01.01.1900, максимальна - поточна дата
            val minCalendar = Calendar.getInstance().apply { set(1900, 0, 1) }
            datePickerDialog.datePicker.minDate = minCalendar.timeInMillis
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

            datePickerDialog.show()
        }
    }

}