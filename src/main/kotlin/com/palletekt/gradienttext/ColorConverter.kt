package com.palletekt.gradienttext

import java.awt.Color
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

/**
 * Utility object for converting various color formats to Adventure API's [TextColor].
 *
 * Supports conversion from:
 * - Hex strings (with or without # prefix)
 * - RGB integer values
 * - java.awt.Color objects
 * - Named color strings
 */
object ColorConverter {

    /**
     * Converts a hexadecimal color string to [TextColor].
     *
     * Accepts formats:
     * - `#RRGGBB` (e.g., `#FF5733`)
     * - `RRGGBB` (e.g., `FF5733`)
     * - `0xRRGGBB` (e.g., `0xFF5733`)
     *
     * @param hex The hex color string
     * @return The corresponding TextColor
     * @throws IllegalArgumentException if the hex string is invalid
     */
    fun fromHex(hex: String): TextColor {
        val cleaned = hex.trim().removePrefix("#").removePrefix("0x").removePrefix("0X")

        require(cleaned.length == 6) {
            "Invalid hex color format: $hex. Expected format: #RRGGBB or RRGGBB"
        }

        return try {
            val rgb = cleaned.toInt(16)
            TextColor.color(rgb)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid hex color value: $hex", e)
        }
    }

    /**
     * Creates a [TextColor] from RGB components.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return The corresponding TextColor
     * @throws IllegalArgumentException if any component is out of range
     */
    fun fromRGB(r: Int, g: Int, b: Int): TextColor {
        require(r in 0..255) { "Red component out of range: $r (must be 0-255)" }
        require(g in 0..255) { "Green component out of range: $g (must be 0-255)" }
        require(b in 0..255) { "Blue component out of range: $b (must be 0-255)" }

        return TextColor.color(r, g, b)
    }

    /**
     * Converts a [java.awt.Color] to [TextColor].
     *
     * Useful when integrating with other libraries or systems that use AWT colors.
     *
     * @param color The AWT color to convert
     * @return The corresponding TextColor
     */
    fun fromAwtColor(color: Color): TextColor {
        return TextColor.color(color.red, color.green, color.blue)
    }

    /**
     * Converts a named color string to [TextColor].
     *
     * Supports all Minecraft named colors (case-insensitive):
     * - Basic: white, black, red, green, blue, yellow, etc.
     * - Extended: gold, aqua, dark_red, light_purple, etc.
     *
     * @param name The color name
     * @return The corresponding TextColor, or null if name is not recognized
     */
    fun fromName(name: String): TextColor? {
        return NamedTextColor.NAMES.value(name.lowercase())
    }

    /**
     * Parses a color from various string formats.
     *
     * Attempts to parse in order:
     * 1. Named color (e.g., "red", "gold")
     * 2. Hex color (e.g., "#FF5733", "FF5733")
     * 3. If all fail, throws exception
     *
     * @param colorString The color string to parse
     * @return The parsed TextColor
     * @throws IllegalArgumentException if the format is not recognized
     */
    fun parse(colorString: String): TextColor {
        // Try named color first
        fromName(colorString)?.let {
            return it
        }

        // Try hex format
        if (colorString.startsWith("#") || colorString.startsWith("0x") || colorString.length == 6
        ) {
            return try {
                fromHex(colorString)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid color format: $colorString", e)
            }
        }

        throw IllegalArgumentException(
                "Unrecognized color format: $colorString. " +
                        "Use named colors (e.g., 'red') or hex format (e.g., '#FF5733')"
        )
    }

    /**
     * Converts multiple color strings to a list of [TextColor].
     *
     * Convenience method for parsing entire color palettes.
     *
     * @param colorStrings Variable number of color strings
     * @return List of parsed TextColors
     * @throws IllegalArgumentException if any color string is invalid
     */
    fun parseColors(vararg colorStrings: String): List<TextColor> {
        return colorStrings.map { parse(it) }
    }
}

/** Extension function to convert [java.awt.Color] to [TextColor]. */
fun Color.toTextColor(): TextColor = ColorConverter.fromAwtColor(this)
