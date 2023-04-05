package com.myapplication.others

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonObject
import com.myapplication.others.Toaster.shortToast
import com.myapplication.base.BaseActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

class MyUtils {
    val min = 0
    val max = 3


    fun viewGone(view: View?) {
        if (view != null) {
            view.visibility = View.GONE

        }
    }

    fun viewInvisible(view: View?) {
        if (view != null && view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        }
    }

    fun viewVisible(view: View?) {
        if (view != null && (view.visibility == View.INVISIBLE || view.visibility == View.GONE)) {
            view.visibility = View.VISIBLE

        }
    }

    fun isEmptyString(value: String?): Boolean {
        return TextUtils.isEmpty(value) || TextUtils.isEmpty(value?.trim())
    }

    fun getVideoURI_from_Url(url: String?): String {
        return ""
    }

    @Throws(IOException::class)
    fun downloadUrl(myUrl: String?): String? {
        var input: InputStream? = null
        return try {
            val url = URL(myUrl)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 10000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.connect()
            input = conn.inputStream
            readIt(input)
        } finally {
            input?.close()
        }
    }

    @Throws(IOException::class)
    private fun readIt(stream: InputStream?): String? {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        val sb = StringBuilder()
        var line: String? = ""

        while (reader.readLine()?.let { line = it } != null) {
            if (line?.contains("fmt_stream_map") == true) {
                sb.append(
                    """
                    $line
                    
                    """.trimIndent()
                )
                break
            }
        }
        reader.close()
        val result = decode(sb.toString())
        val url = result!!.split("\\|".toRegex()).toTypedArray()
        return if (url.size > 1) url[1] else url[0]
    }

    private fun decode(input: String): String? {
        var working = input
        var index: Int
        index = working.indexOf("\\u")
        while (index > -1) {
            val length = working.length
            if (index > length - 6) break
            val numStart = index + 2
            val numFinish = numStart + 4
            val substring = working.substring(numStart, numFinish)
            val number = substring.toInt(16)
            val stringStart = working.substring(0, index)
            val stringEnd = working.substring(numFinish)
            working = stringStart + number.toChar() + stringEnd
            index = working.indexOf("\\u")
        }
        return working
    }

    companion object {
        fun viewGone(view: View?) {
            if (view != null) {
                view.visibility = View.GONE

            }
        }

        fun viewsGone(view: List<View?>?) {
            if (!view.isNullOrEmpty()) {
                for (item in view) {
                    viewGone(item)
                }
            }
        }

        fun viewsVisible(view: List<View?>?) {
            if (!view.isNullOrEmpty()) {
                for (item in view) {
                    viewVisible(item)
                }
            }
        }


        fun viewInvisible(view: View?) {
            if (view != null) {
                view.visibility = View.INVISIBLE

            }
        }

        fun viewVisible(view: View?) {
            if (view != null && (view.visibility == View.INVISIBLE || view.visibility == View.GONE)) {
                view.visibility = View.VISIBLE

            }
        }

        fun isEmptyString(value: String?): Boolean {
            return TextUtils.isEmpty(value) || TextUtils.isEmpty(value?.trim())
        }

        fun isPasswordValid(value: String?): Boolean {
            if (value?.length!! < 8)
                return true
            return false
        }

        fun contains(value: String?, match: String?): Boolean {
            return !isEmptyString(value) && !isEmptyString(match) && value!!.contains(match!!)
        }

        fun convertToJSON(body: JsonObject?): JSONObject {
            return JSONObject(body?.toString() ?: "")
        }

        fun setText(view: TextView?, value: String?) {
            viewVisible(view)
            if (view != null) {
                if (value != null && value != "") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view?.text = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        view?.text = Html.fromHtml(value)
                    }
                } else {
                    view.text = "--"
                }


            }
        }

        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

        fun capitalize(input: String): String? {
            val words = input.lowercase().split(" ").toTypedArray()
            val builder = java.lang.StringBuilder()
            for (i in words.indices) {
                val word = words[i]
                if (i > 0 && word.length > 0) {
                    builder.append(" ")
                }
                val cap = word.substring(0, 1).uppercase() + word.substring(1)
                builder.append(cap)
            }
            return builder.toString()
        }


        fun getWorkoutImageUrl(value: String?): String {
            return Cons.BASE_URL + value
        }


        fun getUPImage(value: Any): String {
            return Cons.BASE_URL + value
        }

        fun setImage(view: ImageView?, value: Int?) {
            viewVisible(view)
            view?.setImageResource(value!!)
        }

        fun setUnclickable(view: View?) {
            viewVisible(view)
            view?.isEnabled = false
        }

        fun decimalFormat(floatNumber: Float): String {
            val format = DecimalFormat("##.##")
            //format.format(floatNumber)
            val formatted: String = String.format("%.2f", floatNumber)
            return formatted
        }


        fun enableFullScreenView(decorView: View) {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        fun disableFullScreen(window: Window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }


        fun isEmptyList(items: List<Any?>?): Boolean {
            return items == null || items.isEmpty()
        }



        fun isYouTubeUrlValid(videoUrl: String?): Boolean {
            if (isEmptyString(videoUrl)) {
                //shortToast("Empty YouTube link ...")
                return false
            } else if (videoUrl!!.startsWith(Cons.YOUTUBE_LINK_MATCHER)) {
                //shortToast("Invalid YouTube link ...")
                return true
            } else if (videoUrl.startsWith(Cons.SHORT_YOUTUBE_LINK_MATCHER)) {
                return true
            }
            return false
        }

        fun isValidVideoUrl(videoUrl: String?): Boolean {
            if (isEmptyString(videoUrl)) {
                shortToast("Empty YouTube link ...")
                return false
            } else if (!videoUrl!!.contains(Cons.YOUTUBE_LINK_MATCHER)) {
                shortToast("Invalid YouTube link ...")
                return false
            }
            return true
        }

        fun getIntent(
            context: Context?,
            toClass: Class<out BaseActivity?>
        ): Intent? {
            return Intent(context, toClass.javaClass)
        }

    }

}
