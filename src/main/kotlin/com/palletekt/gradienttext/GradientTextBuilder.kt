package com.palletekt.gradienttext

import java.awt.Color
import net.kyori.adventure.text.format.TextColor

/**
 * Fluent builder for creating [GradientText] instances.
 *
 * Provides a chainable, type-safe API for configuring gradient text effects. All methods return
 * `this` to allow method chaining.
 *
 * **Example**:
 * ```kotlin
 * val gradient = GradientText.builder()
 *     .text("Hello World")
 *     .colors("#FF0000", "#00FF00", "#0000FF")
 *     .style(AnimationStyle.WAVE)
 *     .speed(0.08)
 *     .spread(0.15)
 *     .italic(false)
 *     .build()
 * ```
 */
class GradientTextBuilder {
    private var text: String = ""
    private var colors: MutableList<TextColor> = mutableListOf()
    private var style: AnimationStyle = AnimationStyle.WAVE
    private var speed: Double = 0.08
    private var spread: Double = 0.15
    private var italic: Boolean = false

    /**
     * Sets the text to apply gradient to.
     *
     * @param text The text string
     * @return This builder for chaining
     */
    fun text(text: String): GradientTextBuilder {
        this.text = text
        return this
    }

    /**
     * Sets the color palette from hex strings.
     *
     * Accepts various formats:
     * - `#RRGGBB` (e.g., `#FF5733`)
     * - `RRGGBB` (e.g., `FF5733`)
     * - Named colors (e.g., `red`, `gold`)
     *
     * @param colorStrings Variable number of color strings
     * @return This builder for chaining
     */
    fun colors(vararg colorStrings: String): GradientTextBuilder {
        this.colors = ColorConverter.parseColors(*colorStrings).toMutableList()
        return this
    }

    /**
     * Sets the color palette from TextColor objects.
     *
     * @param colors Variable number of TextColor objects
     * @return This builder for chaining
     */
    fun colors(vararg colors: TextColor): GradientTextBuilder {
        this.colors = colors.toMutableList()
        return this
    }

    /**
     * Sets the color palette from a list of TextColor objects.
     *
     * @param colors List of TextColor objects
     * @return This builder for chaining
     */
    fun colors(colors: List<TextColor>): GradientTextBuilder {
        this.colors = colors.toMutableList()
        return this
    }

    /**
     * Sets the color palette from java.awt.Color objects.
     *
     * @param colors Variable number of AWT Color objects
     * @return This builder for chaining
     */
    fun colorsFromAwt(vararg colors: Color): GradientTextBuilder {
        this.colors = colors.map { it.toTextColor() }.toMutableList()
        return this
    }

    /**
     * Adds additional colors to the palette.
     *
     * @param colorStrings Color strings to add
     * @return This builder for chaining
     */
    fun addColors(vararg colorStrings: String): GradientTextBuilder {
        this.colors.addAll(ColorConverter.parseColors(*colorStrings))
        return this
    }

    /**
     * Adds additional TextColor objects to the palette.
     *
     * @param colors TextColor objects to add
     * @return This builder for chaining
     */
    fun addColors(vararg colors: TextColor): GradientTextBuilder {
        this.colors.addAll(colors)
        return this
    }

    /**
     * Sets the animation style.
     *
     * @param style The AnimationStyle to use
     * @return This builder for chaining
     */
    fun style(style: AnimationStyle): GradientTextBuilder {
        this.style = style
        return this
    }

    /**
     * Sets the animation speed.
     *
     * - Smaller values (0.01-0.05): Slow, gentle flow
     * - Medium values (0.05-0.10): Standard animation speed
     * - Larger values (0.10-0.20): Fast, exciting flow
     *
     * @param speed The speed multiplier (must be positive)
     * @return This builder for chaining
     */
    fun speed(speed: Double): GradientTextBuilder {
        require(speed > 0) { "Speed must be positive" }
        this.speed = speed
        return this
    }

    /**
     * Sets the gradient spread.
     *
     * - Smaller values (0.05-0.10): Tight, rapidly changing colors
     * - Medium values (0.10-0.20): Balanced gradient spread
     * - Larger values (0.20-0.50): Wide, gentle color transitions
     *
     * @param spread The spread factor (must be positive)
     * @return This builder for chaining
     */
    fun spread(spread: Double): GradientTextBuilder {
        require(spread > 0) { "Spread must be positive" }
        this.spread = spread
        return this
    }

    /**
     * Sets whether the text should be italic.
     *
     * @param italic True for italic, false for normal
     * @return This builder for chaining
     */
    fun italic(italic: Boolean): GradientTextBuilder {
        this.italic = italic
        return this
    }

    /**
     * Builds the [GradientText] instance.
     *
     * @return A configured GradientText instance
     * @throws IllegalArgumentException if text is empty or fewer than 2 colors provided
     */
    fun build(): GradientText {
        require(text.isNotEmpty()) { "Text must be set before building" }
        require(colors.size >= 2) { "At least 2 colors required (currently: ${colors.size})" }

        return GradientText(
                text = text,
                colors = colors.toList(),
                style = style,
                speed = speed,
                spread = spread,
                italic = italic
        )
    }
}
