package com.boostcamp.dailyfilm.presentation.login

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dailyfilm.R
import com.boostcamp.dailyfilm.presentation.calendar.CalendarActivity
import com.boostcamp.dailyfilm.presentation.util.UiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberSignInResultLauncher(
    requestLogin: (String) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    coroutineScope: CoroutineScope,
): ActivityResultLauncher<Intent> {
    val errorMsg = stringResource(R.string.failed_google_login)
    return rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                requestLogin(account.idToken!!)
            } catch (e: ApiException) {
                coroutineScope.launch {
                    onShowSnackbar(errorMsg)
                }
            }
        }
    }
}

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    coroutineScope: CoroutineScope,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as Activity
    val webClientId = stringResource(R.string.default_web_client_id)
    val options = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }
    val client = remember { GoogleSignIn.getClient(activity, options) }
    val activityResultLauncher = rememberSignInResultLauncher(
        viewModel::requestLogin,
        onShowSnackbar,
        coroutineScope
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (val result = uiState.getContentIfNotHandled()) {
            is UiState.Uninitialized -> {
                autoLogin(activity)
            }

            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                onLogin()
            }

            is UiState.Failure -> {
                isLoading = false
                result.throwable.message?.let {
                    onShowSnackbar(it)
                }
            }

            else -> {}
        }
    }

    LoginScreen(
        modifier = modifier,
        client = client,
        activityResultLauncher = activityResultLauncher
    )
    ProgressLoading(isLoading = isLoading)
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    client: GoogleSignInClient,
    activityResultLauncher: ActivityResultLauncher<Intent>
) {
    val backgroundColor = if (isSystemInDarkTheme()) {
        colorResource(id = R.color.light_blue)
    } else {
        Color.White
    }
    val contentsColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        colorResource(id = R.color.dark_gray)
    }
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Logo()
        LoginButton(activityResultLauncher, client, backgroundColor, contentsColor)
    }
}

@Composable
fun ProgressLoading(isLoading: Boolean) {
    if (isLoading) {
        Surface(
            color = Color.Black.copy(alpha = 0.2f),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))

        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    Modifier.size(150.dp),
                    color = colorResource(id = R.color.dark_gray),
                    strokeWidth = 8.dp
                )
            }
        }
    }
}

@Composable
fun LoginButton(
    activityResultLauncher: ActivityResultLauncher<Intent>,
    client: GoogleSignInClient,
    backgroundColor: Color,
    contentColor: Color
) {

    Button(
        onClick = {
            activityResultLauncher.launch(client.signInIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(start = 48.dp, end = 48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = BorderStroke(2.dp, colorResource(id = R.color.dark_gray))
    ) {
        Image(
            painter = painterResource(id = R.drawable.btn_google_login),
            contentDescription = stringResource(R.string.google_logo)
        )
        Text(text = stringResource(R.string.sign_in_with_google), modifier = Modifier.padding(6.dp))
    }
}

@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.mipmap.app_logo),
        contentDescription = stringResource(R.string.dailyfilm_logo),
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        alignment = Alignment.Center
    )

}

private fun autoLogin(content: Activity) {
    GoogleSignIn.getLastSignedInAccount(content)?.let {
        content.startActivity(Intent(content, CalendarActivity::class.java))
        content.finish()
    }
}