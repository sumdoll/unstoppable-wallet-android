package io.horizontalsystems.bankwallet.arcticfish.modules.user.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.user.UserLoginViewModel
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.TranslatableString
import io.horizontalsystems.bankwallet.ui.compose.components.*
import io.horizontalsystems.bankwallet.ui.compose.observeKeyboardState

@Composable
fun LoginScreen(
    navController: NavController,
    popUpToInclusiveId: Int,
    viewModel: UserLoginViewModel
) {
//    val viewModel = viewModel<RestoreMnemonicViewModel>(factory = RestoreMnemonicModule.Factory())
    val uiState = viewModel.uiState
    val context = LocalContext.current

    var textState by rememberSaveable("", stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var isMnemonicPhraseInputFocused by remember { mutableStateOf(false) }
    val keyboardState by observeKeyboardState()

    val borderColor = if (uiState.error != null) {
        ComposeAppTheme.colors.red50
    } else {
        ComposeAppTheme.colors.steel20
    }

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = TranslatableString.ResString(R.string.Restore_Advanced_Title),
            navigationIcon = {
                HsBackButton(onClick = navController::popBackStack)
            }
        )
        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(12.dp))
                NormalImage(
                    R.drawable.ic_company_logo,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .size(100.dp),
                )
                Spacer(Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(ComposeAppTheme.colors.lawrence),
                ) {

                    FormsInput(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                        initial = uiState.loginName,
                        pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                        hint = stringResource(R.string.Login_Hint_Account),
                        onValueChange = viewModel::onNameChange
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    FormsInput(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                        initial = uiState.loginName,
                        pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                        hint = stringResource(R.string.Login_Hint_password),
                        onValueChange = viewModel::onPasswordChange
                    )

                }

                Spacer(Modifier.height(10.dp))

                ButtonSecondaryTransparent(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .height(28.dp),
                    title = stringResource(R.string.Login_ForgotPassword),
                    onClick = {
                        //TODO: 忘记密码
                    }
                )

                Spacer(Modifier.height(20.dp))
                // 付款和已收款确认按钮
                ButtonPrimaryYellow(
                    modifier = Modifier.weight(0.4f)
                        .width(120.dp),
                    title = stringResource(R.string.Login_Login),
                    onClick = {
                        //TODO: 登录操作
                    },
                    enabled = true
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}


@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    popUpToInclusiveId: Int,
    viewModel: UserLoginViewModel
) {
//    val viewModel = viewModel<RestoreMnemonicViewModel>(factory = RestoreMnemonicModule.Factory())
    val uiState = viewModel.uiState
    val context = LocalContext.current

    var textState by rememberSaveable("", stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var isMnemonicPhraseInputFocused by remember { mutableStateOf(false) }
    val keyboardState by observeKeyboardState()

//    val borderColor = if (uiState.error != null) {
//        ComposeAppTheme.colors.red50
//    } else {
//        ComposeAppTheme.colors.steel20
//    }

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = TranslatableString.ResString(R.string.Restore_Advanced_Title),
            navigationIcon = {
                HsBackButton(onClick = navController::popBackStack)
            }
        )
        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(12.dp))
                HeaderText(stringResource(R.string.Login_FindPassword))
//                NormalImage(
//                    R.drawable.ic_company_logo,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                        .size(100.dp),
//                )
                Spacer(Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
//                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(ComposeAppTheme.colors.lawrence),
                ) {

                    FormsInput(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                        initial = uiState.loginName,
                        pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                        hint = stringResource(R.string.Login_Email_Hint),
                        onValueChange = viewModel::onNameChange
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    FormsInput(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                        initial = uiState.loginName,
                        pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                        hint = stringResource(R.string.Login_AuthCode_Hint),
                        onValueChange = viewModel::onPasswordChange
                    )

                    ButtonSecondaryTransparent(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .height(28.dp),
                        title = stringResource(R.string.Login_GetAuthCode),
                        onClick = {
                            //TODO: 忘记密码
                        }
                    )
                }

                Spacer(Modifier.height(10.dp))

                FormsInput(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(horizontal = 16.dp),
                    initial = uiState.loginName,
                    pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                    hint = stringResource(R.string.Login_NewPass_Hint),
                    onValueChange = viewModel::onNameChange
                )

                Spacer(Modifier.height(20.dp))
                // 提交按钮
                ButtonPrimaryYellow(
                    modifier = Modifier.weight(0.4f)
                        .width(120.dp),
                    title = stringResource(R.string.Login_Submit),
                    onClick = {
                        //TODO: 提交操作
                    },
                    enabled = true
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}


@Composable
fun RegisterScreen(
    navController: NavController,
    popUpToInclusiveId: Int,
    viewModel: UserLoginViewModel
) {
//    val viewModel = viewModel<RestoreMnemonicViewModel>(factory = RestoreMnemonicModule.Factory())
    val uiState = viewModel.uiState
    val context = LocalContext.current

    var textState by rememberSaveable("", stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var isMnemonicPhraseInputFocused by remember { mutableStateOf(false) }
    val keyboardState by observeKeyboardState()

//    val borderColor = if (uiState.error != null) {
//        ComposeAppTheme.colors.red50
//    } else {
//        ComposeAppTheme.colors.steel20
//    }

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = TranslatableString.ResString(R.string.Restore_Advanced_Title),
            navigationIcon = {
                HsBackButton(onClick = navController::popBackStack)
            }
        )
        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(12.dp))

                NormalImage(
                    R.drawable.ic_company_logo,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .size(100.dp),
                )
                Spacer(Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
//                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(ComposeAppTheme.colors.lawrence),
                ) {

                    FormsInput(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                        initial = uiState.loginName,
                        pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                        hint = stringResource(R.string.Login_Email_Hint),
                        onValueChange = viewModel::onNameChange
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    FormsInput(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                        initial = uiState.loginName,
                        pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                        hint = stringResource(R.string.Login_AuthCode_Hint),
                        onValueChange = viewModel::onPasswordChange
                    )

                    ButtonSecondaryTransparent(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .height(28.dp),
                        title = stringResource(R.string.Login_GetAuthCode),
                        onClick = {
                            //TODO: 忘记密码
                        }
                    )
                }

                Spacer(Modifier.height(10.dp))

                FormsInput(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(horizontal = 16.dp),
                    initial = uiState.loginName,
                    pasteEnabled = false,
//                        state = uiState.error?.let { DataState.Error(it) },
                    hint = stringResource(R.string.Login_NewPass_Hint),
                    onValueChange = viewModel::onNameChange
                )

                Spacer(Modifier.height(20.dp))
                // 提交按钮
                ButtonPrimaryYellow(
                    modifier = Modifier.weight(0.4f)
                        .width(120.dp),
                    title = stringResource(R.string.Login_Submit),
                    onClick = {
                        //TODO: 提交操作
                    },
                    enabled = true
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
