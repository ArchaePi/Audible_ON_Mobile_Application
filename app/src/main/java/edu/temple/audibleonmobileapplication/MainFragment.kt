package edu.temple.audibleonmobileapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.*
import java.io.IOException


class MainFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val webView = root.findViewById<WebView>(R.id.webView)

        webView.loadUrl("http://ec2-54-234-5-62.compute-1.amazonaws.com:5000/")

        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url("http://ec2-54-234-5-62.compute-1.amazonaws.com:5000/").build()

        okHttpClient.run {

            newCall(request).enqueue(object : Callback {
                // called if server is unreachable
                override fun onFailure(call: Call, e: IOException) {
                    activity?.runOnUiThread(Runnable {
                        Toast.makeText(activity, "server down", Toast.LENGTH_SHORT).show()
                        //pagenameTextView.text = "error connecting to the server"
                    })
                }


                @Throws(IOException::class)
                override  // called if we get a
                // response from the server
                fun onResponse(
                    call: Call,
                    response: Response,
                ) {
                    //webView.settings.javaScriptEnabled = true
                    //webView.loadData(response.body!!.string(), "text/html; charset=utf-8", "UTF-8")
                    //pagenameTextView.text = response.body!!.string()
                }
            })

        }
            // Inflate the layout for this fragment
            return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}