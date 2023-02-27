package com.example.webtoonapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.webtoonapp.databinding.FragmentWebviewBinding

class WebViewFragment(private val position: Int, private val webViewUrl: String) : Fragment() {

    var listener: OnTabLayoutNameChanged? = null

    private lateinit var binding: FragmentWebviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            webViewClient = WebtoonWebViewClient(binding.progressBar) { url ->
                activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                    putString("tab$position", url)
                }
            }
            settings.javaScriptEnabled = true
            loadUrl(webViewUrl)
        }

        binding.backToLastButton.setOnClickListener {
            val sharedPreference =
                activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
            val url = sharedPreference?.getString("tab$position", "")
            if (url.isNullOrEmpty()) {
                Toast.makeText(context, "마지막 저장 시점이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                binding.webView.loadUrl(url)
            }
        }

        binding.changeTabNameButton.setOnClickListener {
            val editText = EditText(requireContext())
            AlertDialog.Builder(requireContext()).apply {
                setView(editText)
                setPositiveButton("저장") { _, _ ->
                    activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                        putString("tab${position}_name", editText.text.toString())
                        listener?.nameChanged(position, editText.text.toString())
                    }
                }
                setNegativeButton("취소") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
            }.show()

        }
    }

    fun canGoBack(): Boolean {
        return binding.webView.canGoBack()
    }

    fun goBack() {
        binding.webView.goBack()
    }

    companion object {
        const val SHARED_PREFERENCE = "WEB_HISTORY"
    }
}

interface OnTabLayoutNameChanged {
    fun nameChanged(position: Int, name: String)
}