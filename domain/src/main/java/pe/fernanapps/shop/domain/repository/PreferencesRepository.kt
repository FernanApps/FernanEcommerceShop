package pe.fernanapps.shop.domain.repository

interface PreferencesRepository {
    fun <T> putValue(key: String, value: T)
    fun <T> getValue(key: String, defaultValue: T): T
}
