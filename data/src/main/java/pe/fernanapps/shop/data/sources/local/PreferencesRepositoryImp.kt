package pe.fernanapps.shop.data.sources.local

import pe.fernanapps.shop.domain.repository.PreferencesRepository
import pe.fernanapps.shop.utils.PrefManager
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(private val pre: PrefManager) :
    PreferencesRepository {
    override fun <T> putValue(key: String, value: T) = pre.putValue(key, value)

    override fun <T> getValue(key: String, defaultValue: T) = pre.getValue(key, defaultValue)
}