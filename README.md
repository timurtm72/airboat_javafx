🚁 ESP32 Airboat Control System
Java JavaFX Jackson Maven License

Современное JavaFX приложение для управления системой воздушной лодки на базе ESP32-S3 через UDP протокол с расширенной телеметрией и интуитивным интерфейсом.

Airboat Control Interface

📋 Содержание
Особенности
Архитектура
Быстрый старт
Настройка ESP32
Использование
Протокол связи
Сборка и развертывание
Устранение неполадок
Вклад в проект
Лицензия
✨ Особенности
🎮 Управление в реальном времени
Сервопривод рулевого управления с точностью до градуса (0-180°)
BLDC мотор с плавным управлением скоростью (0-100%)
Быстрые команды: СТОП и ЦЕНТР для экстренного управления
Автоматическая отправка команд при изменении параметров
📊 Продвинутая телеметрия
WiFi мониторинг: Уровень сигнала с визуальным индикатором
Система питания: Напряжение батареи с прогресс-баром и процентами
GPS навигация: Координаты (широта/долгота) и курс в реальном времени
Автообновление: Телеметрия обновляется каждые 2 секунды
🔌 Надежная связь
UDP протокол с настраиваемыми параметрами подключения
Визуальные индикаторы состояния соединения
Автоматические переподключения при потере связи
Валидация данных и обработка ошибок
📝 Расширенное логирование
Временные метки для всех событий
Цветовая индикация типов сообщений
Отслеживание команд и ответов системы
История операций с возможностью прокрутки
🏗️ Архитектура
Основные компоненты
📁 com.example.aeriboat/
├── 🎯 HelloApplication.java       # Точка входа приложения
├── 🎮 AirboatController.java      # Главный UI контроллер
├── 📦 model/
│   ├── AirboatCommand.java        # Модель команд управления
│   └── AirboatTelemetry.java      # Модель телеметрии
└── 🔧 service/
    ├── UdpClient.java             # UDP клиент для ESP32
    └── JsonService.java           # JSON сериализация (Jackson)
Технологический стек
Frontend: JavaFX 17.0.6 с FXML
JSON обработка: Jackson 2.16.1 (высокопроизводительная сериализация)
Сетевое взаимодействие: Java UDP Sockets
Сборка: Maven 3.6+
Runtime: Java 17+ (модульная система JPMS)
Паттерны проектирования
MVC: Разделение логики и представления
Dependency Injection: Внедрение зависимостей через конструкторы
Async/Await: Асинхронная обработка команд и телеметрии
Observer: Реактивные обновления UI через JavaFX Properties
Strategy: Гибкая JSON обработка с fallback механизмами
🚀 Быстрый старт
Предварительные требования
Java 17+ (Скачать)
Apache Maven 3.6+ (Скачать)
ESP32 контроллер с прошивкой airboat
Клонирование и запуск
# Клонируйте репозиторий
git clone https://github.com/your-username/aeriboat.git
cd aeriboat

# Соберите проект
mvn clean compile

# Запустите приложение
mvn javafx:run
Альтернативные способы запуска
🔧 Создание исполняемого JAR
🐳 Docker контейнер
🔧 Настройка ESP32
Конфигурация WiFi точки доступа
Параметр	Значение
SSID	ESP32_AIRBOAT_[MAC-адрес]
Пароль	2025
IP адрес	192.168.4.1
UDP порт	12345
Шифрование	WPA2-PSK
Аппаратные требования
Микроконтроллер: ESP32-S3
Сервопривод: SG90 или аналогичный (PWM управление)
BLDC мотор: С ESC контроллером
GPS модуль: UART интерфейс (опционально)
Питание: Li-Po батарея 3.7V
📱 Использование
1. Подключение к ESP32
Подключение к WiFi:

Сеть: ESP32_AIRBOAT_XX:XX:XX:XX:XX:XX
Пароль: 2025
Настройка в приложении:

IP адрес: 192.168.4.1
UDP порт: 12345
Нажмите "Подключиться"
Проверка соединения:

