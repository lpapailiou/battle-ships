package battleships.esa.ffhs.ch.entity

class MockDatabase private constructor() {

    companion object {
        @Volatile private var instance: MockDatabase? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance?: MockDatabase().also { instance = it }
        }
    }

    var gameDao = GameDao()
        private set

}