<p align="center">
  <img src="https://img.icons8.com/color/96/000000/money-bag.png" alt="Control de Gastos Logo"/>
</p>

<h1 align="center">💰 Control de Gastos</h1>

<p align="center">
  <strong>Tu compañero inteligente para manejar tus finanzas personales</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design"/>
  <img src="https://img.shields.io/badge/SDK-33%2B-blue?style=for-the-badge" alt="Min SDK"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-1.0-green?style=flat-square" alt="Version"/>
  <img src="https://img.shields.io/badge/license-MIT-blue?style=flat-square" alt="License"/>
  <img src="https://img.shields.io/badge/PRs-welcome-brightgreen?style=flat-square" alt="PRs Welcome"/>
</p>

---

## 📱 Acerca del Proyecto

**Control de Gastos** es una aplicación Android moderna y elegante diseñada para ayudarte a llevar un registro detallado de tus gastos diarios y alcanzar tus objetivos de ahorro. Con una interfaz intuitiva y funcionalidades potentes, gestionar tus finanzas nunca fue tan fácil.

---

## ✨ Características Principales

| Característica | Descripción |
|----------------|-------------|
| 📊 **Dashboard Inteligente** | Visualiza un resumen completo de tus finanzas en un solo vistazo |
| 💸 **Registro de Gastos** | Añade, edita y elimina gastos con categorías personalizadas |
| 🎯 **Objetivos de Ahorro** | Establece metas financieras y monitorea tu progreso |
| 📁 **Categorías Predefinidas** | Alimentación, Transporte, Entretenimiento, Compras, Salud, Educación, Servicios y más |
| 🌙 **Modo Oscuro** | Interfaz adaptable para uso cómodo en cualquier momento |
| 📱 **Material Design 3** | Diseño moderno siguiendo las últimas guías de Google |

---

## 🛠️ Stack Tecnológico

<table>
  <tr>
    <td align="center"><strong>Lenguaje</strong></td>
    <td align="center"><strong>Base de Datos</strong></td>
    <td align="center"><strong>Arquitectura</strong></td>
    <td align="center"><strong>UI</strong></td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://img.icons8.com/color/48/000000/kotlin.png" width="40"/><br/>
      Kotlin
    </td>
    <td align="center">
      <img src="https://img.icons8.com/color/48/000000/database-restore.png" width="40"/><br/>
      Room
    </td>
    <td align="center">
      <img src="https://img.icons8.com/color/48/000000/module.png" width="40"/><br/>
      MVVM
    </td>
    <td align="center">
      <img src="https://img.icons8.com/color/48/000000/material-ui.png" width="40"/><br/>
      Material 3
    </td>
  </tr>
</table>

### 📦 Dependencias Principales

```gradle
// Android Jetpack
- AndroidX Core KTX
- AppCompat
- ConstraintLayout
- Navigation Component

// Base de Datos
- Room (con Coroutines)

// Arquitectura
- ViewModel
- LiveData
- Coroutines
```

---

## 📂 Estructura del Proyecto

```
📦 app
├── 📁 src/main
│   ├── 📁 java/com/example/aprendiendo
│   │   ├── 📄 MainActivity.kt
│   │   ├── 📁 data
│   │   │   ├── 📁 converters    # Conversores de Room
│   │   │   ├── 📁 dao           # Data Access Objects
│   │   │   ├── 📁 database      # Configuración de Room
│   │   │   ├── 📁 entities      # Modelos de datos
│   │   │   └── 📁 repository    # Repositorios
│   │   └── 📁 ui
│   │       ├── 📁 adapters      # RecyclerView Adapters
│   │       ├── 📁 dialogs       # Diálogos personalizados
│   │       ├── 📁 fragments     # Fragmentos de la app
│   │       └── 📁 viewmodel     # ViewModels
│   └── 📁 res
│       ├── 📁 layout            # Layouts XML
│       ├── 📁 navigation        # Gráficos de navegación
│       └── 📁 values            # Recursos (strings, colors, etc.)
└── 📄 build.gradle.kts
```

---

## 🚀 Instalación

### Prerrequisitos

- Android Studio Arctic Fox o superior
- JDK 17 o superior
- Android SDK 33+

### Pasos de instalación

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/JHSprogramador/Monyer.git
   ```

2. **Abre el proyecto en Android Studio**
   ```
   File > Open > Selecciona la carpeta del proyecto
   ```

3. **Sincroniza las dependencias de Gradle**
   ```
   Android Studio lo hará automáticamente o haz clic en "Sync Now"
   ```

4. **Ejecuta la aplicación**
   ```
   Run > Run 'app' o presiona Shift + F10
   ```

---

## 📸 Capturas de Pantalla

<p align="center">
  <i>🚧 Próximamente se agregarán capturas de pantalla 🚧</i>
</p>

<!--
<p align="center">
  <img src="screenshots/dashboard.png" width="200" alt="Dashboard"/>
  <img src="screenshots/expenses.png" width="200" alt="Gastos"/>
  <img src="screenshots/goals.png" width="200" alt="Objetivos"/>
</p>
-->

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Si deseas contribuir al proyecto:

1. 🍴 Haz un Fork del proyecto
2. 🌿 Crea tu rama de características (`git checkout -b feature/NuevaCaracteristica`)
3. 💾 Haz commit de tus cambios (`git commit -m 'Añadir nueva característica'`)
4. 📤 Haz Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. 🔃 Abre un Pull Request

---

## 📋 Próximas Mejoras

- [ ] 📊 Gráficos y estadísticas detalladas
- [ ] 🔔 Notificaciones de recordatorio
- [ ] ☁️ Respaldo en la nube
- [ ] 📤 Exportar datos a CSV/PDF
- [ ] 🌐 Soporte multi-idioma
- [ ] 💳 Integración con cuentas bancarias

---

## 👨‍💻 Autor

<p align="center">
  <strong>JHSprogramador</strong><br/>
  <a href="https://github.com/JHSprogramador">
    <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
  </a>
</p>

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

---

<p align="center">
  <strong>⭐ Si te gusta este proyecto, ¡no olvides darle una estrella! ⭐</strong>
</p>

<p align="center">
  Hecho con ❤️ y ☕ en Android
</p>
