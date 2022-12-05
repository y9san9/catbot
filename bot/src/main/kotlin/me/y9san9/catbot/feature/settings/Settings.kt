package me.y9san9.catbot.feature.settings

import kotlinx.coroutines.flow.Flow
import me.y9san9.catbot.di.CoroutineScopeDeps

interface SettingsDeps : CoroutineScopeDeps {
    val commands: Flow<SettingsCommand>
    val languageChosen: Flow<LanguageChosen>
    val languageInput: Flow<LanguageInput>

    sealed interface SettingsCommand {
        suspend fun sendSettings()
    }
    interface LanguageChosen {
        suspend fun sendLanguageInput()
    }
    interface LanguageInput {
        suspend fun save()
    }
}

context(SettingsDeps)
fun handleSettings() {
    commands.launchEach {
        sendSettings()
    }
    languageChosen.launchEach {
        sendLanguageInput()
    }
    languageInput.launchEach {
        save()
    }
}
