import com.example.weather_app.prefs.PreferenceStorage
import com.google.gson.Gson

class FakePreferenceStorage : PreferenceStorage {

    private val storage = mutableMapOf<String, String>()
    private val gson = Gson()

    override fun <T> save(key: String, value: T) {
        storage[key] = when (value) {
            is String -> value
            is Int, is Boolean, is Float, is Long, is Double -> value.toString()
            else -> gson.toJson(value)
        }
    }

    override fun getString(key: String, default: String): String {
        return storage[key] ?: default
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return 0
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return 0L
    }
}