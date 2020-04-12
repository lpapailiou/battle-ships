package battleships.esa.ffhs.ch.data

class MockDatabase private constructor() {

    companion object {
        @Volatile private var instance: MockDatabase? = null

        fun getInstance() = instance
            ?: synchronized(this) {
            instance
                ?: MockDatabase().also { instance = it }
        }
    }

    var gameListDao = GameListMockDao()
        private set

}