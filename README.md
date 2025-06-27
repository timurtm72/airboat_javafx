# 🚁 ESP32 Airboat Control System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17.0.6-blue.svg)](https://openjfx.io/)
[![Jackson](https://img.shields.io/badge/Jackson-2.16.1-green.svg)](https://github.com/FasterXML/jackson)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> Современное JavaFX приложение для управления системой воздушной лодки на базе ESP32-S3 через UDP протокол с расширенной телеметрией и интуитивным интерфейсом.

![Airboat Control Interface](docs/interface-preview.png)

## 📋 Содержание

- [Особенности](#-особенности)
- [Архитектура](#️-архитектура)
- [Быстрый старт](#-быстрый-старт)
- [Настройка ESP32](#-настройка-esp32)
- [Использование](#-использование)
- [Протокол связи](#-протокол-связи)
- [Сборка и развертывание](#️-сборка-и-развертывание)
- [Устранение неполадок](#-устранение-неполадок)
- [Вклад в проект](#-вклад-в-проект)
- [Лицензия](#-лицензия)

## ✨ Особенности

### 🎮 Управление в реальном времени
- **Сервопривод рулевого управления** с точностью до градуса (0-180°)
- **BLDC мотор** с плавным управлением скоростью (0-100%)
- **Быстрые команды**: СТОП и ЦЕНТР для экстренного управления
- **Автоматическая отправка** команд при изменении параметров

### 📊 Продвинутая телеметрия
- **WiFi мониторинг**: Уровень сигнала с визуальным индикатором
- **Система питания**: Напряжение батареи с прогресс-баром и процентами
- **GPS навигация**: Координаты (широта/долгота) и курс в реальном времени
- **Автообновление**: Телеметрия обновляется каждые 2 секунды

### 🔌 Надежная связь
- **UDP протокол** с настраиваемыми параметрами подключения
- **Визуальные индикаторы** состояния соединения
- **Автоматические переподключения** при потере связи
- **Валидация данных** и обработка ошибок

### 📝 Расширенное логирование
- **Временные метки** для всех событий
- **Цветовая индикация** типов сообщений
- **Отслеживание команд** и ответов системы
- **История операций** с возможностью прокрутки

## 🏗️ Архитектура

### Основные компоненты

```
📁 com.example.aeriboat/
├── 🎯 HelloApplication.java       # Точка входа приложения
├── 🎮 AirboatController.java      # Главный UI контроллер
├── 📦 model/
│   ├── AirboatCommand.java        # Модель команд управления
│   └── AirboatTelemetry.java      # Модель телеметрии
└── 🔧 service/
    ├── UdpClient.java             # UDP клиент для ESP32
    └── JsonService.java           # JSON сериализация (Jackson)
```

### Технологический стек

- **Frontend**: JavaFX 17.0.6 с FXML
- **JSON обработка**: Jackson 2.16.1 (высокопроизводительная сериализация)
- **Сетевое взаимодействие**: Java UDP Sockets
- **Сборка**: Maven 3.6+
- **Runtime**: Java 17+ (модульная система JPMS)

### Паттерны проектирования

- **MVC**: Разделение логики и представления
- **Dependency Injection**: Внедрение зависимостей через конструкторы
- **Async/Await**: Асинхронная обработка команд и телеметрии
- **Observer**: Реактивные обновления UI через JavaFX Properties
- **Strategy**: Гибкая JSON обработка с fallback механизмами

## 🚀 Быстрый старт

### Предварительные требования

- **Java 17+** ([Скачать](https://adoptium.net/))
- **Apache Maven 3.6+** ([Скачать](https://maven.apache.org/download.cgi))
- **ESP32 контроллер** с прошивкой [airboat](https://github.com/timurtm72/airboat)

### Клонирование и запуск

```bash
# Клонируйте репозиторий
git clone https://github.com/your-username/aeriboat.git
cd aeriboat

# Соберите проект
mvn clean compile

# Запустите приложение
mvn javafx:run
```

### Альтернативные способы запуска

<details>
<summary>🔧 Создание исполняемого JAR</summary>

```bash
# Создание JAR с зависимостями
mvn clean package

# Запуск JAR
java --module-path /path/to/javafx/lib \
     --add-modules javafx.controls,javafx.fxml \
     -jar target/aeriboat-1.0-SNAPSHOT.jar
```
</details>

<details>
<summary>🐳 Docker контейнер</summary>

```bash
# Сборка образа
docker build -t aeriboat:latest .

# Запуск контейнера
docker run -p 12345:12345/udp aeriboat:latest
```
</details>

## 🔧 Настройка ESP32

### Конфигурация WiFi точки доступа

| Параметр | Значение |
|----------|----------|
| **SSID** | `ESP32_AIRBOAT_[MAC-адрес]` |
| **Пароль** | `2025` |
| **IP адрес** | `192.168.4.1` |
| **UDP порт** | `12345` |
| **Шифрование** | WPA2-PSK |

### Аппаратные требования

- **Микроконтроллер**: ESP32-S3
- **Сервопривод**: SG90 или аналогичный (PWM управление)
- **BLDC мотор**: С ESC контроллером
- **GPS модуль**: UART интерфейс (опционально)
- **Питание**: Li-Po батарея 3.7V

## 📱 Использование

### 1. Подключение к ESP32

1. **Подключение к WiFi**:
   ```
   Сеть: ESP32_AIRBOAT_XX:XX:XX:XX:XX:XX
   Пароль: 2025
   ```

2. **Настройка в приложении**:
   - IP адрес: `192.168.4.1`
   - UDP порт: `12345`
   - Нажмите "Подключиться"

3. **Проверка соединения**:
   - 🟢 Зеленый индикатор = подключено
   - 🔴 Красный индикатор = отключено

### 2. Управление лодкой

#### Рулевое управление
- **Слайдер угла**: Плавное управление от 0° до 180°
- **Кнопка "ЦЕНТР"**: Быстрая установка в 90° (прямо)
- **Автоотправка**: Команды отправляются мгновенно

#### Управление скоростью
- **Слайдер скорости**: Плавное управление от 0% до 100%
- **Кнопка "СТОП"**: Экстренная остановка (0% скорости)
- **Плавный старт**: Рекомендуется постепенное увеличение скорости

### 3. Мониторинг телеметрии

#### Состояние системы
- **WiFi сигнал**: От -30 dBm (отлично) до -90 dBm (слабо)
- **Батарея**: От 3.0V (разряжена) до 4.2V (полная)
- **Автообновление**: Каждые 2 секунды

#### GPS навигация
- **Координаты**: Широта и долгота в десятичных градусах
- **Курс**: Направление движения в градусах (0-360°)
- **Точность**: Зависит от качества GPS модуля

## 🔌 Протокол связи

### JSON схема команд

#### Команда управления
```json
{
  "angle": 90,     // Угол сервопривода (0-180°)
  "speed": 50      // Скорость мотора (0-100%)
}
```

#### Запрос телеметрии
```json
{
  "status": true   // Запрос статуса системы
}
```

#### Ответ телеметрии
```json
{
  "wifi_signal_level": -45,   // Уровень WiFi сигнала (dBm)
  "battery_voltage": 3700,    // Напряжение батареи (мВ)
  "latitude": 55755500,       // Широта (микроградусы)
  "longitude": 37617700,      // Долгота (микроградусы)
  "course": 180               // Курс (градусы)
}
```

### Особенности протокола

- **Транспорт**: UDP (без гарантии доставки)
- **Кодировка**: UTF-8
- **Максимальный размер пакета**: 1024 байта
- **Таймаут**: 5 секунд на операцию
- **JSON библиотека**: Jackson с fallback парсингом

## 🛠️ Сборка и развертывание

### Зависимости проекта

```xml
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
```

### Профили сборки

<details>
<summary>🎯 Профиль разработки</summary>

```bash
mvn clean compile -Pdev
mvn javafx:run -Pdev
```
</details>

<details>
<summary>🚀 Профиль продакшена</summary>

```bash
mvn clean package -Pprod
mvn javafx:jlink -Pprod  # Создание native image
```
</details>

### Системные требования

| Компонент | Минимум | Рекомендуется |
|-----------|---------|---------------|
| **Java** | 17 | 21+ |
| **RAM** | 512 MB | 1 GB |
| **Хранилище** | 50 MB | 200 MB |
| **ОС** | Windows 10, macOS 10.14, Ubuntu 18.04 | Последние версии |

## 🔍 Устранение неполадок

### Проблемы подключения

<details>
<summary>❌ "Не удается подключиться к ESP32"</summary>

**Возможные причины:**
- ESP32 не в режиме точки доступа
- Неправильный IP адрес или порт
- Брандмауэр блокирует UDP трафик

**Решения:**
1. Проверьте светодиоды на ESP32
2. Убедитесь в подключении к WiFi сети ESP32
3. Попробуйте пинг: `ping 192.168.4.1`
4. Отключите брандмауэр временно
</details>

<details>
<summary>⚠️ "Команды не отправляются"</summary>

**Диагностика:**
1. Проверьте зеленый индикатор подключения
2. Посмотрите лог событий на наличие ошибок
3. Проверьте серийный монитор ESP32

**Решения:**
- Переподключитесь к ESP32
- Перезагрузите ESP32
- Проверьте прошивку ESP32
</details>

<details>
<summary>📡 "Телеметрия не обновляется"</summary>

**Проверки:**
- ESP32 отвечает на ping
- Лог показывает получение данных
- JSON формат корректный

**Исправления:**
- Увеличьте таймаут в настройках
- Проверьте качество WiFi сигнала
- Обновите прошивку ESP32
</details>

### Известные ограничения

- **UDP протокол**: Не гарантирует доставку пакетов
- **Безопасность**: Нет шифрования или аутентификации
- **Производительность**: Ограничена пропускной способностью WiFi
- **Дальность**: Зависит от мощности передатчика ESP32

### Логи и отладка

```bash
# Включение подробного логирования
mvn javafx:run -Djava.util.logging.config.file=logging.properties

# Мониторинг сетевого трафика
tcpdump -i any -n 'port 12345'

# Проверка портов
netstat -an | grep 12345
```

## 🤝 Вклад в проект

Мы приветствуем вклад в развитие проекта! 

### Как внести свой вклад

1. **Fork** репозитория
2. Создайте **feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit** изменения: `git commit -m 'Add amazing feature'`
4. **Push** в branch: `git push origin feature/amazing-feature`
5. Откройте **Pull Request**

### Правила разработки

- Следуйте [Java Code Style](https://google.github.io/styleguide/javaguide.html)
- Покрывайте код тестами (минимум 80%)
- Обновляйте документацию при изменениях API
- Используйте [Conventional Commits](https://www.conventionalcommits.org/)

### Roadmap

- [ ] 🔐 Добавление SSL/TLS шифрования
- [ ] 📱 Мобильное приложение (Android/iOS)
- [ ] 🗺️ Интеграция с картами (OpenStreetMap)
- [ ] 📊 Графики телеметрии в реальном времени
- [ ] 🎮 Поддержка джойстиков и геймпадов
- [ ] ☁️ Облачная синхронизация настроек

## 📞 Поддержка и сообщество

- **GitHub Issues**: [Сообщить о проблеме](https://github.com/your-username/aeriboat/issues)
- **Discussions**: [Обсуждения](https://github.com/your-username/aeriboat/discussions)
- **Wiki**: [Документация](https://github.com/your-username/aeriboat/wiki)
- **Telegram**: [@airboat_community](https://t.me/airboat_community)

## 📄 Лицензия

Проект распространяется под лицензией **MIT License**. См. [LICENSE](LICENSE) для подробностей.

```
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
```

## 👨‍💻 Авторы

- **JavaFX Application**: Разработано с помощью AI Assistant
- **ESP32 Firmware**: [timurtm72](https://github.com/timurtm72/airboat)
- **Technical Consulting**: Сообщество разработчиков

---

<div align="center">

**Сделано с ❤️ для сообщества RC энтузиастов**

⭐ Поставьте звезду если проект вам понравился!

</div> 
