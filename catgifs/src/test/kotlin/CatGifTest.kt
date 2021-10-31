
import kotlinx.coroutines.flow.first
import me.y9san9.catgifs.CatGifsClient
import java.io.File

private val catgifs = CatGifsClient()

private suspend fun main() {
    val file = File("test.gif")
    file.delete()
    catgifs.randomGifFiles().first()
}
