# 🔐 Configuración del Keystore para GesKot

## Situación Actual

La aplicación **GesKot** viene con un keystore preconfigurado para desarrollo y testing:

- **Archivo**: `geskot.jks` (en la raíz del proyecto)
- **Store Password**: `android123`
- **Key Alias**: `geskot`
- **Key Password**: `android123`

## 🚀 Para Releases de Desarrollo/Testing

Si solo necesitas generar APKs para testing o distribución interna, puedes usar el keystore actual:

```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/app-release.apk`

## 🏢 Para Releases de Producción (Google Play Store)

### 1. Crear tu Propio Keystore

**⚠️ IMPORTANTE**: Para subir a Google Play Store, debes crear tu propio keystore único:

```bash
keytool -genkey -v -keystore geskot-production.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias geskot-production \
    -dname "CN=Tu Nombre, OU=Tu Organización, O=Tu Empresa, L=Valencia, ST=Valencia, C=ES"
```

Se te pedirá una contraseña. **¡Guárdala en un lugar seguro!**

### 2. Actualizar la Configuración

Edita `app/build.gradle`:

```gradle
signingConfigs {
    release {
        storeFile file('../geskot-production.jks')
        storePassword 'TU_CONTRASEÑA_SEGURA'
        keyAlias 'geskot-production'
        keyPassword 'TU_CONTRASEÑA_SEGURA'
    }
}
```

### 3. Generar el APK de Producción

```bash
./gradlew assembleRelease
```

## 🔒 Configuración Segura para Producción

### Opción 1: Variables de Entorno

Crea `keystore.properties` (y añádelo a `.gitignore`):
```properties
storePassword=TU_CONTRASEÑA
keyPassword=TU_CONTRASEÑA
keyAlias=geskot-production
storeFile=../geskot-production.jks
```

En `app/build.gradle`:
```gradle
def keystorePropertiesFile = rootProject.file("keystore.properties")
def keystoreProperties = new Properties()
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

android {
    signingConfigs {
        release {
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }
}
```

### Opción 2: Google Play App Signing

1. Sube tu APK firmado a Google Play Console
2. Habilita **Play App Signing**
3. Google gestiona las claves por ti (recomendado)

## 📱 Verificar la Firma

Para verificar que tu APK está correctamente firmado:

```bash
# Verificar la firma
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk

# Ver información del certificado
keytool -printcert -jarfile app/build/outputs/apk/release/app-release.apk
```

## 🚨 Qué NO Hacer

- ❌ **NO** subas keystores de producción a GitHub
- ❌ **NO** compartas las contraseñas en código
- ❌ **NO** uses el keystore de desarrollo para producción
- ❌ **NO** pierdas tu keystore - no podrás actualizar la app

## ✅ Mejores Prácticas

- ✅ Usa contraseñas fuertes y diferentes
- ✅ Haz backup del keystore en múltiples ubicaciones seguras
- ✅ Usa Play App Signing cuando sea posible
- ✅ Mantén las credenciales fuera del código fuente
- ✅ Documenta el proceso para tu equipo

## 🆘 Si Pierdes el Keystore

Si pierdes el keystore de producción:

1. **Google Play Store**: No podrás actualizar la app, tendrás que publicar una nueva
2. **Distribución directa**: Puedes generar un nuevo keystore, pero los usuarios verán advertencias de seguridad

**Por eso es crucial hacer backups del keystore.**

---

**Conclusión**: Para desarrollo usa el keystore actual. Para producción, crea tu propio keystore y manténlo seguro.