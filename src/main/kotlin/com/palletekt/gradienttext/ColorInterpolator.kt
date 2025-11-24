package com.palletekt.gradienttext

import kotlin.math.floor
import net.kyori.adventure.text.format.TextColor

/**
 * Handles color interpolation for gradient effects.
 *
 * Provides various interpolation methods including linear interpolation (lerp), smoother step
 * functions for organic transitions, and cyclic gradient wrapping.
 *
 * This object is the mathematical heart of the gradient engine, responsible for creating smooth
 * color transitions that appear fluid even at 20 TPS.
 */
object ColorInterpolator {

    /**
     * Linearly interpolates between two colors.
     *
     * Performs RGB component-wise interpolation to create a smooth transition from [color1] to
     * [color2] based on factor [t].
     *
     * @param color1 The starting color
     * @param color2 The ending color
     * @param t The interpolation factor (0.0 = color1, 1.0 = color2)
     * @return The interpolated color
     */
    fun interpolate(color1: TextColor, color2: TextColor, t: Double): TextColor {
        val r = lerp(color1.red(), color2.red(), t)
        val g = lerp(color1.green(), color2.green(), t)
        val b = lerp(color1.blue(), color2.blue(), t)

        return TextColor.color(r.coerceIn(0, 255), g.coerceIn(0, 255), b.coerceIn(0, 255))
    }

    /**
     * Interpolates smoothly between multiple colors based on a position value.
     *
     * Uses a smoother step function for more organic-looking transitions. Automatically wraps
     * position for cyclic gradients (negative or > size).
     *
     * @param colors The color palette to interpolate between
     * @param position The position in the gradient (can be any value, will be wrapped)
     * @return The interpolated color at the given position
     */
    fun getColorAtPosition(colors: List<TextColor>, position: Double): TextColor {
        if (colors.isEmpty()) {
            throw IllegalArgumentException("Color list cannot be empty")
        }
        if (colors.size == 1) {
            return colors[0]
        }

        val size = colors.size

        // Wrap position to valid array bounds (Cyclic gradient)
        // This creates the infinite loop effect
        var wrappedPosition = position % size
        if (wrappedPosition < 0) wrappedPosition += size

        // Determine which two colors to interpolate between
        val index1 = floor(wrappedPosition).toInt() % size
        val index2 = (index1 + 1) % size

        // Calculate the decimal portion (how far between the two colors)
        val decimal = wrappedPosition - floor(wrappedPosition)

        // Apply smoother step for more organic transitions
        val smoothedDecimal = smootherStep(decimal)

        return interpolate(colors[index1], colors[index2], smoothedDecimal)
    }

    /**
     * Enhanced smoothing function for more organic color transitions.
     *
     * Uses Ken Perlin's improved smoothstep function (6t^5 - 15t^4 + 10t^3) which has zero first
     * and second-order derivatives at t=0 and t=1, creating buttery-smooth transitions that feel
     * natural.
     *
     * @param t Input value (typically 0.0 to 1.0)
     * @return Smoothed value (0.0 to 1.0)
     */
    fun smootherStep(t: Double): Double {
        // Clamp to 0-1 range
        val clamped = t.coerceIn(0.0, 1.0)
        // Perlin's improved smoothstep: 6t^5 - 15t^4 + 10t^3
        return clamped * clamped * clamped * (clamped * (clamped * 6 - 15) + 10)
    }

    /**
     * Simple linear interpolation between two integer values.
     *
     * @param a Starting value
     * @param b Ending value
     * @param t Interpolation factor (0.0 to 1.0)
     * @return The interpolated value
     */
    private fun lerp(a: Int, b: Int, t: Double): Int {
        return (a + (b - a) * t).toInt()
    }
}
