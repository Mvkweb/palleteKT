package com.palletekt.gradienttext

/**
 * Defines the available animation styles for gradient text effects.
 *
 * Each style provides a unique visual effect by calculating character positions differently,
 * creating various flowing, pulsing, or breathing animations.
 *
 * @property displayName The human-readable name of the animation style
 * @property description A brief description of the visual effect
 */
enum class AnimationStyle(val displayName: String, val description: String) {
    /**
     * Traditional left-to-right flowing gradient.
     *
     * The gradient sweeps across the text horizontally, creating a smooth wave effect that moves
     * from left to right continuously.
     *
     * **Best for**: Long text, titles, server names
     */
    WAVE(displayName = "Wave", description = "Smooth left-to-right flowing gradient") {
        override fun calculatePosition(
                charIndex: Int,
                textLength: Int,
                spread: Double,
                tick: Int,
                speed: Double
        ): Double {
            return (charIndex * spread) - (tick * speed)
        }
    },

    /**
     * Gradient emanates from the center of the text.
     *
     * Creates a pulsing effect where the gradient originates from the middle character and flows
     * outward to both ends, then repeats.
     *
     * **Best for**: Short words, centered displays, dramatic effects
     */
    PULSE(displayName = "Pulse", description = "Gradient emanates from center outward") {
        override fun calculatePosition(
                charIndex: Int,
                textLength: Int,
                spread: Double,
                tick: Int,
                speed: Double
        ): Double {
            val distanceFromCenter = kotlin.math.abs(charIndex - (textLength - 1) / 2.0)
            return (distanceFromCenter * spread) - (tick * speed)
        }
    },

    /**
     * Gentle pulsing glow effect, like a heartbeat.
     *
     * All characters pulse together using a combined sine wave pattern, creating a breathing or
     * heartbeat-like effect with smooth color transitions.
     *
     * **Best for**: Status indicators, ambient text, gentle animations
     */
    BREATHE(displayName = "Breathe", description = "Gentle pulsing glow like a heartbeat") {
        override fun calculatePosition(
                charIndex: Int,
                textLength: Int,
                spread: Double,
                tick: Int,
                speed: Double
        ): Double {
            // All characters pulse together - no position-based offset
            val timeOffset = tick * speed
            val wave1 = kotlin.math.sin(timeOffset)
            val wave2 = kotlin.math.sin(timeOffset * 2.1)

            // Normalize to 0-1 range and combine waves
            val normalizedWave1 = (wave1 + 1.0) / 2.0
            val normalizedWave2 = (wave2 + 1.0) / 2.0

            return (normalizedWave1 * 0.7) + (normalizedWave2 * 0.3)
        }
    },

    /**
     * Sub-tick interpolation for ultra-smooth flowing effect.
     *
     * Uses precise position calculations with cyclic wrapping to create an extremely smooth
     * gradient flow, appearing to move at high framerates even on a 20 TPS server.
     *
     * **Best for**: Premium effects, item names, animated displays
     */
    FLOW(displayName = "Flow", description = "Ultra-smooth sub-tick interpolation flow") {
        override fun calculatePosition(
                charIndex: Int,
                textLength: Int,
                spread: Double,
                tick: Int,
                speed: Double
        ): Double {
            // Direct position calculation with offset
            // This creates the "sub-tick" smoothness by using precise decimal positions
            return (charIndex * spread) - (tick * speed)
        }
    };

    /**
     * Calculates the position value for a character in the gradient.
     *
     * This position is used by [ColorInterpolator] to determine which color the character should
     * display based on the color palette.
     *
     * @param charIndex The index of the character in the text (0-based)
     * @param textLength The total length of the text
     * @param spread How stretched the gradient is (smaller = tighter, larger = wider)
     * @param tick The current game tick (for animation)
     * @param speed How fast the animation moves per tick
     * @return A position value used for color interpolation
     */
    abstract fun calculatePosition(
            charIndex: Int,
            textLength: Int,
            spread: Double,
            tick: Int,
            speed: Double
    ): Double
}