🟢 Зеленый индикатор = подключено
🔴 Красный индикатор = отключено
2. Управление лодкой
Рулевое управление
Слайдер угла: Плавное управление от 0° до 180°
Кнопка "ЦЕНТР": Быстрая установка в 90° (прямо)
Автоотправка: Команды отправляются мгновенно
Управление скоростью
Слайдер скорости: Плавное управление от 0% до 100%
Кнопка "СТОП": Экстренная остановка (0% скорости)
Плавный старт: Рекомендуется постепенное увеличение скорости
3. Мониторинг телеметрии
Состояние системы
WiFi сигнал: От -30 dBm (отлично) до -90 dBm (слабо)
Батарея: От 3.0V (разряжена) до 4.2V (полная)
Автообновление: Каждые 2 секунды
GPS навигация
Координаты: Широта и долгота в десятичных градусах
Курс: Направление движения в градусах (0-360°)
Точность: Зависит от качества GPS модуля
🔌 Протокол связи
JSON схема команд
Команда управления
{
  "angle": 90,     // Угол сервопривода (0-180°)
  "speed": 50      // Скорость мотора (0-100%)
}
Запрос телеметрии
{
  "status": true   // Запрос статуса системы
}
Ответ телеметрии
{
  "wifi_signal_level": -45,   // Уровень WiFi сигнала (dBm)
  "battery_voltage": 3700,    // Напряжение батареи (мВ)
  "latitude": 55755500,       // Широта (микроградусы)
  "longitude": 37617700,      // Долгота (микроградусы)
  "course": 180               // Курс (градусы)
}
Особенности протокола
Транспорт: UDP (без гарантии доставки)
Кодировка: UTF-8
Максимальный размер пакета: 1024 байта
Таймаут: 5 секунд на операцию
JSON библиотека: Jackson с fallback парсингом
🛠️ Сборка и развертывание
Зависимости проекта
<dependencies>
    <!-- JavaFX UI Framework -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.6</version>
    </dependency>
    
    <!-- JSON Processing -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.16.1</version>
    </dependency>
    
    <!-- Дополнительные UI компоненты -->
    <dependency>
        <groupId>org.controlsfx</groupId>
        <artifactId>controlsfx</artifactId>
        <version>11.1.2</version>
    </dependency>
</dependencies>
Профили сборки
🎯 Профиль разработки
🚀 Профиль продакшена
Системные требования
Компонент	Минимум	Рекомендуется
Java	17	21+
RAM	512 MB	1 GB
Хранилище	50 MB	200 MB
ОС	Windows 10, macOS 10.14, Ubuntu 18.04	Последние версии
🔍 Устранение неполадок
Проблемы подключения
❌ "Не удается подключиться к ESP32"
⚠️ "Команды не отправляются"
📡 "Телеметрия не обновляется"
Известные ограничения
UDP протокол: Не гарантирует доставку пакетов
Безопасность: Нет шифрования или аутентификации
Производительность: Ограничена пропускной способностью WiFi
Дальность: Зависит от мощности передатчика ESP32
Логи и отладка
# Включение подробного логирования
mvn javafx:run -Djava.util.logging.config.file=logging.properties

# Мониторинг сетевого трафика
tcpdump -i any -n 'port 12345'

# Проверка портов
netstat -an | grep 12345
🤝 Вклад в проект
Мы приветствуем вклад в развитие проекта!

Как внести свой вклад
Fork репозитория
Создайте feature branch: git checkout -b feature/amazing-feature
Commit изменения: git commit -m 'Add amazing feature'
Push в branch: git push origin feature/amazing-feature
Откройте Pull Request
Правила разработки
Следуйте Java Code Style
Покрывайте код тестами (минимум 80%)
Обновляйте документацию при изменениях API
Используйте Conventional Commits
Roadmap
 🔐 Добавление SSL/TLS шифрования
 📱 Мобильное приложение (Android/iOS)
 🗺️ Интеграция с картами (OpenStreetMap)
 📊 Графики телеметрии в реальном времени
 🎮 Поддержка джойстиков и геймпадов
 ☁️ Облачная синхронизация настроек
📞 Поддержка и сообщество
GitHub Issues: Сообщить о проблеме
Discussions: Обсуждения
Wiki: Документация
Telegram: @airboat_community
📄 Лицензия
Проект распространяется под лицензией MIT License. См. LICENSE для подробностей.

MIT License

Copyright (c) 2025 ESP32 Airboat Project

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
👨‍💻 Авторы
JavaFX Application: Разработано с помощью AI Assistant
ESP32 Firmware: timurtm72
Technical Consulting: Сообщество разработчиков
Сделано с ❤️ для сообщества RC энтузиастов

⭐ Поставьте звезду если проект вам понравился!
