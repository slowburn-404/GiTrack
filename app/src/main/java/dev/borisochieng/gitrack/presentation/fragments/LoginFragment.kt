package dev.borisochieng.gitrack.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentLoginBinding
import dev.borisochieng.gitrack.presentation.viewmodels.LoginViewModel
import dev.borisochieng.gitrack.presentation.viewmodels.LoginViewModelFactory
import dev.borisochieng.gitrack.utils.AccessTokenManager
import dev.borisochieng.gitrack.utils.Constants.CLIENT_ID
import dev.borisochieng.gitrack.utils.Constants.CLIENT_SECRET
import dev.borisochieng.gitrack.utils.Constants.GITHUB_AUTH_URL
import java.util.UUID

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var isResumedFromDeepLink = false

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory((requireActivity().application as GitTrackApplication).authRepository)
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


    private fun getAccessTokenFromViewModel(code: String?) {
        binding.piLogin.show()
        binding.bTLogin.isEnabled = false
        code?.let {
            loginViewModel.getAccessToken(CLIENT_ID, CLIENT_SECRET, it)
        }

        loginViewModel.accessToken.observe(viewLifecycleOwner) { accessTokenResponse ->
            if (accessTokenResponse != null) {
                AccessTokenManager.saveAccessToken(
                    requireContext(),
                    accessTokenResponse.accessToken
                )

                findNavController().navigate(R.id.action_loginFragment_to_userRepositoriesFragment)

                AccessTokenManager.getAccessToken(requireContext())
                    ?.let { Log.d("AccessToken", it) }
            } else {
                Snackbar.make(
                    binding.root,
                    "Something went wrong please try again",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun handleDeepLink() {
        val dataFromDeepLink = getUriFromDeepLink()
        Log.d("Deeplink data", dataFromDeepLink.toString())
        dataFromDeepLink?.let { uri ->
            val code = getCodeFromUri(uri)
            getAccessTokenFromViewModel(code)
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