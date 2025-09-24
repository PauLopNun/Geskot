# 📱 Distribuir APK de GesKot

## ✅ APK Generado
Tu APK optimizado está en: `app/build/outputs/apk/release/app-release-unsigned.apk`

## 🚀 Opciones de Distribución

### 🏆 **Opción 1: GitHub Releases (Mejor)**

**Pasos:**
1. **Subir código a GitHub** (si no lo has hecho)
2. **Crear un Release:**
   - Ve a tu repo GitHub
   - Click en "Releases" → "Create a new release"
   - Tag: `v1.0.0`
   - Title: `GesKot v1.0 - App ValenBisi Valencia`
   - Description: Copia el contenido de abajo ↓
   - **Arrastra tu APK** en "Attach binaries"

**Descripción para el Release:**
```markdown
# 🚴‍♂️ GesKot v1.0 - App Valenbisi para Valencia

## ✨ Características
- 📊 **Datos en tiempo real** de todas las estaciones Valenbisi
- 🗺️ **Mapas gratuitos** con OpenStreetMap (sin API keys)
- 🎨 **Colores dinámicos** según disponibilidad
- 🔍 **Búsqueda inteligente** por nombre/dirección
- 📱 **Material Design 3** moderno

## 📥 Instalación
1. Descarga `app-release-unsigned.apk`
2. Habilita "Fuentes desconocidas" en Android
3. Instala el APK
4. ¡Disfruta pedaleando por Valencia! 🚴‍♂️

## 🔧 Requisitos
- Android 7.0+ (API 24)
- Conexión a internet (opcional)

## 📍 Datos
API oficial Valencia OpenData - Actualización cada 10 minutos
```

### 🌐 **Opción 2: Hosting Directo**

Si tienes un servidor web o Netlify/Vercel:

```html
<!-- Botón de descarga -->
<a href="./geskot-v1.0.apk" download>
  📱 Descargar GesKot APK
</a>
```

### ☁️ **Opción 3: Cloud Storage**

**Google Drive/Dropbox:**
1. Sube el APK
2. Comparte enlace público
3. Agrega al README

### 📦 **Opción 4: En el Repo (No recomendado)**

GitHub tiene límite de 100MB por archivo. Tu APK es de ~12MB, así que técnicamente cabe, pero **no es recomendado** porque:
- Incrementa el tamaño del repo
- Hace el clone más lento
- Los binarios no deberían estar en git

## 🎯 **Recomendación Final**

**Usa GitHub Releases** - es gratis, profesional y está integrado con tu repo.

## 🔄 **Para actualizaciones futuras:**

```bash
# Cambiar versión en app/build.gradle
versionCode 2
versionName "1.1"

# Generar nuevo APK
./gradlew assembleRelease

# Crear nuevo release en GitHub
```