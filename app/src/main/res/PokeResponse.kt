data class PokeResponse(
    val count: Int,
    val results: List<Pokemon>
)

data class Pokemon(
    val name: String
)