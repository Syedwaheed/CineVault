package com.edu.auth.presentation.login

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.auth.presentation.R
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvBorder
import com.edu.core.presentation.designsystem.CvGold
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily
import com.edu.core.presentation.ui.ObserveAsEvent
import com.edu.core.presentation.ui.UiText
import org.koin.androidx.compose.koinViewModel

// ─── Stateful screen ──────────────────────────────────────────────────────────

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            is LoginEffect.OpenBrowser -> {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                )
            }
            LoginEffect.NavigateToHome -> onNavigateToHome()
        }
    }

    LoginContent(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

// ─── Stateless content ────────────────────────────────────────────────────────

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF1A0D05), CvBg),
                        center = Offset(size.width / 2f, size.height * 0.4f),
                        radius = size.maxDimension * 0.85f,
                    ),
                )
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = CineVaultSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Branding ──────────────────────────────────────────────────────
            CineVaultLogoIcon(size = 72.dp)

            Spacer(modifier = Modifier.height(CineVaultSpacing.xl))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Cine",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = CvText,
                        fontFamily = PlayfairDisplayFontFamily,
                    ),
                )
                Text(
                    text = "Vault",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = CvAmber,
                        fontFamily = PlayfairDisplayFontFamily,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

            Text(
                text = stringResource(R.string.login_tagline),
                style = MaterialTheme.typography.labelMedium.copy(color = CvTextMute),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(1.5f))

            // ── State-dependent actions ───────────────────────────────────────
            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(tween(CineVaultAnimation.DURATION_MEDIUM)) togetherWith
                        fadeOut(tween(CineVaultAnimation.DURATION_MEDIUM))
                },
                label = "login_actions",
                modifier = Modifier.fillMaxWidth(),
            ) { state: LoginUiState ->
                when (state) {
                    LoginUiState.Idle -> IdleContent(onAction = onAction)
                    LoginUiState.FetchingToken -> LoadingContent(
                        text = stringResource(R.string.login_connecting),
                    )
                    LoginUiState.WaitingForApproval -> ApprovalContent(onAction = onAction)
                    LoginUiState.LoggingIn -> LoadingContent(
                        text = stringResource(R.string.login_signing_in),
                    )
                    is LoginUiState.Error -> ErrorContent(
                        message = state.message,
                        onAction = onAction,
                    )
                }
            }

            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

            Text(
                text = stringResource(R.string.login_powered_by_tmdb),
                style = MaterialTheme.typography.labelMedium.copy(color = CvTextMute),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
        }
    }
}

// ─── State sub-composables ────────────────────────────────────────────────────

@Composable
private fun IdleContent(onAction: (LoginAction) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        AuthPrimaryButton(
            text = stringResource(R.string.login_continue_with_tmdb),
            onClick = { onAction(LoginAction.ContinueWithTmdb) },
        )
    }
}

@Composable
private fun LoadingContent(text: String) {
    AuthPrimaryButton(
        text = text,
        onClick = {},
        loading = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ApprovalContent(onAction: (LoginAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CvSurface, RoundedCornerShape(CineVaultRadius.md))
                .border(1.dp, CvBorder, RoundedCornerShape(CineVaultRadius.md))
                .padding(CineVaultSpacing.lg),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm)) {
                Text(
                    text = stringResource(R.string.login_approval_title),
                    style = MaterialTheme.typography.titleLarge.copy(color = CvText),
                )
                Text(
                    text = stringResource(R.string.login_approval_body),
                    style = MaterialTheme.typography.bodyMedium.copy(color = CvTextDim),
                )
            }
        }

        Spacer(modifier = Modifier.height(CineVaultSpacing.xs))

        AuthPrimaryButton(
            text = stringResource(R.string.login_i_approved),
            onClick = { onAction(LoginAction.ApprovalConfirmed) },
        )

        TextButton(
            onClick = { onAction(LoginAction.Cancel) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.login_cancel),
                style = MaterialTheme.typography.labelLarge.copy(color = CvTextDim),
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: UiText,
    onAction: (LoginAction) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.errorContainer,
                    RoundedCornerShape(CineVaultRadius.md),
                )
                .padding(CineVaultSpacing.lg),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm)) {
                Text(
                    text = stringResource(R.string.login_error_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                )
                Text(
                    text = message.asString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    ),
                )
            }
        }

        AuthPrimaryButton(
            text = stringResource(R.string.login_try_again),
            onClick = { onAction(LoginAction.RetryClicked) },
        )
    }
}

// ─── Shared UI atoms ──────────────────────────────────────────────────────────

@Composable
private fun AuthPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = !loading,
        shape = RoundedCornerShape(CineVaultRadius.pill),
        colors = ButtonDefaults.buttonColors(
            containerColor = CvAmber,
            contentColor = CvBg,
            disabledContainerColor = CvAmber.copy(alpha = 0.7f),
            disabledContentColor = CvBg,
        ),
        modifier = modifier.fillMaxWidth().height(52.dp),
    ) {
        AnimatedContent(
            targetState = loading,
            transitionSpec = {
                fadeIn(tween(CineVaultAnimation.DURATION_SHORT)) togetherWith
                    fadeOut(tween(CineVaultAnimation.DURATION_SHORT))
            },
            label = "button_content",
        ) { isLoading: Boolean ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = CvBg,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        }
    }
}

// ─── CineVault logo icon (drawn via Canvas to match /design/screens.jsx) ──────

@Composable
private fun CineVaultLogoIcon(modifier: Modifier = Modifier, size: Dp = 72.dp) {
    Canvas(modifier = modifier.size(size)) {
        val s = this.size.width / 92f

        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(CvAmber, CvGold),
                start = Offset.Zero,
                end = Offset(this.size.width, this.size.height),
            ),
            cornerRadius = CornerRadius(20f * s),
        )

        val ink = Color(0xFF0A0A0A)
        val sw = 2.4f * s

        // Left and right vertical frame lines
        drawLine(ink, Offset(28f * s, 22f * s), Offset(28f * s, 70f * s), sw, cap = StrokeCap.Round)
        drawLine(ink, Offset(64f * s, 22f * s), Offset(64f * s, 70f * s), sw, cap = StrokeCap.Round)

        // Horizontal frame lines
        listOf(32f, 46f, 60f).forEach { y ->
            drawLine(ink, Offset(28f * s, y * s), Offset(64f * s, y * s), sw, cap = StrokeCap.Round)
        }

        // Lens circle
        drawCircle(ink, 6.5f * s, Offset(46f * s, 40f * s))
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Idle", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun LoginContentIdlePreview() {
    CineVaultTheme {
        LoginContent(
            uiState = LoginUiState.Idle,
            onAction = {},
        )
    }
}

@Preview(name = "WaitingForApproval", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun LoginContentApprovalPreview() {
    CineVaultTheme {
        LoginContent(
            uiState = LoginUiState.WaitingForApproval,
            onAction = {},
        )
    }
}

@Preview(name = "Loading", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun LoginContentLoadingPreview() {
    CineVaultTheme {
        LoginContent(
            uiState = LoginUiState.FetchingToken,
            onAction = {},
        )
    }
}

@Preview(name = "Error", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun LoginContentErrorPreview() {
    CineVaultTheme {
        LoginContent(
            uiState = LoginUiState.Error(UiText.DynamicString("No internet connection")),
            onAction = {},
        )
    }
}
