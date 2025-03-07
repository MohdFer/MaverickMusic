package it.vfsfitvnm.vimusic.ui.screens.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.media3.common.util.UnstableApi
import it.vfsfitvnm.vimusic.LocalPlayerAwareWindowInsets
import it.vfsfitvnm.vimusic.LocalPlayerServiceBinder
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.enums.ExoPlayerMinTimeForEvent
import it.vfsfitvnm.vimusic.enums.PlayerPlayButtonType
import it.vfsfitvnm.vimusic.enums.PlayerThumbnailSize
import it.vfsfitvnm.vimusic.enums.PlayerTimelineType
import it.vfsfitvnm.vimusic.enums.PlayerVisualizerType
import it.vfsfitvnm.vimusic.enums.UiType
import it.vfsfitvnm.vimusic.ui.components.themed.HeaderWithIcon
import it.vfsfitvnm.vimusic.ui.styling.LocalAppearance
import it.vfsfitvnm.vimusic.utils.UiTypeKey
import it.vfsfitvnm.vimusic.utils.closebackgroundPlayerKey
import it.vfsfitvnm.vimusic.utils.effectRotationKey
import it.vfsfitvnm.vimusic.utils.exoPlayerMinTimeForEventKey
import it.vfsfitvnm.vimusic.utils.isAtLeastAndroid6
import it.vfsfitvnm.vimusic.utils.lastPlayerPlayButtonTypeKey
import it.vfsfitvnm.vimusic.utils.lastPlayerThumbnailSizeKey
import it.vfsfitvnm.vimusic.utils.lastPlayerTimelineTypeKey
import it.vfsfitvnm.vimusic.utils.lastPlayerVisualizerTypeKey
import it.vfsfitvnm.vimusic.utils.persistentQueueKey
import it.vfsfitvnm.vimusic.utils.playerPlayButtonTypeKey
import it.vfsfitvnm.vimusic.utils.playerThumbnailSizeKey
import it.vfsfitvnm.vimusic.utils.playerTimelineTypeKey
import it.vfsfitvnm.vimusic.utils.playerVisualizerTypeKey
import it.vfsfitvnm.vimusic.utils.rememberPreference
import it.vfsfitvnm.vimusic.utils.resumePlaybackWhenDeviceConnectedKey
import it.vfsfitvnm.vimusic.utils.skipSilenceKey
import it.vfsfitvnm.vimusic.utils.thumbnailTapEnabledKey
import it.vfsfitvnm.vimusic.utils.toast
import it.vfsfitvnm.vimusic.utils.volumeNormalizationKey

