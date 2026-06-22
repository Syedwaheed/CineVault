package com.edu.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.edu.core.domain.auth.Account
import com.edu.core.domain.auth.UserStats
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvBorder
import com.edu.core.presentation.designsystem.CvDanger
import com.edu.core.presentation.designsystem.CvGold
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvSurfaceElev
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.JetBrainsMonoFontFamily
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily
import com.edu.core.presentation.designsystem.components.shimmerEffect
import com.edu.core.presentation.ui.ObserveAsEvent
import com.edu.core.presentation.ui.UiText
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    imageBaseUrl: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            ProfileEffect.NavigateToAuth -> onLogout()
            is ProfileEffect.ShowMessage -> scope.launch {
                snackbarHostState.showSnackbar(effect.message.asString(context))
            }
        }
    }

    ProfileContent(
        uiState = uiState,
        imageBaseUrl = imageBaseUrl,
        snackbarHostState = snackbarHostState,
        contentPadding = contentPadding,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileContent(
    uiState: ProfileUiState,
    imageBaseUrl: String,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign out?") },
            text = { Text("You'll need to sign in again to access your watchlist and profile.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onAction(ProfileAction.ConfirmLogout)
                    },
                    modifier = Modifier.testTag("confirm_logout_button"),
                ) {
                    Text("Sign out", color = CvDanger)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = CvSurface,
            titleContentColor = CvText,
            textContentColor = CvTextDim,
        )
    }

    Scaffold(
        containerColor = CvBg,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = CvText,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CvBg,
                    scrolledContainerColor = CvBg,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { scaffoldPadding ->
        val bottomPad = maxOf(
            scaffoldPadding.calculateBottomPadding(),
            contentPadding.calculateBottomPadding(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = scaffoldPadding.calculateTopPadding(), bottom = bottomPad),
        ) {
            when (uiState) {
                ProfileUiState.Loading -> ProfileLoadingContent()
                is ProfileUiState.Success -> ProfileSuccessContent(
                    account = uiState.account,
                    stats = uiState.stats,
                    imageBaseUrl = imageBaseUrl,
                    onAction = onAction,
                    onLogoutClick = { showLogoutDialog = true },
                )
                is ProfileUiState.Error -> ProfileErrorContent(
                    message = uiState.message,
                    onRetry = { onAction(ProfileAction.Retry) },
                )
            }
        }
    }
}

@Composable
private fun ProfileLoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = CineVaultSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

        // Avatar shimmer
        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .shimmerEffect(cornerRadius = 46.dp),
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.md))

        // Name shimmer
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(24.dp)
                .shimmerEffect(cornerRadius = CineVaultRadius.sm),
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.xs))

        // Username shimmer
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(14.dp)
                .shimmerEffect(cornerRadius = CineVaultRadius.xs),
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

        // Stats shimmer row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .shimmerEffect(cornerRadius = CineVaultRadius.lg),
                )
            }
        }

        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

        // Settings rows shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .shimmerEffect(cornerRadius = CineVaultRadius.lg),
        )
    }
}

@Composable
private fun ProfileSuccessContent(
    account: Account,
    stats: UserStats,
    imageBaseUrl: String,
    onAction: (ProfileAction) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

        // Avatar
        ProfileAvatar(
            account = account,
            imageBaseUrl = imageBaseUrl,
            onClick = { onAction(ProfileAction.AvatarClick) },
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.md))

        // Display name
        Text(
            text = account.name.ifBlank { account.username },
            fontFamily = PlayfairDisplayFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = CvText,
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.xs))

        // Username meta
        Text(
            text = "@${account.username}",
            fontFamily = JetBrainsMonoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            letterSpacing = 0.3.sp,
            color = CvTextDim,
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            StatCell(value = stats.watchlistCount.toString(), label = "Watchlist", modifier = Modifier.weight(1f))
            StatCell(value = stats.moviesWatched.toString(), label = "Watched", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

        // Settings
        SettingsSection(
            onAction = onAction,
            onLogoutClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CineVaultSpacing.lg),
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

        Text(
            text = "CINEVAULT 2.1.0",
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 10.sp,
            color = CvTextMute,
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
    }
}

@Composable
private fun ProfileAvatar(
    account: Account,
    imageBaseUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gradientBrush = Brush.linearGradient(listOf(CvAmber, CvGold))
    Box(
        modifier = modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(gradientBrush)
            .padding(3.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (account.avatarPath != null) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${imageBaseUrl}w185${account.avatarPath}")
                    .crossfade(true)
                    .build(),
                contentDescription = account.name,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .shimmerEffect(cornerRadius = 999.dp),
                    )
                },
                error = {
                    AvatarInitials(name = account.name.ifBlank { account.username })
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
            )
        } else {
            AvatarInitials(name = account.name.ifBlank { account.username })
        }
    }
}

@Composable
private fun AvatarInitials(name: String, modifier: Modifier = Modifier) {
    val initials = name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
        .ifBlank { "?" }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(CvSurface),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            fontFamily = PlayfairDisplayFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            color = CvAmber,
        )
    }
}

