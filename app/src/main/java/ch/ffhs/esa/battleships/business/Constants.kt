package ch.ffhs.esa.battleships.business

const val OFFLINE_PLAYER_ID = "x12345123451234512345"
const val BOT_PLAYER_ID = "x54321543215432154321"
const val FIREBASE_BOARD_PATH = "board"
const val FIREBASE_GAME_WITHOUT_ATTACKERS_PATH = "games-without-attackers"
const val FIREBASE_PLAYER_PATH = "player"
const val BOARD_SIZE = 10
const val STRICT_OVERLAP_RULE =
    true    // if true: no ships are allowed to touch each other, else: ships can touch, but not overlap
const val WINNING_SCORE_MULTIPLIER =
    5            // when it comes to calculating scores, a win will be multiplied with this value

