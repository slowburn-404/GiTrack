package dev.borisochieng.gitrack.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.internal.GsonBuildConfig
import dev.borisochieng.gitrack.BuildConfig
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import dev.borisochieng.gitrack.data.GitHubAuthService
import dev.borisochieng.gitrack.data.RetrofitClient
import dev.borisochieng.gitrack.databinding.FragmentLoginBinding
import dev.borisochieng.gitrack.ui.viewmodels.LoginViewModel
import dev.borisochieng.gitrack.ui.viewmodels.LoginViewModelFactory
import dev.borisochieng.gitrack.utils.AccessTokenManager
import dev.borisochieng.gitrack.utils.Constants.CLIENT_ID
import dev.borisochieng.gitrack.utils.Constants.CLIENT_SECRET
import dev.borisochieng.gitrack.utils.Constants.GITHUB_AUTH_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var isResumedFromDeepLink = false

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory((requireActivity().application as GitTrackApplication).gitTrackRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.bTLogin.setOnClickListener {
            launchWebIntent(generateGitHubURL())
        }

        val accessToken = AccessTokenManager.getAccessToken(requireContext())
        if (accessToken != null)
            findNavController().navigate(R.id.action_loginFragment_to_userRepositoriesFragment)

        Log.d("Auth URL", generateGitHubURL())

        return binding.root
    }

    private fun generateGitHubURL(): String {
        val state = UUID.randomUUID().toString()

        return "${GITHUB_AUTH_URL}?client_id=$CLIENT_ID&state=$state"
    }

    private fun launchWebIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        isResumedFromDeepLink = true

        requireActivity().startActivity(intent.apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        })
    }

    private fun getUriFromDeepLink() = requireActivity().intent?.data


    private fun getCodeFromUri(uri: Uri?): String? = uri?.getQueryParameter("code")


    //APIService call to exchange code for access token
    private fun requestAccessToken(
        code: String?
    ) {

        val authService = RetrofitClient.instance.create(GitHubAuthService::class.java)

        val call: Call<AccessTokenResponse>? = code?.let {
            authService.getAccessToken(CLIENT_ID, CLIENT_SECRET, code = it)
        }

        call?.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    accessToken?.let {
                        AccessTokenManager.saveAccessToken(
                            requireContext(), it
                        )
                        binding.piLogin.show()
                        binding.bTLogin.isEnabled = false
                        findNavController().navigate(R.id.action_loginFragment_to_userRepositoriesFragment)

                    }

                } else {
                    //TODO better error handling
                    Snackbar.make(
                        binding.root, "Something went wrong kindly try again", Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                //TODO better error handling
                Snackbar.make(binding.root, "${t.message}", Snackbar.LENGTH_LONG).show()
                Log.e("APIService Call Error", t.message.toString())
            }

        })
    }

    private fun handleDeepLink() {
        val dataFromDeepLink = getUriFromDeepLink()
        Log.d("Deeplink data", dataFromDeepLink.toString())
        dataFromDeepLink?.let { uri ->
            val code = getCodeFromUri(uri)
            requestAccessToken(code)
            //clear deeplink
            requireActivity().intent?.data = null
        }
    }

    override fun onResume() {
        super.onResume()
        if (isResumedFromDeepLink) {
            handleDeepLink()
            isResumedFromDeepLink = false

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}