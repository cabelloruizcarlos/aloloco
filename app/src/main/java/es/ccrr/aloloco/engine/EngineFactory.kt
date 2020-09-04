package es.ccrr.aloloco.engine

import android.content.Context
import es.ccrr.aloloco.engine.api.controllers.ServiceController

class EngineFactory(val context: Context) {

    val serviceController: ServiceController by lazy {
        ServiceController()
    }
}
