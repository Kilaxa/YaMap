package com.example.yamap

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import com.yandex.mapkit.mapview.MapView as MapView

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private val tapListeners = mutableListOf<MapObjectTapListener>() // Список для хранения слушателей

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация MapKitFactory
        MapKitFactory.setApiKey("611433d1-f7a1-4db6-a5b2-b3dd65b9ccf0")
        MapKitFactory.initialize(this)

        setContentView(R.layout.layout)

        mapView = findViewById(R.id.mapview)

        // Перемещаем камеру к указанной точке
        mapView.map.move(
            CameraPosition(Point(54.724587, 55.940793), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 300f),
            null
        )

        // Пример добавления метки
        addPlacemark(55.751244, 37.618423, "Москва", "Столица России")
        addPlacemark(59.934280, 30.335099, "Санкт-Петербург","Культурная столица")
        addPlacemark(54.724841, 55.940839, "Уфа, 6ка","Столица Мира")

        addPlacemark(43.1626, -1.2360, "Saint-Jean-Pied-de-Port", "Франция\nНачало Французского Пути. Этот городок в Пиренеях является отправной точкой для паломников.")
        addPlacemark(43.0097, -1.3169, "Roncesvalles","Испания\nИзвестен своим монастырем и является первой остановкой на испанской стороне Пути.")
        addPlacemark(42.8185, -1.6432, "Pamplona", "Испания\nГород с богатой историей, известный своими бегами с быками во время фестиваля Сан-Фермин.")
        addPlacemark(42.6707, -1.8169, "Puente la Reina", "Испания\nИзвестен своим средневековым мостом, построенным для паломников на пути к Сантьяго.")
        addPlacemark(42.4650, -2.4456, "Logroño", "Испания\nСтолица региона Ла-Риоха, известная своими винодельнями и историческим центром.")
        addPlacemark(42.3439, -3.6969, "Burgos", "Испания\nЗдесь находится знаменитый готический собор, включенный в список объектов ЮНЕСКО.")
        addPlacemark(42.5987, -5.5671, "León", "Испания\nГород с богатым культурным наследием, включая величественный собор и монастырь.")
        addPlacemark(42.4570, -6.0599, "Astorga", "Испания\nИсторический город с прекрасным собором и Дворцом Гауди.")
        addPlacemark(42.5465, -6.5982, "Ponferrada", "Испания\nГород с замком тамплиеров, одной из самых впечатляющих крепостей на Пути.")
        addPlacemark(42.7791, -7.4161, "Sarria", "Испания\nЧасто служит стартовой точкой для тех, кто проходит минимальное расстояние для получения сертификата Компостела.")
        addPlacemark(42.8782, -8.5448, "Santiago de Compostela", "Испания\nКонечная точка паломничества. Здесь находится собор, где, по преданию, покоятся мощи Святого Иакова.")
    }

    // Функция для добавления метки
    private fun addPlacemark(latitude: Double, longitude: Double, title: String, description: String) {
        // Проверяем, что карта инициализирована и готова к использованию
        if (this::mapView.isInitialized && mapView.map != null) {
            val point = Point(latitude, longitude)

            // Создаем метку на карте
            val placemark = mapView.map.mapObjects.addPlacemark(
                point,
                ImageProvider.fromResource(this, R.drawable.custom_icon) // Укажите свою иконку метки
            )

            // Создаем слушатель для метки
            val tapListener = MapObjectTapListener { _, _ ->
                // Показываем диалог с названием и описанием метки
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(description)
                    .setPositiveButton("OK", null)
                    .show()
                true
            }

            // Присваиваем слушатель метке и сохраняем его в список
            placemark.addTapListener(tapListener)
            tapListeners.add(tapListener) // Сохраняем слушатель в списке
        }
    }

    override fun onStart() {
        super.onStart()

        // Проверка перед вызовом onStart для избежания взаимодействия с неинициализированной картой
        if (this::mapView.isInitialized) {
            mapView.onStart()
            MapKitFactory.getInstance().onStart()
        }
    }

    override fun onStop() {
        super.onStop()

        // Очистка объектов карты перед остановкой
        if (this::mapView.isInitialized) {
            mapView.map.mapObjects.clear() // Удаляем все объекты с карты
            tapListeners.clear() // Очищаем список слушателей
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Если активность уничтожается, убедитесь, что ресурсы освобождены
        if (this::mapView.isInitialized) {
            mapView.onStop() // Завершаем работу с картой
        }
    }
}
