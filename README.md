# 🔴🟢 PulseMaster (Simón Dice) - Android Kotlin

Juego de memoria visual y auditiva estilo "Simón Dice" desarrollado en **Kotlin** para Android. El proyecto se centra en el manejo de corrutinas para la secuenciación del juego, animaciones visuales y gestión de recursos multimedia.

## 📱 Demostración

Aquí puedes ver la fluidez de las animaciones, el fondo en movimiento y la sincronización de sonidos.

[PulseMaster.webm](https://github.com/user-attachments/assets/31d24c8b-1d2a-4448-a0dc-0d80c3d05e7c)


## 🛠️ Tecnologías y Conceptos Clave

Este proyecto académico implementa conceptos fundamentales del desarrollo moderno en Android:

* **Lenguaje:** Kotlin.
* **Concurrencia:** Uso de **Kotlin Coroutines** (`lifecycleScope`, `delay`) para gestionar los turnos y evitar bloqueos en el hilo principal (UI Thread).
* **Animaciones:** Implementación de `ObjectAnimator` y `AnimatorSet` para el feedback visual de los pulsadores.
* **Multimedia:** Integración de `VideoView` para fondos y `MediaPlayer` para efectos sonoros.
* **Ciclo de Vida:** Gestión de cambios de configuración y paso de datos con `Intents` y `Bundles`.

## 📋 Requisitos del Entorno

⚠️ **Importante:** Este proyecto utiliza una configuración de SDK muy reciente. Para compilarlo correctamente, asegúrate de cumplir estos requisitos:

* **Android Studio:** Versión Koala / Ladybug o superior (Compatible con API 36).
* **Versión de SDK (API Level):**
    * **Mínimo (`minSdk`):** API 33 (Android 13 Tiramisu). *La app no funcionará en dispositivos anteriores.*
    * **Objetivo (`targetSdk`):** API 36 (Android 16 Baklava).
* **Java JDK:** Versión 17 o superior.

## ⚙️ Estructura del Proyecto

* `PantallaJuego.kt`: Lógica principal del juego y secuenciación.
* `Ranking.kt`: Singleton para gestión temporal de puntuaciones.
* `Clasificacion.kt`: Visualización de resultados.

## 📝 Nota Académica

Este proyecto corresponde a la primera evaluación de Desarrollo de Aplicaciones Móviles.

* **Persistencia:** El ranking es volátil (se reinicia al cerrar la app); la implementación de Bases de Datos (Room/SQLite) se realizará en evaluaciones futuras.
* **Listas:** Se utiliza una gestión manual de vistas (TextViews), previo al aprendizaje del componente `RecyclerView`.
* **Diseño de Interfaz:** La aplicación ha sido desarrollada para funcionar **exclusivamente en orientación vertical** (Portrait Mode). Se ha bloqueado la rotación intencionadamente para mantener la coherencia de las animaciones.

## 🚀 Cómo ejecutarlo

1.  Clonar el repositorio o descargar el código.
2.  Abrir en **Android Studio**.
3.  Esperar a que Gradle sincronice las dependencias.
4.  Ejecutar en un emulador (Pixel con API 33+) o dispositivo físico compatible.

---
*Desarrollado como parte del portafolio de DAM.*
