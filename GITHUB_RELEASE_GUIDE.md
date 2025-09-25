# 📦 Guía para Crear GitHub Release

## 🎯 Pasos para Subir el APK a GitHub Releases

### 1. **Preparar el Repositorio**

Asegúrate de que todos los cambios estén commiteados y pusheados:

```bash
git add .
git commit -m "v1.0: Release version with all improvements"
git push origin main
```

### 2. **Crear el Release en GitHub**

#### **Opción A: Interfaz Web (Recomendada)**

1. Ve a tu repositorio en GitHub
2. Click en **"Releases"** (en la barra lateral derecha)
3. Click en **"Create a new release"**

#### **Configuración del Release:**

- **Tag version**: `v1.0.0`
- **Release title**: `🚴‍♂️ GesKot v1.0 - Valencia Bike Share App`
- **Target**: `main` (rama principal)

#### **Descripción del Release:**
Copia y pega el contenido de `RELEASE_NOTES_v1.0.md`:

```markdown
## 🎉 Primera Versión Estable - Valencia Bike Share App

GesKot v1.0 es la primera versión completa de la aplicación Android para consultar en tiempo real la disponibilidad de las estaciones Valenbisi en Valencia, España.

## ✨ Características Principales

### 🔍 Búsqueda y Filtros Inteligentes
- Búsqueda por texto: Encuentra estaciones por nombre o dirección
- Filtro por bicis disponibles: Switch para mostrar solo estaciones con bicicletas
- Filtro por espacios libres: Switch para mostrar solo estaciones con espacios
- Filtros combinables: Usa múltiples filtros simultáneamente

### 🏷️ Nombres Descriptivos de Estaciones
- Ubicaciones reales: "Xàtiva", "Ayuntamiento", "Plaza de la Reina"
- Sin numeración genérica: Ya no verás "Estación 1, Estación 2"
- Fácil identificación: Nombres basados en lugares conocidos de Valencia

### 🗺️ Mapas Optimizados
- OpenStreetMap integrado: Gratuito y sin configuración
- Tamaño óptimo: 350dp de altura, perfecto para la información
- Sin botones innecesarios: Diseño limpio y funcional
- Rendimiento mejorado: Menos warnings HWUI, más fluidez

## 📱 Instalación

### Para Usuarios
1. Descarga `GesKot-v1.0-release.apk`
2. Habilita "Orígenes desconocidos" en Android
3. Instala el APK
4. ¡Disfruta explorando las estaciones de Valencia!

### Requisitos del Sistema
- Android 7.0 (API 24) o superior
- 50 MB de espacio libre
- Conexión a Internet (recomendada)

---

**¡Gracias por probar GesKot! 🚴‍♂️**

*Desarrollado con ❤️ para la comunidad de Valencia*
```

### 3. **Subir el APK**

En la sección **"Attach binaries"**:

1. Click en **"Attach files by dropping them here or selecting them"**
2. Selecciona el archivo: `GesKot-v1.0-release.apk` (11.3 MB)
3. Espera a que se suba (puede tardar unos minutos)

### 4. **Opciones Adicionales**

- ✅ **Set as the latest release** (marcado)
- ⬜ **Set as a pre-release** (desmarcado, es versión estable)
- ⬜ **Create a discussion** (opcional)

### 5. **Publicar el Release**

1. Click en **"Publish release"**
2. El APK estará disponible públicamente en: `https://github.com/TU_USUARIO/GesKot/releases/latest`

---

## 🔗 Resultado Final

Una vez publicado, tendrás:

- **URL del Release**: `https://github.com/TU_USUARIO/GesKot/releases/tag/v1.0.0`
- **Descarga directa**: `https://github.com/TU_USUARIO/GesKot/releases/download/v1.0.0/GesKot-v1.0-release.apk`
- **Badge para README**:
  ```markdown
  [![Download APK](https://img.shields.io/badge/📱_Descargar_APK-v1.0-success?style=for-the-badge)](https://github.com/TU_USUARIO/GesKot/releases/latest)
  ```

---

## 📋 Checklist Final

Antes de publicar, verifica:

- ✅ APK generado correctamente (`GesKot-v1.0-release.apk`)
- ✅ Tamaño aproximado: ~11.3 MB
- ✅ Todos los commits pusheados a main
- ✅ Release notes preparadas
- ✅ Tag version en formato semántico (v1.0.0)

---

## 🚀 Promocionar el Release

Después de publicar:

1. **Actualiza el README.md** con el link del release
2. **Comparte en redes sociales** o comunidades de Valencia
3. **Pide feedback** a los primeros usuarios
4. **Documenta bugs** para la próxima versión

**¡Tu app está lista para que la pruebe la comunidad! 🎉**