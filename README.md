# 🌈 PalleteKT

A powerful, high-performance Kotlin library for creating **smooth, animated gradient text** in Minecraft plugins using the Adventure API.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Paper API](https://img.shields.io/badge/Paper-1.21-333333?style=for-the-badge&logo=paper&logoColor=white)](https://papermc.io)
[![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge&color=7F52FF)](LICENSE)

## ✨ Features

- 🎨 **4 Animation Styles**: WAVE, PULSE, BREATHE, and FLOW
- 🔥 **Preset Gradients**: Rainbow, Fire, and Ocean themes built-in
- 🎯 **Flexible Color Input**: Support for hex codes, RGB, named colors, and java.awt.Color
- ⚡ **Performance Optimized**: Sub-tick interpolation for buttery-smooth animations at 20 TPS
- 🛠️ **Fluent Builder API**: Intuitive, chainable configuration
- 📦 **Kotlin Extensions**: Convenient extension functions for rapid development
- 📝 **Fully Documented**: Comprehensive KDocs and examples

## 🚀 Quick Start

### Installation

```kotlin
import com.palletekt.palletekt.GradientText
import com.palletekt.palletekt.AnimationStyle

// Create a gradient
val gradient = GradientText.builder()
    .text("Hello World!")
    .colors("#FF0000", "#00FF00", "#0000FF")
    .style(AnimationStyle.WAVE)
    .speed(0.08)
    .spread(0.15)
    .build()

// Generate animated component (call every tick)
val component = gradient.generate(tick = currentTick)

// Send to player
player.sendMessage(component)
```

### Using Extension Functions

```kotlin
import com.palletekt.palletekt.rainbow
import com.palletekt.palletekt.fire
import com.palletekt.palletekt.ocean

// Rainbow gradient
player.sendMessage("Welcome!".rainbow(currentTick))

// Fire gradient
player.sendMessage("Server is on fire!".fire(currentTick))

// Ocean gradient
player.sendMessage("Deep waters ahead".ocean(currentTick))
```

### Preset Gradients

```kotlin
// Rainbow
val rainbow = GradientText.rainbow("✨ RAINBOW ✨")
player.sendMessage(rainbow.generate(currentTick))

// Fire
val fire = GradientText.fire("🔥 FIRE 🔥")
player.sendMessage(fire.generate(currentTick))

// Ocean
val ocean = GradientText.ocean("🌊 OCEAN 🌊")
player.sendMessage(ocean.generate(currentTick))
```

## 🎬 Animation Styles

### WAVE

Traditional left-to-right flowing gradient. Smooth wave effect that sweeps across the text horizontally.

**Best for**: Long text, titles, server names

```kotlin
GradientText.builder()
    .text("Wave Animation")
    .colors("#FF0000", "#FFFF00", "#00FF00")
    .style(AnimationStyle.WAVE)
    .build()
```

### PULSE

Gradient emanates from the center outward. Creates a pulsing effect originating from the middle character.

**Best for**: Short words, centered displays, dramatic effects

```kotlin
GradientText.builder()
    .text("Pulse")
    .colors("#FF0080", "#FF00FF", "#8000FF")
    .style(AnimationStyle.PULSE)
    .build()
```

### BREATHE

Gentle pulsing glow like a heartbeat. All characters pulse together with smooth color transitions.

**Best for**: Status indicators, ambient text, gentle animations

```kotlin
GradientText.builder()
    .text("Breathe...")
    .colors("#00FF00", "#00FFFF", "#0000FF")
    .style(AnimationStyle.BREATHE)
    .build()
```

### FLOW

Ultra-smooth sub-tick interpolation. Precise position calculations create an extremely fluid effect.

**Best for**: Premium effects, item names, animated displays

```kotlin
GradientText.builder()
    .text("Flow")
    .colors("#FFD700", "#FF4500", "#8B0000")
    .style(AnimationStyle.FLOW)
    .build()
```

## 📚 API Reference

### GradientText

Core class for gradient text generation.

#### Builder Methods

| Method | Description | Default |
|--------|-------------|---------|
| `text(String)` | Sets the text to apply gradient to | Required |
| `colors(vararg String)` | Sets color palette from hex/named colors | Required |
| `colors(vararg TextColor)` | Sets color palette from TextColor objects | Required |
| `colors(List<TextColor>)` | Sets color palette from list | Required |
| `style(AnimationStyle)` | Sets the animation style | `WAVE` |
| `speed(Double)` | Sets animation speed (0.01-0.20) | `0.08` |
| `spread(Double)` | Sets gradient spread (0.05-0.50) | `0.15` |
| `italic(Boolean)` | Enables/disables italic text | `false` |
| `build()` | Builds the GradientText instance | - |

#### Generation Methods

```kotlin
// Animated (call every tick)
fun generate(tick: Int): Component

// Static (no animation)
fun generateStatic(offset: Double = 0.0): Component
```

### AnimationStyle

Enum defining animation styles:

- `WAVE` - Left-to-right flowing gradient
- `PULSE` - Center-outward emanating gradient
- `BREATHE` - Gentle pulsing glow
- `FLOW` - Ultra-smooth sub-tick interpolation

### ColorConverter

Utility for color format conversion:

```kotlin
// From hex string
ColorConverter.fromHex("#FF5733")
ColorConverter.fromHex("FF5733")

// From RGB
ColorConverter.fromRGB(255, 87, 51)

// From java.awt.Color
ColorConverter.fromAwtColor(Color.RED)

// From named color
ColorConverter.fromName("red")

// Parse (tries all formats)
ColorConverter.parse("#FF5733")
ColorConverter.parse("gold")

// Parse multiple
ColorConverter.parseColors("red", "#00FF00", "blue")
```

### Extension Functions

#### String Extensions

```kotlin
// Create gradient text
"Hello".gradientText("#FF0000", "#00FF00")

// Quick presets
"Rainbow".rainbow(tick)
"Fire".fire(tick)
"Ocean".ocean(tick)
```

#### Component Extensions

```kotlin
component.withGradient(colors, tick, style)
```

## 🎯 Advanced Examples

### Animated Item Names

```kotlin
class MyPlugin : JavaPlugin() {
    private val itemKey = NamespacedKey(this, "animated_item")
    private var tick = 0
    
    private val gradient = GradientText.builder()
        .text("✦ Legendary Sword ✦")
        .colors("#FFD700", "#FFA500", "#FF4500")
        .style(AnimationStyle.FLOW)
        .speed(0.12)
        .spread(0.18)
        .build()
    
    override fun onEnable() {
        // Update items every tick
        server.scheduler.runTaskTimer(this, Runnable {
            tick++
            updateAnimatedItems()
        }, 1L, 1L)
    }
    
    private fun updateAnimatedItems() {
        for (player in server.onlinePlayers) {
            for (item in player.inventory) {
                if (item?.itemMeta?.persistentDataContainer?.has(itemKey, PersistentDataType.BYTE) == true) {
                    item.editMeta { meta ->
                        meta.displayName(gradient.generate(tick))
                    }
                }
            }
        }
    }
}
```

### Custom Color Palettes

```kotlin
// Sunset gradient
val sunset = GradientText.builder()
    .text("Sunset Vibes")
    .colors(
        "#FF512F", "#DD2476", "#F953C6",
        "#B91D73", "#FBB03B", "#FFA500"
    )
    .style(AnimationStyle.WAVE)
    .build()

// Cyberpunk gradient
val cyberpunk = GradientText.builder()
    .text("Cyberpunk 2077")
    .colors("#FF00FF", "#00FFFF", "#FFFF00", "#FF00FF")
    .style(AnimationStyle.PULSE)
    .speed(0.15)
    .build()

// Nature gradient
val nature = GradientText.builder()
    .text("Mother Earth")
    .colorsFromAwt(
        Color(34, 139, 34),   // Forest Green
        Color(50, 205, 50),   // Lime Green
        Color(144, 238, 144), // Light Green
        Color(152, 251, 152)  // Pale Green
    )
    .style(AnimationStyle.BREATHE)
    .build()
```

### Performance Tips

```kotlin
// ❌ DON'T: Create new gradient every tick
fun onTick() {
    val gradient = GradientText.builder()
        .text("Hello")
        .colors("#FF0000", "#00FF00")
        .build()
    player.sendMessage(gradient.generate(tick))
}

// ✅ DO: Create once, reuse many times
class MyPlugin : JavaPlugin() {
    private val gradient = GradientText.builder()
        .text("Hello")
        .colors("#FF0000", "#00FF00")
        .build()
    
    fun onTick() {
        player.sendMessage(gradient.generate(tick))
    }
}

// ✅ DO: Use Persistent Data Container for item identification
// This is MUCH faster than string comparisons
item.itemMeta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
```

## 🎨 Color Formats

GradientText supports multiple color input formats:

```kotlin
// Hex with # prefix
.colors("#FF5733", "#C70039", "#900C3F")

// Hex without prefix
.colors("FF5733", "C70039", "900C3F")

// Hex with 0x prefix
.colors("0xFF5733", "0xC70039", "0x900C3F")

// Named colors (Minecraft/Adventure)
.colors("red", "gold", "yellow", "green", "aqua", "blue")

// RGB values
.colors(TextColor.color(255, 87, 51))

// java.awt.Color
.colorsFromAwt(Color.RED, Color.ORANGE, Color.YELLOW)

// Mixed formats
.addColors("red")           // Named
.addColors("#00FF00")       // Hex
.addColors(TextColor.color(0, 0, 255))  // TextColor
```

## 🔧 Configuration Guide

### Speed Parameter

Controls how fast the gradient animates per tick:

- **0.01 - 0.05**: Slow, gentle flow (meditative)
- **0.05 - 0.10**: Standard speed (balanced)
- **0.10 - 0.20**: Fast, exciting flow (energetic)

```kotlin
.speed(0.08)  // Recommended default
```

### Spread Parameter

Controls how stretched the gradient is across the text:

- **0.05 - 0.10**: Tight, rapidly changing colors (vibrant)
- **0.10 - 0.20**: Balanced gradient spread (standard)
- **0.20 - 0.50**: Wide, gentle transitions (subtle)

```kotlin
.spread(0.15)  // Recommended default
```

## 📦 Building from Source

```bash
# Clone the repository
git clone https://github.com/palletekt/palletekt.git
cd palletekt

# Build with Gradle
./gradlew build

# Build shadow JAR
./gradlew shadowJar

# The JAR will be in build/libs/PalleteKT-1.0.0.jar
```

## 🤝 Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with [Adventure API](https://docs.adventure.kyori.net/) by KyoriPowered
- Powered by [Paper](https://papermc.io/)
- Inspired by the Minecraft plugin development community

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/palletekt/palletekt/issues)
- **Discussions**: [GitHub Discussions](https://github.com/palletekt/palletekt/discussions)

## 🌟 Show Your Support

If you find this library helpful, please consider giving it a star ⭐ on GitHub!

---

Made with ❤️ by [PalleteKT](https://github.com/palletekt)