@Composable
private fun StatCell(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(CvSurface, RoundedCornerShape(CineVaultRadius.lg))
            .border(1.dp, CvBorder, RoundedCornerShape(CineVaultRadius.lg))
            .padding(vertical = CineVaultSpacing.md, horizontal = CineVaultSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = value,
            fontFamily = PlayfairDisplayFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = CvAmber,
        )
        Spacer(modifier = Modifier.height(CineVaultSpacing.xs))
        Text(
            text = label,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 11.sp,
            color = CvTextDim,
        )
    }
}

private data class SettingRow(
    val type: SettingType?,
    val label: String,
    val meta: String,
    val isDanger: Boolean = false,
)

private val settingRows = listOf(
    SettingRow(SettingType.NOTIFICATIONS, "Notifications", "On"),
    SettingRow(SettingType.THEME, "Theme", "Cinematic Dark"),
    SettingRow(SettingType.LANGUAGE, "Language", "English (US)"),
    SettingRow(SettingType.ABOUT, "About CineVault", ""),
)

@Composable
private fun SettingsSection(
    onAction: (ProfileAction) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "SETTINGS",
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 10.sp,
            color = CvTextMute,
            modifier = Modifier.padding(start = CineVaultSpacing.xs, bottom = CineVaultSpacing.sm),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CvSurface, RoundedCornerShape(CineVaultRadius.lg))
                .border(1.dp, CvBorder, RoundedCornerShape(CineVaultRadius.lg))
                .clip(RoundedCornerShape(CineVaultRadius.lg)),
        ) {
            settingRows.forEachIndexed { index, row ->
                SettingItem(
                    label = row.label,
                    meta = row.meta,
                    isDanger = false,
                    showDivider = true,
                    onClick = { row.type?.let { onAction(ProfileAction.SettingClick(it)) } },
                )
            }

            // Sign out — danger row
            SettingItem(
                label = "Sign out",
                meta = "",
                isDanger = true,
                showDivider = false,
                onClick = onLogoutClick,
            )
        }
    }
}

@Composable
private fun SettingItem(
    label: String,
    meta: String,
    isDanger: Boolean,
    showDivider: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = CineVaultSpacing.lg, vertical = CineVaultSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (isDanger) CvDanger.copy(alpha = 0.12f) else CvSurfaceElev,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isDanger) "→" else "·",
                    color = if (isDanger) CvDanger else CvText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.width(CineVaultSpacing.md))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isDanger) CvDanger else CvText,
                modifier = Modifier.weight(1f),
            )

            if (meta.isNotBlank()) {
                Text(
                    text = meta,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp,
                    color = CvTextDim,
                )
                Spacer(modifier = Modifier.width(CineVaultSpacing.sm))
            }

            if (!isDanger) {
                Text(text = "›", color = CvTextMute, fontSize = 18.sp)
            }
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(CvBorder),
            )
        }
    }
}

@Composable
private fun ProfileErrorContent(
    message: UiText,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(CineVaultSpacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message.asString(),
            style = MaterialTheme.typography.bodyLarge,
            color = CvTextDim,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(CineVaultSpacing.xl))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = CvAmber, contentColor = CvBg),
            shape = RoundedCornerShape(CineVaultRadius.pill),
        ) {
            Text("Retry")
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val previewAccount = Account(
    id = 123,
    name = "Elena Hartwell",
    username = "elena",
    includeAdult = false,
    language = "en",
    country = "US",
    avatarPath = null,
)

private val previewStats = UserStats(watchlistCount = 18, moviesWatched = 247)

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun ProfileLoadingPreview() {
    CineVaultTheme {
        ProfileContent(
            uiState = ProfileUiState.Loading,
            imageBaseUrl = "https://image.tmdb.org/t/p/",
            snackbarHostState = remember { SnackbarHostState() },
            contentPadding = PaddingValues(),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun ProfileSuccessPreview() {
    CineVaultTheme {
        ProfileContent(
            uiState = ProfileUiState.Success(account = previewAccount, stats = previewStats),
            imageBaseUrl = "https://image.tmdb.org/t/p/",
            snackbarHostState = remember { SnackbarHostState() },
            contentPadding = PaddingValues(),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun ProfileErrorPreview() {
    CineVaultTheme {
        ProfileContent(
            uiState = ProfileUiState.Error(UiText.DynamicString("Failed to load profile")),
            imageBaseUrl = "https://image.tmdb.org/t/p/",
            snackbarHostState = remember { SnackbarHostState() },
            contentPadding = PaddingValues(),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun StatCellPreview() {
    CineVaultTheme {
        Row(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            StatCell(value = "18", label = "Watchlist", modifier = Modifier.weight(1f))
            StatCell(value = "247", label = "Watched", modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun AvatarInitialsPreview() {
    CineVaultTheme {
        Box(modifier = Modifier.size(96.dp)) {
            AvatarInitials(name = "Elena Hartwell")
        }
    }
}
