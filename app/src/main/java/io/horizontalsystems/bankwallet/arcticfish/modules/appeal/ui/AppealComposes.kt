package io.horizontalsystems.bankwallet.arcticfish.modules.appeal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.appeal.AppealStage
import io.horizontalsystems.bankwallet.core.displayNameStringRes
import io.horizontalsystems.bankwallet.core.providers.Translator
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
fun SubmitAppeal(
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
                title = TranslatableString.ResString(R.string.Appeal_Title),
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
            TextImportantWarning(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = stringResource(R.string.Appeal_Tip_SubmitNotice),
            )
            VSpacer(10.dp)

            HeaderText(stringResource(id = R.string.Appeal_Type_Title))
//        RestoreByMenu(restoreMenuViewModel)
            TypeSection(
                navController,
                viewModel,
                uiState,
                coroutineScope
            )

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
}

@Composable
fun ReviewAppeal(
    navController: NavController,
    popUpToInclusiveId: Int,
    restoreMenuViewModel: RestoreMenuViewModel,
) {
    val viewModel = viewModel<RestorePrivateKeyViewModel>(factory = RestorePrivateKeyModule.Factory())

    Scaffold(
        backgroundColor = ComposeAppTheme.colors.tyler,
        topBar = {
            AppBar(
                title = TranslatableString.ResString(R.string.Appeal_Title),
                navigationIcon = {
                    HsBackButton(onClick = navController::popBackStack)
                },
                menuItems = listOf(
                    MenuItem(
                        title = TranslatableString.ResString(R.string.Appeal_Cancel),
                        onClick = {
                            // TODO: 取消确认
//                            viewModel.resolveAccountType()?.let { accountType ->
//                                navController.slideFromRight(
//                                    R.id.restoreSelectCoinsFragment,
//                                    bundleOf(
//                                        RestoreBlockchainsFragment.ACCOUNT_NAME_KEY to viewModel.accountName,
//                                        RestoreBlockchainsFragment.ACCOUNT_TYPE_KEY to accountType,
//                                        ManageAccountsModule.popOffOnSuccessKey to popUpToInclusiveId,
//                                    )
//                                )
//                            }
                        }
                    )
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            stageRow(stage = AppealStage.Submit)
            VSpacer(10.dp)

            noteRow(stage = AppealStage.Submit, limit = 3)
            VSpacer(10.dp)

            HeaderText(stringResource(id = R.string.Appeal_Type_Title))
//        RestoreByMenu(restoreMenuViewModel)

            Spacer(Modifier.height(10.dp))
            HeaderText(stringResource(id = R.string.Appeal_Des_Title))
            FormsInputMultiline(
                modifier = Modifier.padding(horizontal = 16.dp),
                hint = stringResource(id = R.string.Appeal_Des_Tip),
                enabled = false,
//            state = viewModel.inputState,
                qrScannerEnabled = false,
                pasteEnabled = false
            ) {
//            viewModel.onEnterPrivateKey(it)
            }
//            body_leah(
//                modifier = Modifier.padding(horizontal = 16.dp),
//                text = "测试描述"
//            )

            Spacer(Modifier.height(10.dp))
            HeaderText(stringResource(id = R.string.Appeal_Upload_Title))
            // TODO: show image


            Spacer(Modifier.height(10.dp))
            HeaderText(stringResource(id = R.string.Appeal_Contact_Title))
            FormsInput(
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = false,
//            initial = viewModel.accountName,
                pasteEnabled = false,
                hint = "",//viewModel.defaultName,
                onValueChange = {}//viewModel::onEnterName
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun stageRow(
    stage: AppealStage
) {
    var stageStr: String? = null

    if (stage == AppealStage.Submit) {
        stageStr = stringResource(R.string.Appeal_Stage_Submit)
    } else if (stage == AppealStage.Plea) {
        stageStr = stringResource(R.string.Appeal_Stage_Plea)
    } else if (stage == AppealStage.Scrutiny) {
        stageStr = stringResource(R.string.Appeal_Stage_Scrutiny)
    } else if (stage == AppealStage.Done) {
        stageStr = stringResource(R.string.Appeal_Stage_Done)
    }
    stageStr?.let {
        LightRowRed {
            headline1_jacob(
                text = stringResource(R.string.Appeal_Stage_Plea),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

//                R.drawable.ic_arrow_right
            NormalImage(
                img = R.drawable.ic_arrow_right,
                modifier = Modifier
                    .padding(start = 3.dp, end = 3.dp)
                    .size(12.dp)
            )

            subhead2_leah(
                text = stringResource(R.string.Appeal_Stage_Scrutiny),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            NormalImage(
                img = R.drawable.ic_arrow_right,
                modifier = Modifier
                    .padding(start = 3.dp, end = 3.dp)
                    .size(12.dp)
            )

            subhead2_leah(
                text = stringResource(R.string.Appeal_Stage_Done),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
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
        noteRow(stage, limit, result)
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
fun noteRow(
    stage: AppealStage,
    limit: Int = 3,
    result: String? = null
) {
    var note : String = stringResource(R.string.Appeal_Tip_SubmitNotice)
    if (stage == AppealStage.Submit)
        note = stringResource(R.string.Appeal_Tip_SubmitNotice)
    else if (stage == AppealStage.Plea)
        note = Translator.getString(R.string.Appeal_Tip_PleaNote, limit)
    else if (stage == AppealStage.Scrutiny)
        note = Translator.getString(R.string.Appeal_Tip_PleaScrutiny, limit)
    else if (stage == AppealStage.Done)
        note = Translator.getString(R.string.Appeal_Tip_Done, (result?: ""))

    TextImportantWarning(
        modifier = Modifier.padding(horizontal = 6.dp),
        text = note,
    )
}

@Composable
fun bottomRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HSpacer(20.dp)
        // 取消和提交按钮
        ButtonPrimaryYellow(
            modifier = Modifier.weight(0.4f)
                .width(120.dp),
            title = stringResource(R.string.Appeal_Button_Cancel),
            onClick = {
                //TODO: 取消操作
            },
            enabled = true
        )
        HSpacer(20.dp)

        ButtonPrimaryYellow(
            modifier = Modifier.weight(0.4f)
                .width(120.dp),
            title = stringResource(R.string.Appeal_Button_Submit),
            onClick = {
                //TODO: 确认操作
            },
            enabled = true
        )
        HSpacer(20.dp)
    }
}
