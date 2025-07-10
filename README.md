[![CI](https://github.com/Romanow/log4j2-masking-lib/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Romanow/log4j2-masking-lib/actions/workflows/build.yml)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)
[![Release](https://img.shields.io/github/v/release/Romanow/log4j2-masking-lib?logo=github&sort=semver)](https://github.com/Romanow/log4j2-masking-lib/releases/latest)
[![Codecov](https://codecov.io/gh/Romanow/log4j2-masking-lib/branch/master/graph/badge.svg?token=Cckw6pHLh7)](https://codecov.io/gh/Romanow/log4j2-masking-lib)
[![License](https://img.shields.io/github/license/Romanow/log4j2-masking-lib)](https://github.com/Romanow/log4j2-masking-lib/blob/master/LICENSE)

# Masking library for Log4j2

## Подключение

### Maven

```xml

<dependency>
    <groupId>ru.romanow-alex</groupId>
    <artifactId>log4j2-masking-lib</artifactId>
    <version>${log-masking-lib.version}</version>
</dependency>
```

### Gradle

```groovy
testImplementation "ru.romanow-alex:log4j2-masking-lib:$logMaskingVersion"
```

## Реализация

Для маскирования логов используется
плагин [`MaskingConverter`](src/main/kotlin/ru/romanow/logging/filter/MaskingConverter.kt).

Для его подключения используется annotation processor `org.apache.logging.log4j:log4j-core`, который собирает плагины
в [`$buildDir/tmp/kapt3/classes/main/META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat`](build/tmp/kapt3/classes/main/META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat).

В шаблоне этот конвертер вызывается как `%mask{ %msg }`:

```xml

<PatternLayout pattern="%d{yyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %mask{ %msg }%n"/>
```

## Правила маскирования

Тип поля:

* `email` –> `romanowalex@mail.ru` -> `r**********@mail.ru`.
* `firstName` –> не маскируется.
* `lastName`, `middleName` –> if (name.length > 7) `Ab*****c` else `A*****`.
* `text` –> в зависимости от длины:
    * 1 -> `*`;
    * 1..4 -> `1234` -> `1***`;
    * 5..9 -> `123456789` -> `1******89` (length * 60%);
    * 10..15 -> `12345678901` -> `12*******01` (length * 60%);
    * 16 + -> `12345678901234567` -> `123***********567` (length * 60%);

Правила описаны в [`resources/logging/rules.yml`](src/main/resources/logging/rules.yml). Если требуется добавить новые
правила, то нужно в `resources/logging` создать файл `additional-rules.yml`.

```yaml
masking:
  - field: 'fullName'
    type: FULL_NAME
  - field: 'lastName'
    type: NAME
  - field: 'email'
    type: EMAIL
  - field: 'JWT'
    type: TEXT
  - regex: '(?:JWT|Authorization)\s*(?::|=)\s*(.+)'
    type: TEXT
 ```
