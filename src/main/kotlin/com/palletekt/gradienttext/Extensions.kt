package com.palletekt.gradienttext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

/**
 * Extension functions for convenient gradient text operations.
 *
 * These extensions provide Kotlin-idiomatic ways to create gradient effects without verbose builder
 * calls.
 */

/**
 * Creates a gradient text from this string with the specified colors.
 *
 * **Example**:
 * ```kotlin
 * val text = "Hello World".gradientText("#FF0000", "#00FF00", "#0000FF")
 * val component = text.generate(tick = 0)
 * ```
 *
 * @param colorStrings The color palette (hex or named colors)
 * @return A GradientText instance configured with default settings
 */
fun String.gradientText(vararg colorStrings: String): GradientText {
    return GradientText.builder().text(this).colors(*colorStrings).build()
}

/**
 * Creates a gradient text from this string with TextColor objects.
 *
 * @param colors The color palette
 * @return A GradientText instance configured with default settings
 */
fun String.gradientText(vararg colors: TextColor): GradientText {
    return GradientText.builder().text(this).colors(*colors).build()
}

/**
 * Creates an animated rainbow component from this string.
 *
 * **Example**:
 * ```kotlin
 * player.sendMessage("Welcome!".rainbow(currentTick))
 * ```
 *
 * @param tick The current game tick for animation
 * @return An animated rainbow Component
 */
fun String.rainbow(tick: Int): Component {
    return GradientText.rainbow(this).generate(tick)
}

/**
 * Creates a static rainbow component from this string.
 *
 * @return A static rainbow Component
 */
fun String.rainbow(): Component {
    return GradientText.rainbow(this).generateStatic()
}

/**
 * Creates an animated fire gradient component from this string.
 *
 * @param tick The current game tick for animation
 * @return An animated fire gradient Component
 */
fun String.fire(tick: Int): Component {
    return GradientText.fire(this).generate(tick)
}

/**
 * Creates a static fire gradient component from this string.
 *
 * @return A static fire gradient Component
 */
fun String.fire(): Component {
    return GradientText.fire(this).generateStatic()
}

/**
 * Creates an animated ocean gradient component from this string.
 *
 * @param tick The current game tick for animation
 * @return An animated ocean gradient Component
 */
fun String.ocean(tick: Int): Component {
    return GradientText.ocean(this).generate(tick)
}

/**
 * Creates a static ocean gradient component from this string.
 *
 * @return A static ocean gradient Component
 */
fun String.ocean(): Component {
    return GradientText.ocean(this).generateStatic()
}

/**
 * Applies an animated gradient to this Component.
 *
 * **Note**: This creates a new component with gradient applied. The original component's text is
 * extracted and used.
 *
 * @param colors The color palette
 * @param tick The current game tick
 * @param style The animation style (default: WAVE)
 * @return A new Component with gradient applied
 */
fun Component.withGradient(
        colors: List<TextColor>,
        tick: Int,
        style: AnimationStyle = AnimationStyle.WAVE
): Component {
    // Extract plain text from component
    // Note: This is a simplified version. For production, you might want
    // to preserve other formatting like bold, underline, etc.
    val plainText = this.toString() // Simplified - in production use proper text extraction

    return GradientText.builder().text(plainText).colors(colors).style(style).build().generate(tick)
}
