# 🚴‍♂️ GesKot - Aplicación Android de Valenbisi

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-purple.svg)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-green.svg)
![API](https://img.shields.io/badge/API-24+-brightgreen.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

**GesKot** es una aplicación Android moderna desarrollada en Kotlin con Jetpack Compose que permite visualizar en tiempo real la disponibilidad de las estaciones del sistema Valenbisi en Valencia, España.

[📱 **DESCARGAR APK**](../../releases/latest) • [🚀 Instalación](#instalación-rápida) • [📖 Documentación](#arquitectura-del-proyecto)

</div>

---

## ✨ Características

### 🎯 Funcionalidades Principales
- **📊 Datos en tiempo real**: API oficial de Valencia actualizada cada 10 minutos
- **🗺️ Mapas integrados**: OpenStreetMap gratuito, optimizado y sin configuración
- **🎨 Colores dinámicos**: Indicadores visuales intuitivos:
  - 🟢 **Verde**: Alta disponibilidad (60%+ bicicletas)
  - 🟡 **Naranja**: Disponibilidad media (30-60% bicicletas)
  - 🔴 **Rojo**: Baja disponibilidad (<30% bicicletas)
- **🔍 Búsqueda y filtros avanzados**: Por nombre, dirección, bicis disponibles o espacios libres
- **📍 Información detallada**: Disponibilidad, ubicación, estadísticas y mapas
- **☁️ Sin configuración**: Funciona inmediatamente sin API keys
- **🏷️ Nombres descriptivos**: Estaciones identificadas por ubicaciones reales (Xàtiva, Ayuntamiento, etc.)

### 🔄 Características Técnicas
- **⚡ Interfaz moderna**: Material Design 3 con animaciones fluidas
- **📱 Diseño adaptativo**: Optimizado para todos los tamaños de pantalla
- **🌐 Conectividad inteligente**: Online con fallback offline
- **♿ Accesibilidad completa**: Soporte para lectores de pantalla

## 📱 Descargar App

### 🎯 **Para Usuarios** (Solo descargar)
[![Download APK](https://img.shields.io/badge/📱_Descargar_APK-v1.0.0-success?style=for-the-badge)](../../releases/latest)

**📦 APK Listo**: `GesKot-v1.0-release.apk` (11.3 MB) - ¡Funcional y optimizado!

1. **Descarga** el APK desde GitHub Releases
2. **Habilita** "Fuentes desconocidas" en Android
3. **Instala** y ¡disfruta pedaleando por Valencia! 🚴‍♂️

### 🚀 **Para Desarrolladores** (Compilar código)

#### Opción 1: APK Directo
```bash
./gradlew installDebug
```

#### Opción 2: Desde Código
1. **Clonar proyecto:**
   ```bash
   git clone https://github.com/tu-usuario/GesKot.git
   cd GesKot
   ```
2. **Abrir en Android Studio** y ejecutar ▶️

**¡La app funciona inmediatamente!** Los mapas cargan con OpenStreetMap sin configuración.

## 🗺️ Mapas: OpenStreetMap

### 🆓 OpenStreetMap (Único y Optimizado)
- ✅ **Gratuito y sin configuración**
- ✅ **Funciona inmediatamente**
- ✅ **Datos completos de Valencia**
- ✅ **Tamaño optimizado y sin botones innecesarios**
- ✅ **Rendimiento mejorado con menos advertencias HWUI**

> **Nota:** Se removió Google Maps para simplificar la experiencia y eliminar dependencias innecesarias. OpenStreetMap ofrece toda la funcionalidad necesaria para ubicar las estaciones.

## 📱 Pantallas

| Pantalla | Descripción |
|----------|-------------|
| **🏠 Lista Principal** | Estaciones con nombres descriptivos y colores según disponibilidad |
| **🔍 Búsqueda y Filtros** | Búsqueda por nombre/dirección + switches para bicis/espacios disponibles |
| **📋 Detalle** | Info completa: bicis, espacios, estadísticas y progreso visual |
| **🗺️ Mapa** | Ubicación exacta con OpenStreetMap optimizado |

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
- **OpenStreetMap** 6.1.17 - Mapas gratuitos optimizados

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

### 🔐 Configuración Keystore (Para Releases)

La aplicación viene configurada con un keystore para firmar releases. Las credenciales son:

- **Archivo**: `geskot.jks` (en la raíz del proyecto)
- **Store Password**: `android123`
- **Key Alias**: `geskot`
- **Key Password**: `android123`

**Para producción**, debes generar tu propio keystore:

```bash
keytool -genkey -v -keystore mi-app.jks -keyalg RSA -keysize 2048 -validity 10000 -alias mi-app-key
```

Luego actualiza en `app/build.gradle`:
```gradle
signingConfigs {
    release {
        storeFile file('../mi-app.jks')
        storePassword 'TU_PASSWORD'
        keyAlias 'mi-app-key'
        keyPassword 'TU_PASSWORD'
    }
}
```

> ⚠️ **Importante**: Nunca subas keystores de producción a repositorios públicos. Usa variables de entorno o archivos locales.

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