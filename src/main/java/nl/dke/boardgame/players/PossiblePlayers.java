package nl.dke.boardgame.players;

/**
 * Stores all possible artificial intelligence players
 */
public enum PossiblePlayers
{
    //NOTE: if you add a new ENUM, also edit the Table class so it supports
    //      the creation of this HexPlayer (createPlayer() method)
    random,
    human,
    alphabeta
}
