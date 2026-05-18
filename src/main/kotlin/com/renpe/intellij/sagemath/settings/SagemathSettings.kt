package com.renpe.intellij.sagemath.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "SagemathSettings",
    storages = [Storage("sagemath.xml")],
)
@Service(Service.Level.APP)
class SagemathSettings : PersistentStateComponent<SagemathSettings.State> {

    data class State(
        var interpreterPath: String = "",
        var defaultWslDistribution: String = "",
        var defaultCondaPath: String = "",
        var defaultCondaEnv: String = "",
        var defaultExtraArgs: String = "",
    )

    @Volatile
    private var state = State()

    override fun getState(): State = state

    override fun loadState(s: State) {
        XmlSerializerUtil.copyBean(s, state)
    }

    companion object {
        fun getInstance(): SagemathSettings =
            ApplicationManager.getApplication().getService(SagemathSettings::class.java)
    }
}
