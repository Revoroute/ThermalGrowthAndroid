package co.uk.revoroute.thermalgrowth.data

import android.content.Context
import co.uk.revoroute.thermalgrowth.model.Material
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class MaterialRepository(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    fun loadMaterials(): List<Material> {
        val inputStream = context.resources.openRawResource(
            co.uk.revoroute.thermalgrowth.R.raw.materials
        )
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return json.decodeFromString(jsonString)
    }

    fun groupedMaterials(): List<Pair<String, List<Material>>> {
        return loadMaterials()
            .groupBy { it.category }
            .toSortedMap()   // alphabetical category order
            .map { (key, items) ->
                key to items.sortedBy { it.name }
            }
    }
}