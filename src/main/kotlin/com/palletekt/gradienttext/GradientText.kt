package com.palletekt.gradienttext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

/**
 * Core class for generating gradient text with animations.
 *
 * This class encapsulates all the configuration needed to create beautiful, smooth gradient text
 * effects in Minecraft. Once configured, it can generate animated or static gradient components.
 *
 * **Performance Note**: Creating a [GradientText] instance is lightweight. The actual component
 * generation happens in [generate], allowing you to create the configuration once and generate
 * components many times efficiently.
 *
 * @property text The text to apply the gradient to
 * @property colors The color palette for the gradient (at least 2 colors required)
 * @property style The animation style to use
 * @property speed How fast the animation moves per tick (default: 0.08)
 * @property spread How stretched the gradient is across the text (default: 0.15)
 * @property italic Whether to apply italic formatting (default: false)
 *
 * @see GradientTextBuilder For a fluent API to create instances
 */
data class GradientText(
        val text: String,
        val colors: List<TextColor>,
        val style: AnimationStyle = AnimationStyle.WAVE,
        val speed: Double = 0.08,
        val spread: Double = 0.15,
        val italic: Boolean = false
) {
    init {
        require(text.isNotEmpty()) { "Text cannot be empty" }
        require(colors.size >= 2) {
            "At least 2 colors required for gradient (provided: ${colors.size})"
        }
        require(speed > 0) { "Speed must be positive (provided: $speed)" }
        require(spread > 0) { "Spread must be positive (provided: $spread)" }
    }

    /**
     * Generates an animated gradient text component for the current tick.
     *
     * This method should be called every tick (or frame) with the current game tick to create
     * smooth animations.
     *
     * **Performance**: This is optimized for per-tick execution. The color calculations use
     * sub-tick interpolation to appear smooth even at 20 TPS.
     *
     * @param tick The current game tick (from server or timer)
     * @return An animated Adventure Component ready to display
     */
    fun generate(tick: Int): Component {
        val builder = Component.text()

        // Apply italic setting globally
        builder.decoration(TextDecoration.ITALIC, italic)

        // Process each character
        for ((index, char) in text.withIndex()) {
            val position =
                    style.calculatePosition(
                            charIndex = index,
                            textLength = text.length,
                            spread = spread,
                            tick = tick,
                            speed = speed
                    )

            val color = ColorInterpolator.getColorAtPosition(colors, position)

            builder.append(Component.text(char, color))
        }

        return builder.build()
    }

    /**
     * Generates a static gradient text component (no animation).
     *
     * Useful for:
     * - Preview/testing
     * - One-time displays
     * - Screenshots
     * - When you want the gradient look without animation
     *
     * @param offset Optional offset to shift the gradient (default: 0.0)
     * @return A static Adventure Component with gradient applied
     */
    fun generateStatic(offset: Double = 0.0): Component {
        val builder = Component.text()

        builder.decoration(TextDecoration.ITALIC, italic)

        for ((index, char) in text.withIndex()) {
            val position = (index * spread) - offset
            val color = ColorInterpolator.getColorAtPosition(colors, position)

            builder.append(Component.text(char, color))
        }

        return builder.build()
    }

    /**
     * Creates a builder pre-configured with this instance's values.
     *
     * Useful for creating variations of an existing gradient configuration.
     *
     * @return A new GradientTextBuilder with this instance's settings
     */
    fun toBuilder(): GradientTextBuilder {
        return GradientTextBuilder()
                .text(text)
                .colors(colors)
                .style(style)
                .speed(speed)
                .spread(spread)
                .italic(italic)
    }

    companion object {
        /**
         * Creates a new builder for configuring gradient text.
         *
         * This is the recommended way to create [GradientText] instances.
         *
         * @return A new GradientTextBuilder
         */
        @JvmStatic fun builder(): GradientTextBuilder = GradientTextBuilder()

        /**
         * Creates a simple rainbow gradient (classic Minecraft colors).
         *
         * @param text The text to apply rainbow to
         * @return A GradientText configured with rainbow colors
         */
        @JvmStatic
        fun rainbow(text: String): GradientText {
            val rainbowColors =
                    ColorConverter.parseColors(
                            "red",
                            "gold",
                            "yellow",
                            "green",
                            "aqua",
                            "blue",
                            "light_purple"
                    )
            return GradientText(
                    text = text,
                    colors = rainbowColors,
                    style = AnimationStyle.WAVE,
                    speed = 0.1,
                    spread = 0.2
            )
        }

        /**
         * Creates a fire-themed gradient (red to yellow).
         *
         * @param text The text to apply fire gradient to
         * @return A GradientText configured with fire colors
         */
        @JvmStatic
        fun fire(text: String): GradientText {
            val fireColors =
                    ColorConverter.parseColors(
                            "#FF0000",
                            "#FF4500",
                            "#FF8C00",
                            "#FFD700",
                            "#FFFF00"
                    )
            return GradientText(
                    text = text,
                    colors = fireColors,
                    style = AnimationStyle.FLOW,
                    speed = 0.12,
                    spread = 0.18
            )
        }

        /**
         * Creates an ocean-themed gradient (blue to cyan).
         *
         * @param text The text to apply ocean gradient to
         * @return A GradientText configured with ocean colors
         */
        @JvmStatic
        fun ocean(text: String): GradientText {
            val oceanColors =
                    ColorConverter.parseColors(
                            "#000080",
                            "#0000CD",
                            "#1E90FF",
                            "#00BFFF",
                            "#00FFFF"
                    )
            return GradientText(
                    text = text,
                    colors = oceanColors,
                    style = AnimationStyle.WAVE,
                    speed = 0.06,
                    spread = 0.12
            )
        }
    }
}
