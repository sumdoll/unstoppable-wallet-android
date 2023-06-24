package io.horizontalsystems.bankwallet.arcticfish.modules.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.user.ui.ForgotPasswordScreen
import io.horizontalsystems.bankwallet.arcticfish.modules.user.ui.LoginScreen
import io.horizontalsystems.bankwallet.arcticfish.modules.user.ui.RegisterScreen
import io.horizontalsystems.bankwallet.core.BaseFragment
import io.horizontalsystems.bankwallet.modules.manageaccounts.ManageAccountsModule
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.core.findNavController

class LoginFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                ComposeAppTheme {
                    val popUpToInclusiveId =
                        arguments?.getInt(ManageAccountsModule.popOffOnSuccessKey, R.id.restoreAccountFragment)
                            ?: R.id.restoreAccountFragment

                    LoginMaincreen(findNavController(), popUpToInclusiveId)
                }
            }
        }
    }
}

@Composable
fun LoginMaincreen(
    navController: NavController,
    popUpToInclusiveId: Int,
    viewModel: UserLoginViewModel = viewModel(factory = UserLoginModule.Factory())
) {
    when (viewModel.choiceOption) {
        UserLoginModule.LoginOption.Login -> {
            LoginScreen(navController, popUpToInclusiveId, viewModel)
        }

        UserLoginModule.LoginOption.Register -> {
            RegisterScreen(navController, popUpToInclusiveId, viewModel)
        }

        UserLoginModule.LoginOption.Forgot -> {
            ForgotPasswordScreen(navController, popUpToInclusiveId, viewModel)
        }
    }
}
