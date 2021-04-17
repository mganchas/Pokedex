import com.example.pokedex.data.extensions.addIfNotNull
import com.example.pokedex.data.models.PokemonSprites
import java.util.*

fun PokemonSprites.toLinkedSet() : LinkedList<String> {
    val list = LinkedList<String>()
    list.addIfNotNull(this.backDefault)
    list.addIfNotNull(this.backFemale)
    list.addIfNotNull(this.backShiny)
    list.addIfNotNull(this.backShinyFemale)
    list.addIfNotNull(this.frontDefault)
    list.addIfNotNull(this.frontFemale)
    list.addIfNotNull(this.frontShiny)
    list.addIfNotNull(this.frontShinyFemale)
    return list
}