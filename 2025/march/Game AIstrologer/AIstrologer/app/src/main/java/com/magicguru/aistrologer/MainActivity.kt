package com.magicguru.aistrologer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.utils.Array
import com.magicguru.aistrologer.databinding.ActivityMainBinding
import com.magicguru.aistrologer.game.GDX_GLOBAL_isPauseGame
import com.magicguru.aistrologer.game.utils.gdxGame
import com.magicguru.aistrologer.util.KeyboardHeightListener
import com.magicguru.aistrologer.util.OneTime
import com.magicguru.aistrologer.util.WebViewHelper
import com.magicguru.aistrologer.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    companion object {
        var statusBarHeight = 0
        var navBarHeight    = 0
    }

    val coroutine  = CoroutineScope(Dispatchers.Default)
    private val onceExit   = OneTime()

    private val onceSystemBarHeight = OneTime()

    lateinit var binding : ActivityMainBinding

    val windowInsetsController by lazy { WindowCompat.getInsetsController(window, window.decorView) }

    val blockKeyboardHeight = Array<(Int) -> Unit>()

    lateinit var webViewHelper: WebViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initialize()

        startKeyboardHeightListener()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            onceSystemBarHeight.use {
                statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

                // hide Status or Nav bar (після встановлення їх розмірів)
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            if (binding.webView.isVisible) {
                val imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val navBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                val totalBottom = maxOf(imeBottom, navBottom)

                binding.root.setPadding(0, statusBarHeight, 0, totalBottom)
                log("ime = $imeBottom | navBar = $navBottom | total = $totalBottom")
            }

            WindowInsetsCompat.CONSUMED
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

    fun hideWebView() {
        runOnUiThread {
            binding.webView.visibility = View.GONE
            binding.webView.loadUrl("about:blank")
            binding.root.setPadding(0, 0, 0, 0)

            binding.navHostFragment.requestFocus()
            GDX_GLOBAL_isPauseGame = false
            gdxGame.resume()
        }
    }

    fun showWebView() {
        runOnUiThread {
            binding.root.setPadding(0, statusBarHeight, 0, 0)
            binding.webView.visibility = View.VISIBLE

            binding.webView.requestFocus()
            GDX_GLOBAL_isPauseGame = true
            gdxGame.pause()
        }
    }

    // Keyboard ----------------------------------------------------------------------------------

    private fun startKeyboardHeightListener() = KeyboardHeightListener(binding.root) { keyboardHeight ->
        blockKeyboardHeight.onEach {
            if (binding.webView.isVisible.not()) it(keyboardHeight)
        }
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