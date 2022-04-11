package edu.temple.audibleonmobileapplication

import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment


class TranslationPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_translation_page, container, false)
        val button = root.findViewById<Button>(R.id.back_to_home)
        val done_button = root.findViewById<Button>(R.id.done)
        val switch = root.findViewById<Switch>(R.id.camera_button)

        button.setOnClickListener {
            val fragment = HomePageFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        done_button.setOnClickListener {
            val fragment = DisplayPageFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        switch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                val intent = Intent(activity, CameraActivity::class.java)
                startActivity(intent)
            }
        }

        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TranslationPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}