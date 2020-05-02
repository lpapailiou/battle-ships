package battleships.esa.ffhs.ch.old.model

enum class GameState(val tag: String, val id: Int) {

    INIT("INIT", 0),
    PREPARATION("PREPARATION", 1),
    ACTIVE("ACTIVE", 2),
    ENDED("ENDED", 3);

}
