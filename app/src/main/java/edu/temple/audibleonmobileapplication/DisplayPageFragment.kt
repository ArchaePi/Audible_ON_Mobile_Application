package edu.temple.audibleonmobileapplication

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.util.*


class DisplayPageFragment : Fragment(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var speakBtn: Button? = null
    private var editText : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_display_page, container, false)
        val back_button = root.findViewById<Button>(R.id.back_button)
        speakBtn = root.findViewById(R.id.audio_button)
        editText = root.findViewById(R.id.translated_text)
        //tts = TextToSpeech(activity?.applicationContext, this)//

        speakBtn!!.isEnabled = false
        tts = TextToSpeech(activity?.applicationContext, this)

        back_button.setOnClickListener {
            val fragment = HomePageFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        speakBtn!!.setOnClickListener {
            speakOut()
        }
        // Inflate the layout for this fragment
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DisplayPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                speakBtn!!.isEnabled = true
            }
        }
    }

    private fun speakOut() {
        val text = editText!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}