@ExperimentalAnimationApi
@UnstableApi
@Composable
fun PlayerSettings() {
    val context = LocalContext.current
    val (colorPalette) = LocalAppearance.current
    val binder = LocalPlayerServiceBinder.current

    var persistentQueue by rememberPreference(persistentQueueKey, false)
    var closebackgroundPlayer by rememberPreference(closebackgroundPlayerKey, false)
    var resumePlaybackWhenDeviceConnected by rememberPreference(
        resumePlaybackWhenDeviceConnectedKey,
        false
    )
    var skipSilence by rememberPreference(skipSilenceKey, false)
    var volumeNormalization by rememberPreference(volumeNormalizationKey, false)

    var exoPlayerMinTimeForEvent by rememberPreference(
        exoPlayerMinTimeForEventKey,
        ExoPlayerMinTimeForEvent.`20s`
    )

    var playerThumbnailSize by rememberPreference(playerThumbnailSizeKey, PlayerThumbnailSize.Medium)
    var effectRotationEnabled by rememberPreference(effectRotationKey, true)
    //var wavedPLayerTimelineEnabled by rememberPreference(wavedPlayerTimelineKey, false)
    var thumbnailTapEnabled by rememberPreference(thumbnailTapEnabledKey, false)
    var playerVisualizerType by rememberPreference(playerVisualizerTypeKey, PlayerVisualizerType.Disabled)
    var playerTimelineType by rememberPreference(playerTimelineTypeKey, PlayerTimelineType.Default)
    var playerPlayButtonType by rememberPreference(playerPlayButtonTypeKey, PlayerPlayButtonType.Rectangular)
    var uiType  by rememberPreference(UiTypeKey, UiType.RiMusic)

    var lastPlayerVisualizerType by rememberPreference(lastPlayerVisualizerTypeKey, PlayerVisualizerType.Disabled)
    var lastPlayerTimelineType by rememberPreference(lastPlayerTimelineTypeKey, PlayerTimelineType.Default)
    var lastPlayerThumbnailSize by rememberPreference(lastPlayerThumbnailSizeKey, PlayerThumbnailSize.Medium)
    var lastPlayerPlayButtonType by rememberPreference(lastPlayerPlayButtonTypeKey, PlayerPlayButtonType.Rectangular)

    val activityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Column(
        modifier = Modifier
            .background(colorPalette.background0)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                LocalPlayerAwareWindowInsets.current
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
                    .asPaddingValues()
            )
    ) {
        HeaderWithIcon(
            title = stringResource(R.string.player),
            iconId = R.drawable.app_icon,
            enabled = false,
            showIcon = true,
            modifier = Modifier,
            onClick = {}
        )

        SettingsEntryGroupText(title = stringResource(R.string.effects))

        EnumValueSelectorSettingsEntry(
            title = stringResource(R.string.player_thumbnail_size),
            selectedValue = playerThumbnailSize,
            onValueSelected = {
                playerThumbnailSize = it
                lastPlayerThumbnailSize = it
            },
            valueText = {
                when (it) {
                    PlayerThumbnailSize.Small -> stringResource(R.string.small)
                    PlayerThumbnailSize.Medium -> stringResource(R.string.medium)
                    PlayerThumbnailSize.Big -> stringResource(R.string.big)
                    PlayerThumbnailSize.Biggest -> stringResource(R.string.biggest)
                }
            }
        )

        EnumValueSelectorSettingsEntry(
            title = stringResource(R.string.visualizer),
            selectedValue = playerVisualizerType,
            onValueSelected = {
                playerVisualizerType = it
                lastPlayerVisualizerType = it
            },
            valueText = {
                when (it) {
                    PlayerVisualizerType.Fancy -> stringResource(R.string.vt_fancy)
                    PlayerVisualizerType.Circular -> stringResource(R.string.vt_circular)
                    PlayerVisualizerType.Disabled -> stringResource(R.string.vt_disabled)
                    PlayerVisualizerType.Stacked -> stringResource(R.string.vt_stacked)
                    PlayerVisualizerType.Oneside -> stringResource(R.string.vt_one_side)
                    PlayerVisualizerType.Doubleside -> stringResource(R.string.vt_double_side)
                    PlayerVisualizerType.DoublesideCircular -> stringResource(R.string.vt_double_side_circular)
                    PlayerVisualizerType.Full -> stringResource(R.string.vt_full)
                }
            }
        )
        ImportantSettingsDescription(text = stringResource(R.string.visualizer_require_mic_permission))

        /*
SwitchSettingEntry(
    title = stringResource(R.string.wavy_timeline),
    text = stringResource(R.string.enable_wavy_timeline),
    isChecked = wavedPLayerTimelineEnabled,
    onCheckedChange = { wavedPLayerTimelineEnabled = it }
)
*/
        EnumValueSelectorSettingsEntry(
            title = stringResource(R.string.timeline),
            selectedValue = playerTimelineType,
            onValueSelected = {
                playerTimelineType = it
                lastPlayerTimelineType = it
            },
            valueText = {
                when (it) {
                    PlayerTimelineType.Default -> stringResource(R.string._default)
                    PlayerTimelineType.Wavy -> stringResource(R.string.wavy_timeline)
                    PlayerTimelineType.BodiedBar -> stringResource(R.string.bodied_bar)
                    PlayerTimelineType.PinBar -> stringResource(R.string.pin_bar)
                }
            }
        )

        EnumValueSelectorSettingsEntry(
            title = stringResource(R.string.play_button),
            selectedValue = playerPlayButtonType,
            onValueSelected = {
                playerPlayButtonType = it
                lastPlayerPlayButtonType = it
            },
            valueText = {
                when (it) {
                    PlayerPlayButtonType.Default -> stringResource(R.string._default)
                    PlayerPlayButtonType.Rectangular -> stringResource(R.string.rectangular)
                    PlayerPlayButtonType.Square -> stringResource(R.string.square)
                    PlayerPlayButtonType.CircularRibbed -> stringResource(R.string.circular_ribbed)
                }
            },
            isEnabled = uiType != UiType.ViMusic
        )


        SwitchSettingEntry(
            title = stringResource(R.string.player_rotating_buttons),
            text = stringResource(R.string.player_enable_rotation_buttons),
            isChecked = effectRotationEnabled,
            onCheckedChange = { effectRotationEnabled = it }
        )

//        SettingsEntryGroupText(title = stringResource(R.string.quick_pics_and_tips))

        SettingsEntryGroupText(title = stringResource(R.string.preferences))

        EnumValueSelectorSettingsEntry(
            title = stringResource(R.string.min_listening_time),
            selectedValue = exoPlayerMinTimeForEvent,
            onValueSelected = { exoPlayerMinTimeForEvent = it },
            valueText = {
                when (it) {
                    ExoPlayerMinTimeForEvent.`10s` -> "10s"
                    ExoPlayerMinTimeForEvent.`15s` -> "15s"
                    ExoPlayerMinTimeForEvent.`20s` -> "20s"
                    ExoPlayerMinTimeForEvent.`30s` -> "30s"
                    ExoPlayerMinTimeForEvent.`40s` -> "40s"
                    ExoPlayerMinTimeForEvent.`60s` -> "60s"
                }
            }
        )
        SettingsDescription(text = stringResource(R.string.is_min_list_time_for_tips_or_quick_pics))

        SwitchSettingEntry(
            title = stringResource(R.string.persistent_queue),
            text = stringResource(R.string.save_and_restore_playing_songs),
            isChecked = persistentQueue,
            onCheckedChange = {
                persistentQueue = it
            }
        )


        if (isAtLeastAndroid6) {
            SwitchSettingEntry(
                title = stringResource(R.string.resume_playback),
                text = stringResource(R.string.when_device_is_connected),
                isChecked = resumePlaybackWhenDeviceConnected,
                onCheckedChange = {
                    resumePlaybackWhenDeviceConnected = it
                }
            )
        }

        SwitchSettingEntry(
            title = stringResource(R.string.close_background_player),
            text = stringResource(R.string.when_app_swipe_out_from_task_manager),
            isChecked = closebackgroundPlayer,
            onCheckedChange = {
                closebackgroundPlayer = it
            }
        )

        //SettingsGroupSpacer()

        //SettingsEntryGroupText(title = stringResource(R.string.audio))

        SwitchSettingEntry(
            title = stringResource(R.string.skip_silence),
            text = stringResource(R.string.skip_silent_parts_during_playback),
            isChecked = skipSilence,
            onCheckedChange = {
                skipSilence = it
            }
        )

        SwitchSettingEntry(
            title = stringResource(R.string.loudness_normalization),
            text = stringResource(R.string.autoadjust_the_volume),
            isChecked = volumeNormalization,
            onCheckedChange = {
                volumeNormalization = it
            }
        )

        SwitchSettingEntry(
            title = stringResource(R.string.toggle_lyrics),
            text = stringResource(R.string.by_tapping_on_the_thumbnail),
            isChecked = thumbnailTapEnabled,
            onCheckedChange = { thumbnailTapEnabled = it }
        )

        SettingsEntry(
            title = stringResource(R.string.equalizer),
            text = stringResource(R.string.interact_with_the_system_equalizer),
            onClick = {
                val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL).apply {
                    putExtra(AudioEffect.EXTRA_AUDIO_SESSION, binder?.player?.audioSessionId)
                    putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
                    putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                }

                try {
                    activityResultLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    context.toast("Couldn't find an application to equalize audio")
                }
            }
        )
        SettingsGroupSpacer()
    }
}
