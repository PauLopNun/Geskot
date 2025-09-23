# GesKot - Aplicación Android de Valenbisi

**GesKot** es una aplicación Android desarrollada en Kotlin con Jetpack Compose que permite visualizar la disponibilidad de las estaciones de Valenbisi en Valencia, España.

## Características

- **Lista de estaciones**: Visualiza todas las estaciones de Valenbisi con información en tiempo real.
- **Colores dinámicos**: Indicadores visuales basados en la disponibilidad de bicicletas:
  - 🟢 Verde: Alta disponibilidad (60%+)
  - 🟡 Amarillo: Disponibilidad media (30-60%)
  - 🔴 Rojo: Baja disponibilidad (<30%)
- **Búsqueda y filtros**: Busca estaciones por nombre o dirección.
- **Vista detallada**: Información completa de cada estación con mapa integrado.
- **Mapa interactivo**: Ubicación exacta de cada estación usando Google Maps.
- **Carga dinámica**: Descarga datos CSV en tiempo real con fallback local.
- **Material Design 3**: Interfaz moderna y responsive.

## Pantallas Principales

1. **Lista de Estaciones**: Muestra todas las estaciones con indicadores de disponibilidad.
2. **Detalle de Estación**: Información detallada, incluyendo mapa, número de bicicletas y anclajes libres.
3. **Mapa Interactivo**: Vista de todas las estaciones en un mapa con marcadores coloreados según disponibilidad.
4. **Búsqueda y Filtros**: Filtrado por nombre o dirección de estación.

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación principal
- **Jetpack Compose** - UI toolkit moderno
- **Material Design 3** - Sistema de diseño
- **MVVM Architecture** - Patrón arquitectónico
- **StateFlow / LiveData** - Gestión reactiva del estado
- **Google Maps Compose** - Integración de mapas
- **OkHttp** - Cliente HTTP para descargas
- **OpenCSV** - Procesamiento de archivos CSV
- **Navigation Compose** - Navegación entre pantallas
- **Coroutines** - Operaciones asincrónicas y concurrencia

## Requisitos Previos

1. **Android Studio Hedgehog** (2023.1.1) o superior
2. **JDK 8** o superior
3. **Android SDK** con API level 24+ (Android 7.0)
4. **Clave de Google Maps API** (opcional para mapas)

## Configuración e Instalación

### 1. Clonar el proyecto
```bash
git clone <repository-url>
cd GesKot
```

### 2. Configurar Google Maps (Opcional)
1. Obtén una clave API de Google Maps desde [Google Cloud Console](https://console.cloud.google.com/)
2. Edita el archivo `app/src/main/res/values/strings.xml`
3. Reemplaza `YOUR_GOOGLE_MAPS_API_KEY_HERE` con tu clave real:
```xml
<string name="google_maps_key">TU_CLAVE_API_AQUI</string>
```

### 3. Abrir en Android Studio
1. Abre Android Studio
2. Selecciona "Open an existing project"
3. Navega hasta la carpeta del proyecto y ábrela

### 4. Sincronizar dependencias
- Android Studio sincronizará automáticamente las dependencias
- Si no lo hace, ve a `File > Sync Project with Gradle Files`

### 5. Ejecutar la aplicación
1. Conecta un dispositivo Android o configura un emulador
2. Haz clic en el botón "Run" o presiona `Shift + F10`

## Compilar APK

### APK de Debug
```bash
./gradlew assembleDebug
```
El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### APK de Release
```bash
./gradlew assembleRelease
```
El APK se generará en: `app/build/outputs/apk/release/app-release.apk`

## Arquitectura del Proyecto

- **MVVM**: Separación de UI, lógica de negocio y acceso a datos.
- **Repositorios**: Gestionan la obtención de datos (CSV local o remoto).
- **ViewModels**: Exponen datos a la UI de forma reactiva con StateFlow / LiveData.
- **UI (Compose)**: Pantallas declarativas que reaccionan a cambios de estado.

## Flujo de Datos

```
CSV Remoto / Local → Repositorio → ViewModel → UI (Compose)
```

## Estructura del Proyecto

```
app/
└── src/
    └── main/
        ├── java/com/geskot/app/
        │   ├── data/         # Modelos y repositorios
        │   ├── ui/           # Pantallas y componentes Compose
        │   ├── viewmodel/    # ViewModels
        │   └── util/         # Helpers y utilidades
        ├── res/
        │   ├── drawable/
        │   ├── layout/
        │   └── values/
        └── AndroidManifest.xml
```

## Dependencias Principales

- `com.google.accompanist:accompanist-maps`
- `com.squareup.okhttp3:okhttp`
- `com.opencsv:opencsv`
- `androidx.navigation:navigation-compose`
- `androidx.lifecycle:lifecycle-viewmodel-ktx`

## Contribuciones

Las contribuciones son bienvenidas. Puedes abrir un Pull Request o issue para proponer mejoras o reportar errores.

## Licencia

Este proyecto se distribuye bajo la [MIT License](LICENSE).