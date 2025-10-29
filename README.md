# Phone Tracker App

Descripción
-----------
Aplicación Android para rastrear la ubicación de números telefónicos mexicanos basada en su código de área. Incluye límite de 1 rastreo gratuito diario y flujo de pago para activar Premium mediante tarjeta (Stripe SDK integrado) o SPEI (transferencia manual simulada).

Características
---------------
- Interfaz en español con estilo de colores azul/blanco y tema de mapa.
- Campo para número de teléfono mexicano, botón "Rastrear".
- Simulación de rastreo (3 segundos), muestra ubicación basada en código de área.
- Visualización de ubicación en mapa interactivo con Google Maps.
- Base de datos integrada de códigos de área mexicanos con direcciones ficticias.
- Límite de 1 rastreo gratuito por día, almacenamiento local con SharedPreferences.
- Flujo de pago: tarjeta (Stripe SDK) y SPEI (CLABE mostrada).
- Al activar Premium se eliminan límites y se guarda estado localmente.

Prerequisitos
-------------
- Android Studio (Arctic Fox o superior).
- JDK 17 (compatible con compileOptions configurados).
- Android SDK platform 33 instalado.
- Google Maps API key (para visualización de mapas).
- Conexión a Internet para descargar dependencias.
- (Opcional) Gradle 8.x si no usas el wrapper de Android Studio.

Archivos clave
--------------
- app/src/main/java/com/example/phonetracker/
  - MainActivity.kt - Pantalla principal con entrada de número telefónico
  - LocationDetailActivity.kt - Visualización de mapa y detalles de ubicación
  - PaymentActivity.kt - Sistema de pago premium
  - LocationData.kt, LocationDatabase.kt - Modelos de datos y simulación
  - MyApplication.kt - Configuración global de la aplicación
- app/src/main/res/ - Layouts, strings, styles, drawables
- app/build.gradle - Configuración de dependencias y SDK

Instalación y ejecución
----------------------
1. Descarga/clona el proyecto y ábrelo en Android Studio.
2. Si Android Studio solicita sincronizar Gradle, acepta y deja que descargue dependencias.
3. Configura la clave pública de Stripe (opcional para pagos):
   - Edita `app/build.gradle` y reemplaza el valor de `buildConfigField "String", "STRIPE_PUBLISHABLE_KEY"` con tu clave.
4. Conecta un dispositivo Android o inicia un emulador.
5. Ejecuta la app desde Android Studio: Run > app.

Generar APK
-----------
1. Automáticamente mediante GitHub Actions:
   - El workflow "Build Phone Tracker APK" se ejecuta automáticamente con cada push a main.
   - Descarga el APK desde la sección "Artifacts" del workflow en GitHub Actions.

2. Manualmente desde Android Studio:
   - Build > Build Bundle(s) / APK(s) > Build APK(s).
   - Localiza el APK en `app/build/outputs/apk/`.

Uso
---
- Ingresa un número de teléfono mexicano válido (10 dígitos).
- Pulsa "Rastrear". Verás una animación de búsqueda por 3 segundos.
- Se mostrará el mapa con la ubicación y detalles como ciudad, dirección y última vez visto.
- Puedes compartir la ubicación con el botón correspondiente.
- Si alcanzas el límite diario, actualiza a Premium para rastreos ilimitados.
- Tras 1 rastreo gratuito recibirás un aviso para actualizar a Premium.
- Si eliges actualizar, se abrirá la pantalla de pago. Al confirmar (tarjeta o SPEI), la app marca Premium activo.

Notas de pago
-------------
- Este proyecto simula pagos localmente para la demo.
- Stripe SDK está incluido (com.stripe:stripe-android:20.20.0). Para pagos reales, debes integrar un backend que genere PaymentIntents y provea client secrets.
- Nunca incluyas claves secretas en el cliente. Usa sólo la publishable key en el cliente.

Estructura del proyecto
-----------------------
- app/
  - src/main/AndroidManifest.xml
  - java/com/example/whatsapponlineviewer/
    - MainActivity.kt
    - PaymentActivity.kt
    - CardInputFragment.kt
    - viewmodel/StatusViewModel.kt
    - util/PaymentUtils.kt
    - util/DateUtils.kt
    - BindingAdapters.kt
  - res/layout/*.xml
  - res/values/*.xml
  - res/drawable/ic_whatsapp_phone.xml
  - proguard-rules.pro
- build.gradle
- settings.gradle
- gradle.properties
- .env.example
- README.md

Solución de problemas
---------------------
- Gradle sync falla: Asegúrate que SDK platform 33 está instalado y que Android Studio tiene la ruta correcta al JDK.
- Errores de compilación relacionados con view binding: En este proyecto se incluyeron bindings mínimos como placeholders; en un build real Android Studio generará bindings automáticamente. Si hay conflictos, limpia el proyecto (Build > Clean Project) y luego Rebuild.
- Stripe: Si quieres probar pagos reales, configura un backend y usa tus claves de prueba. No uses claves secretas en el app.
- Tests: Unit tests usan AndroidX test libs. Ejecuta desde Android Studio > Run tests.

Contacto
--------
Este proyecto fue generado automáticamente. Si deseas modificaciones o la exportación como ZIP o subida a GitHub, indícalo y te guiaré.

Licencia
--------
MIT
