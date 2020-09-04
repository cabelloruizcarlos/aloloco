package es.ccrr.aloloco

import android.app.Application
import es.ccrr.aloloco.engine.Engine
import es.ccrr.aloloco.engine.EngineFactory

class AlolocoApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val engineFactory = EngineFactory(this)
        Engine.initialize(engineFactory)
    }
}