package es.ccrr.aloloco.engine

import es.ccrr.aloloco.engine.api.controllers.ServiceController

class Engine private constructor(val serviceController: ServiceController) {

    companion object {

        @Volatile
        private var engine: Engine? = null

        fun initialize(engineFactory: EngineFactory) {
            engine = Engine(engineFactory.serviceController)
        }

        fun getInstance(): Engine {
            return engine
                ?: throw RuntimeException("You must call Engine.initialize before Engine.getInstance")
        }
    }
}