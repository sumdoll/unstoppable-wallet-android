package io.horizontalsystems.bankwallet.arcticfish.modules.pond.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.appeal.AppealStage
import io.horizontalsystems.bankwallet.arcticfish.modules.pond.PondStage
import io.horizontalsystems.bankwallet.core.displayNameStringRes
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.core.slideFromRight
import io.horizontalsystems.bankwallet.modules.createaccount.MnemonicLanguageCell
import io.horizontalsystems.bankwallet.modules.createaccount.PassphraseCell
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoremenu.RestoreMenuViewModel
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoremnemonic.RestoreMnemonicModule
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoremnemonic.RestoreMnemonicViewModel
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoreprivatekey.RestorePrivateKeyModule
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoreprivatekey.RestorePrivateKeyViewModel
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.TranslatableString
import io.horizontalsystems.bankwallet.ui.compose.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SubmitPond(
    navController: NavController
) {
//    val viewModel = viewModel<RestorePrivateKeyViewModel>(factory = RestorePrivateKeyModule.Factory())
    val viewModel = viewModel<RestoreMnemonicViewModel>(factory = RestoreMnemonicModule.Factory())
    val uiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        backgroundColor = ComposeAppTheme.colors.tyler,
        topBar = {
            AppBar(
                title = TranslatableString.ResString(R.string.Pond_Title),
                navigationIcon = {
                    HsBackButton(onClick = navController::popBackStack)
                }
            )
        }
    ) {
        val stage : PondStage = PondStage.Submitted
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            stageRow(stage)
            VSpacer(10.dp)

            HeaderText(stringResource(id = R.string.Pond_Terms_Title))
            CellUniversalLawrenceSection(
                listOf(
                    {
                        val icon : Int = R.drawable.icon_20_check_1
                        val dec : Int = R.string.Pond_Terms_Auth
                        termsItemCell(
                            icon, dec, todo = true,
                            onClicked = {
//                                navController.slideFromRight(
//                                    R.id.restoreSelectCoinsFragment,
//                                    bundleOf(
//                                        RestoreBlockchainsFragment.ACCOUNT_NAME_KEY to viewModel.accountName,
//                                        RestoreBlockchainsFragment.ACCOUNT_TYPE_KEY to accountType,
//                                        ManageAccountsModule.popOffOnSuccessKey to popUpToInclusiveId,
//                                    )
//                                )
                            }
                        )
                    },
                    {
                        val icon : Int = R.drawable.icon_20_user_plus
                        val dec : Int = R.string.Pond_Terms_Video
                        termsItemCell(
                            icon, dec, todo = true,
                            onClicked = {
//                                navController.slideFromRight(
//                                    R.id.restoreSelectCoinsFragment,
//                                    bundleOf(
//                                        RestoreBlockchainsFragment.ACCOUNT_NAME_KEY to viewModel.accountName,
//                                        RestoreBlockchainsFragment.ACCOUNT_TYPE_KEY to accountType,
//                                        ManageAccountsModule.popOffOnSuccessKey to popUpToInclusiveId,
//                                    )
//                                )
                            }
                        )
                    },
                    {
                        val icon : Int = R.drawable.icon_24_lock
                        val dec : Int = R.string.Pond_Terms_Deposit
                        termsItemCell(
                            icon, dec, todo = true,
                            onClicked = {
//                                navController.slideFromRight(
//                                    R.id.restoreSelectCoinsFragment,
//                                    bundleOf(
//                                        RestoreBlockchainsFragment.ACCOUNT_NAME_KEY to viewModel.accountName,
//                                        RestoreBlockchainsFragment.ACCOUNT_TYPE_KEY to accountType,
//                                        ManageAccountsModule.popOffOnSuccessKey to popUpToInclusiveId,
//                                    )
//                                )
                            }
                        )
                    }
                )
            )

            Spacer(Modifier.height(10.dp))
            bottomRow()
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun VideoAuthReqest(
    navController: NavController,
) {
    val viewModel = viewModel<RestorePrivateKeyViewModel>(factory = RestorePrivateKeyModule.Factory())

    Scaffold(
        backgroundColor = ComposeAppTheme.colors.tyler,
        topBar = {
            AppBar(
                title = TranslatableString.ResString(R.string.Pond_Video_Title),
                navigationIcon = {
                    HsBackButton(onClick = navController::popBackStack)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            headline1_jacob(text = stringResource(R.string.Pond_Note_VideoTip))
            VSpacer(10.dp)


            HeaderText(stringResource(R.string.Pond_Video_Head))
//        RestoreByMenu(restoreMenuViewModel)

            Spacer(Modifier.height(10.dp))
            TextImportantLight(
                modifier = Modifier.padding(12.dp),
                text = stringResource(R.string.Pond_Video_Dec),
            )

            Spacer(Modifier.height(10.dp))
            NormalImage(
                img = R.drawable.ic_qr_scan_24px,
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
            )

            Spacer(Modifier.height(10.dp))
            HeaderText(stringResource(id = R.string.Pond_Video_Upload))
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun stageRow(
    stage: PondStage
) {
    var stageInt: Int = R.string.Pond_Note_NoSubmit

    if (stage == PondStage.Submitted) {
        stageInt = R.string.Pond_Note_Submitted
    } else if (stage == PondStage.Passed) {
        stageInt = R.string.Pond_Note_Passed
    } else if (stage == PondStage.Resubmit) {
        stageInt = R.string.Pond_Note_Resubmit
    }
    VSpacer(10.dp)
    TextImportantLight(
        modifier = Modifier.padding(horizontal = 6.dp),
        text = stringResource(stageInt),
    )
    VSpacer(10.dp)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ColumnScope.TypeSection(
    navController: NavController,
    viewModel: RestoreMnemonicViewModel,
    uiState: RestoreMnemonicModule.UiState,
    coroutineScope: CoroutineScope,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSelectorDialog by remember { mutableStateOf(false) }
    var hidePassphrase by remember { mutableStateOf(true) }

    if (showSelectorDialog) {
        SelectorDialogCompose(
            title = stringResource(R.string.CreateWallet_Wordlist),
            items = viewModel.mnemonicLanguages.map {
                TabItem(
                    stringResource(it.displayNameStringRes),
                    it == uiState.language,
                    it
                )
            },
            onDismissRequest = {
                coroutineScope.launch {
                    showSelectorDialog = false
                    delay(300)
                    keyboardController?.show()
                }
            },
            onSelectItem = {
                viewModel.setMnemonicLanguage(it)
            }
        )
    }
}

@Composable
fun termsItemCell(
    iconRes: Int,
    textRes: Int,
    todo: Boolean,
    onClicked: () -> Unit
) {
    RowUniversal(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalPadding = 0.dp,
//        onClick = { onCheckedChange(!todo) },
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = ComposeAppTheme.colors.grey
        )
        body_leah(
            text = stringResource(textRes),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp)
        )

        if (todo) {
            ButtonPrimaryYellow(
                modifier = Modifier.weight(0.4f)
                    .width(120.dp),
                title = stringResource(R.string.Pond_Button_Go),
                onClick = onClicked,
                enabled = true
            )
        } else {
            ButtonPrimaryYellow(
                modifier = Modifier.weight(0.4f)
                    .width(120.dp),
                title = stringResource(R.string.Pond_Button_Finish),
                onClick = {},
                enabled = false
            )
        }
    }
}

@Composable
fun SubmitContents(
    stage: AppealStage,
    limit: Int = 3,
    result: String? = null
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(10.dp))

        HeaderText(stringResource(id = R.string.Appeal_Type_Title))
//        RestoreByMenu(restoreMenuViewModel)

        Spacer(Modifier.height(10.dp))
        HeaderText(stringResource(id = R.string.Appeal_Des_Title))
        FormsInputMultiline(
            modifier = Modifier.padding(horizontal = 16.dp),
            hint = stringResource(id = R.string.Appeal_Des_Tip),
//            state = viewModel.inputState,
            qrScannerEnabled = false,
        ) {
//            viewModel.onEnterPrivateKey(it)
        }

        Spacer(Modifier.height(10.dp))
        HeaderText(stringResource(id = R.string.Appeal_Upload_Title))


        Spacer(Modifier.height(10.dp))
        HeaderText(stringResource(id = R.string.Appeal_Contact_Title))
        FormsInput(
            modifier = Modifier.padding(horizontal = 16.dp),
//            initial = viewModel.accountName,
            pasteEnabled = false,
            hint = "",//viewModel.defaultName,
            onValueChange = {}//viewModel::onEnterName
        )

        Spacer(Modifier.height(10.dp))
        bottomRow()
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun bottomRow(hasSubmit: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HSpacer(20.dp)
        // 提交按钮
        if (hasSubmit) {
            ButtonPrimaryYellow(
                modifier = Modifier.weight(0.4f)
                    .width(120.dp),
                title = stringResource(R.string.Pond_Button_Submit),
                onClick = {},
                enabled = false
            )
        } else {
            ButtonPrimaryYellow(
                modifier = Modifier.weight(0.4f)
                    .width(120.dp),
                title = stringResource(R.string.Pond_Button_Submit),
                onClick = {
                    //TODO: 确认操作
                },
                enabled = true
            )
        }
        HSpacer(20.dp)
    }
}
