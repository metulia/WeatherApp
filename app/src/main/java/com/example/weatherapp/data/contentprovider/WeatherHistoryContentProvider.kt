package com.example.weatherapp.data.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.weatherapp.MyApp
import com.example.weatherapp.R
import com.example.weatherapp.data.repository.room.*

private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH = "WeatherHistoryEntity"

class WeatherHistoryContentProvider : ContentProvider() {

    private var authorities: String? = null // Адрес URI
    private lateinit var uriMatcher: UriMatcher // Помогает определить тип адреса URI

    // Типы данных
    private var entityContentType: String? = null // Набор строк
    private var entityContentItemType: String? = null // Одна строка
    private lateinit var contentUri: Uri // Адрес URI Provider

    override fun onCreate(): Boolean {
        authorities = context?.resources?.getString(R.string.authorities)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        uriMatcher.addURI(authorities, "$ENTITY_PATH/#", URI_ID)
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor {
        val weatherHistoryDao: WeatherHistoryDao = MyApp.getWeatherHistoryDao()
        val cursor = when (uriMatcher.match(p0)) {
            URI_ALL -> weatherHistoryDao.getWeatherHistoryCursor()
            URI_ID -> {
                val id = ContentUris.parseId(p0)
                weatherHistoryDao.getWeatherHistoryCursor(id)
            }
            else -> throw IllegalArgumentException("Wrong URI: $p0")
        }
// Устанавливаем нотификацию при изменении данных в content_uri
        cursor.setNotificationUri(context!!.contentResolver, contentUri)
        return cursor
    }

    override fun getType(p0: Uri): String? {
        when (uriMatcher.match(p0)) {
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        require(uriMatcher.match(p0) == URI_ALL) { throw IllegalArgumentException("Wrong URI: $p0") }
        val weatherHistoryDao: WeatherHistoryDao = MyApp.getWeatherHistoryDao()
        return map(p1)?.let {
            weatherHistoryDao.insert(it)
            val resultUri = ContentUris.withAppendedId(contentUri, it.id)
            context?.contentResolver?.notifyChange(resultUri, null)
            resultUri
        }
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        require(uriMatcher.match(p0) == URI_ID) { throw IllegalArgumentException("Wrong URI: $p0") }
        val weatherHistoryDao: WeatherHistoryDao = MyApp.getWeatherHistoryDao()
        val id = ContentUris.parseId(p0)
        weatherHistoryDao.deleteById(id)
        context?.contentResolver?.notifyChange(p0, null)
        return 1
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        require(uriMatcher.match(p0) == URI_ID) { throw IllegalArgumentException("Wrong URI: $p0") }
        val weatherHistoryDao: WeatherHistoryDao = MyApp.getWeatherHistoryDao()
        map(p1)?.let {
            weatherHistoryDao.update(it)
        }
        context!!.contentResolver.notifyChange(p0, null)
        return 1
    }

    private fun map(values: ContentValues?): WeatherHistoryEntity? {
        return if (values == null) {
            null
        } else {
            val id = if (values.containsKey(ID)) values[ID] as Long else 0
            val city = values[CITY] as String
            val temperature = values[TEMPERATURE] as Int
            val feelLike = values[FEELS_LIKE] as Int
            val icon = values[ICON] as String
            WeatherHistoryEntity(id, city, temperature, feelLike, icon)
        }
    }

}