# 🚴‍♂️ GesKot - Aplicación Android de Valenbisi

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-purple.svg)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-green.svg)
![API](https://img.shields.io/badge/API-24+-brightgreen.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

**GesKot** es una aplicación Android moderna desarrollada en Kotlin con Jetpack Compose que permite visualizar en tiempo real la disponibilidad de las estaciones del sistema Valenbisi en Valencia, España.

[📱 Descargar APK](#compilar-apk) • [🚀 Instalación](#instalación-rápida) • [📖 Documentación](#arquitectura-del-proyecto)

</div>

---

## ✨ Características

### 🎯 Funcionalidades Principales
- **📊 Datos en tiempo real**: API oficial de Valencia actualizada cada 10 minutos
- **🗺️ Mapas integrados**: OpenStreetMap gratuito + Google Maps opcional
- **🎨 Colores dinámicos**: Indicadores visuales intuitivos:
  - 🟢 **Verde**: Alta disponibilidad (60%+ bicicletas)
  - 🟡 **Naranja**: Disponibilidad media (30-60% bicicletas)
  - 🔴 **Rojo**: Baja disponibilidad (<30% bicicletas)
- **🔍 Búsqueda inteligente**: Por nombre o dirección de estaciones
- **📍 Información detallada**: Disponibilidad, ubicación, estadísticas
- **☁️ Sin configuración**: Funciona inmediatamente sin API keys

### 🔄 Características Técnicas
- **⚡ Interfaz moderna**: Material Design 3 con animaciones fluidas
- **📱 Diseño adaptativo**: Optimizado para todos los tamaños de pantalla
- **🌐 Conectividad inteligente**: Online con fallback offline
- **♿ Accesibilidad completa**: Soporte para lectores de pantalla

## 🚀 Instalación Rápida

### Opción 1: APK Directo (Más rápido)
```bash
# Compilar e instalar directamente
./gradlew installDebug
```

### Opción 2: Manual
1. **Clonar proyecto:**
   ```bash
   git clone https://github.com/tu-usuario/GesKot.git
   cd GesKot
   ```

2. **Abrir en Android Studio** y ejecutar ▶️

**¡La app funciona inmediatamente!** Los mapas cargan con OpenStreetMap sin configuración.

## 🗺️ Mapas: Doble Opción

### 🆓 OpenStreetMap (Por defecto)
- ✅ **Gratuito y sin configuración**
- ✅ **Funciona inmediatamente**
- ✅ **Datos completos de Valencia**

### 🌍 Google Maps (Opcional)
Si quieres usar Google Maps también:

1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Habilita "Maps SDK for Android"
3. Crea una API Key
4. En `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="google_maps_key">TU_API_KEY_AQUI</string>
   ```

La app permite alternar entre ambos mapas con botones.

## 📱 Pantallas

| Pantalla | Descripción |
|----------|-------------|
| **🏠 Lista Principal** | Estaciones con colores según disponibilidad |
| **🔍 Búsqueda** | Filtros por nombre, dirección o disponibilidad |
| **📋 Detalle** | Info completa: bicis, espacios, estadísticas |
| **🗺️ Mapa** | Ubicación exacta con OpenStreetMap/Google Maps |

## 🛠️ Stack Tecnológico

### Core
- **Kotlin** 1.9.10 - Lenguaje principal
- **Jetpack Compose** 1.5.4 - UI moderna
- **Material Design 3** - Sistema de diseño
- **MVVM** - Arquitectura limpia

### Conectividad
- **OkHttp** 4.12.0 - Cliente HTTP
- **OpenCSV** 5.8 - Procesamiento CSV
- **Valencia OpenData API** - Fuente de datos oficial

### Mapas
- **OpenStreetMap** 6.1.17 - Mapas gratuitos (principal)
- **Google Maps** 18.2.0 - Mapas premium (opcional)

## 📦 Compilar APK

### Debug (Desarrollo)
```bash
./gradlew assembleDebug
# APK en: app/build/outputs/apk/debug/
```

### Release (Distribución)
```bash
./gradlew assembleRelease
# APK en: app/build/outputs/apk/release/
```

## 🏗️ Arquitectura

### Patrón MVVM
```
📡 Valencia API → 📦 Repository → 🧠 ViewModel → 🎨 UI Compose
      ↓              ↑               ↑           ↓
📱 Local Data → StateFlow/LiveData → User Actions
```

### Estructura de Archivos
```
app/src/main/java/com/geskot/app/
├── 📁 data/
│   ├── model/ - ValenbisiStation.kt, UiState.kt
│   └── repository/ - ValenbisiRepository.kt
├── 📁 presentation/
│   ├── screen/ - MainScreen.kt, DetailScreen.kt
│   ├── components/ - OpenStreetMapView.kt
│   └── viewmodel/ - ValenbisiViewModel.kt
└── 📁 ui/theme/ - Colores, tipografía, temas
```

## 🔧 API de Datos

**Fuente oficial:** [Valencia OpenData - Valenbisi](https://valencia.opendatasoft.com/explore/dataset/valenbisi-disponibilitat-valenbisi-dsiponibilidad/)

- **Formato:** CSV con coordenadas reales
- **Actualización:** Cada 10 minutos
- **Campos:** Dirección, número, geo_point_2d, disponible, libre, total
- **Fallback:** Datos locales si falla la conexión

## 📋 Requisitos

- **Android 7.0** (API 24) o superior
- **2GB RAM** recomendado
- **Conexión a Internet** (opcional, con datos offline)
- **Android Studio Hedgehog** para desarrollo

## 🤝 Contribuir

1. Fork el repositorio
2. Crea branch: `git checkout -b feature/nueva-funcionalidad`
3. Commit: `git commit -m "Descripción"`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Abre Pull Request

## 📄 Licencia

MIT License - Ver archivo [LICENSE](LICENSE) para detalles.

---

<div align="center">

### 🚴‍♂️ ¡Disfruta pedaleando por Valencia!

**Desarrollado con ❤️ para la comunidad**

![Valencia](https://img.shields.io/badge/Made%20in-Valencia,%20Spain-FF6B00?style=flat-square)
![Kotlin](https://img.shields.io/badge/Built%20with-Kotlin-7F52FF?style=flat-square)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square)

</div>