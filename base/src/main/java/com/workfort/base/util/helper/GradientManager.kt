package com.workfort.base.util.helper

import android.graphics.*
import android.view.View
import android.widget.TextView
import java.util.*

open class GradientManager {
    private val mRandom = Random()

    object GradientType {
        const val RANDOM_LINEAR_GRADIENT = 0
        const val RANDOM_RADIAL_GRADIENT = 1
        const val RANDOM_SWEEP_GRADIENT = 2
    }

    object FilterType {
        const val NORMAL = 0
        const val EMBOSS = 1
        const val DEBOSS = 2
    }

    // Custom method to generate a random LinearGradient
    /*
            public LinearGradient (float x0, float y0, float x1, float y1, int[] colors, float[]
                positions, Shader.TileMode tile)

                Create a shader that draws a linear gradient along a line.

                Parameters
                x0 : The x-coordinate for the start of the gradient line
                y0 : The y-coordinate for the start of the gradient line
                x1 : The x-coordinate for the end of the gradient line
                y1 : The y-coordinate for the end of the gradient line
                colors : The colors to be distributed along the gradient line
                positions : May be null. The relative positions [0..1] of each corresponding color
                    in the colors array. If this is null, the the colors are distributed evenly
                    along the gradient line.
                tile : The Shader tiling mode
        */// Colors to draw the gradient
    // No position defined
    // Shader tiling mode
    // Return the LinearGradient
    private fun getRandomLinearGradient(point: Point): LinearGradient {
        return LinearGradient(
            0f,
            0f,
            point.x.toFloat(),
            point.y.toFloat(),
            getRandomColorArray(), // Colors to draw gradient
            null, // Position is undefined
            getRandomShaderTileMode()
        )
    }

    // Custom method to generate a random RadialGradient
    /*
            public RadialGradient (float centerX, float centerY, float radius, int[] colors,
                float[] stops, Shader.TileMode tileMode)

                Create a shader that draws a radial gradient given the center and radius.

                Parameters
                    centerX : The x-coordinate of the center of the radius
                    centerY : The y-coordinate of the center of the radius
                    radius : Must be positive. The radius of the circle for this gradient.
                    colors : The colors to be distributed between the center and edge of the circle
                    stops : May be null. Valid values are between 0.0f and 1.0f. The relative
                        position of each corresponding color in the colors array. If null, colors
                        are distributed evenly between the center and edge of the circle.
                    tileMode : The Shader tiling mode
        */// Stops position is undefined
    // Shader tiling mode
    // Return the RadialGradient
    private fun getRandomRadialGradient(point: Point): RadialGradient {
        return RadialGradient(
            mRandom.nextInt(point.x).toFloat(),
            mRandom.nextInt(point.y).toFloat(),
            mRandom.nextInt(point.x).toFloat(),
            getRandomColorArray(),
            null,
            getRandomShaderTileMode()
        )
    }

    // Custom method to generate a random SweepGradient
    /*
            public SweepGradient (float cx, float cy, int[] colors, float[] positions)
                A subclass of Shader that draws a sweep gradient around a center point.

                Parameters
                cx : The x-coordinate of the center
                cy : The y-coordinate of the center
                colors : The colors to be distributed between around the center. There must be at
                    least 2 colors in the array.
                positions : May be NULL. The relative position of each corresponding color in the
                    colors array, beginning with 0 and ending with 1.0. If the values are not
                    monotonic, the drawing may produce unexpected results. If positions is NULL,
                    then the colors are automatically spaced evenly.
        */// Return the SweepGradient
    private fun getRandomSweepGradient(point: Point): SweepGradient {
        return SweepGradient(
            mRandom.nextInt(point.x).toFloat(),
            mRandom.nextInt(point.y).toFloat(),
            getRandomColorArray(),
            null
        )
    }

    // Custom method to generate random Shader TileMode
    /*
            Shader
                Shader is the based class for objects that return horizontal spans of colors during
                drawing. A subclass of Shader is installed in a Paint calling paint.setShader(shader).
                After that any object (other than a bitmap) that is drawn with that paint will get
                its color(s) from the shader.
        *//*
                Shader.TileMode : CLAMP
                    replicate the edge color if the shader draws outside of its original bounds
            *//*
                Shader.TileMode : MIRROR
                    repeat the shader's image horizontally and vertically, alternating mirror images
                    so that adjacent images always seam
            *//*
                Shader.TileMode : REPEAT
                    repeat the shader's image horizontally and vertically
            */// Return the random Shader TileMode
    private fun getRandomShaderTileMode(): Shader.TileMode {
        val indicator = mRandom.nextInt(3)
        return when (indicator) {
            0 -> Shader.TileMode.CLAMP
            1 -> Shader.TileMode.MIRROR
            else -> Shader.TileMode.REPEAT
        }
    }

    // Custom method to generate random color array
    // Return the color array
    private fun getRandomColorArray(): IntArray {
        val length = mRandom.nextInt(16 - 3) + 3
        val colors = IntArray(length)
        for (i in 0 until length) {
            colors[i] = getRandomHSVColor()
        }

        return colors
    }

    // Custom method to generate random HSV color
    /*
        Hue is the variation of color
        Hue range 0 to 360

        Saturation is the depth of color
        Range is 0.0 to 1.0 float value
        1.0 is 100% solid color

        Value/Black is the lightness of color
        Range is 0.0 to 1.0 float value
        1.0 is 100% bright less of a color that means black
    */
    private fun getRandomHSVColor(): Int {
        val hue = mRandom.nextInt(361) // Generate a random hue value between 0 to 360
        val saturation = 1.0f // We make the color depth full
        val value = 1.0f // We make a full bright color
        val alpha = 255 // We avoid color transparency

        // Finally, generate and return the color
        return Color.HSVToColor(alpha, floatArrayOf(hue.toFloat(), saturation, value))
    }

    fun setTextColorGradient(gradientType: Int, tv: TextView) {
        tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        val point = Point(tv.measuredWidth, tv.measuredHeight)

        /*
            public void setLayerType (int layerType, Paint paint)
                Specifies the type of layer backing this view. The layer can be LAYER_TYPE_NONE,
                LAYER_TYPE_SOFTWARE or LAYER_TYPE_HARDWARE.

                A layer is associated with an optional Paint instance that controls how the
                layer is composed on screen.

            Parameters
                layerType : The type of layer to use with this view, must be one of
                    LAYER_TYPE_NONE, LAYER_TYPE_SOFTWARE or LAYER_TYPE_HARDWARE
                paint : The paint used to compose the layer. This argument is optional and
                    can be null. It is ignored when the layer type is LAYER_TYPE_NONE
        */
        /*
            public static final int LAYER_TYPE_SOFTWARE
                Indicates that the view has a software layer. A software layer is backed by
                a bitmap and causes the view to be rendered using Android's software rendering
                pipeline, even if hardware acceleration is enabled.
        */
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        /*
            Paint
                The Paint class holds the style and color information about how to draw
                geometries, text and bitmaps.
        */
        /*
            Shader
                Known Direct Subclasses
                BitmapShader, ComposeShader, LinearGradient, RadialGradient, SweepGradient

                Shader is the based class for objects that return horizontal spans of colors
                during drawing. A subclass of Shader is installed in a Paint calling
                paint.setShader(shader). After that any object (other than a bitmap) that
                is drawn with that paint will get its color(s) from the shader.
        */
        tv.paint.shader = when (gradientType) {
            GradientType.RANDOM_LINEAR_GRADIENT -> getRandomLinearGradient(point)
            GradientType.RANDOM_RADIAL_GRADIENT -> getRandomRadialGradient(point)
            GradientType.RANDOM_SWEEP_GRADIENT -> getRandomSweepGradient(point)
            else -> getRandomLinearGradient(point)
        }
    }

    fun setTextColorFilter(filterType: Int, tv: TextView) {
        tv.paint.maskFilter = when(filterType) {
            FilterType.NORMAL -> null
            FilterType.EMBOSS -> {
                /*
                    MaskFilter
                        MaskFilter is the base class for object that perform transformations on an
                        alpha-channel mask before drawing it. A subclass of MaskFilter may be installed
                        into a Paint. Blur and emboss are implemented as subclasses of MaskFilter.
                */
                /*
                    public EmbossMaskFilter (float[] direction, float ambient, float specular,
                        float blurRadius)

                    Create an emboss maskfilter

                    Parameters
                        direction : array of 3 scalars [x, y, z] specifying the direction of the
                            light source
                        ambient : 0...1 amount of ambient light
                        specular : coefficient for specular highlights (e.g. 8)
                        blurRadius : amount to blur before applying lighting (e.g. 3)
                    Returns
                        the emboss maskfilter
                */
                val embossFilter = EmbossMaskFilter(
                    floatArrayOf(1f, 5f, 1f), // direction of the light source
                    0.8f, // ambient light between 0 to 1
                    8f, // specular highlights
                    7f // blur before applying lighting
                )
                // Apply the emboss mask filter
                embossFilter
            }
            FilterType.DEBOSS -> {
                val debossFilter = EmbossMaskFilter(
                    floatArrayOf(0f, -1f, 0.5f), // direction of the light source
                    0.8f, // ambient light between 0 to 1
                    13f, // specular highlights
                    7.0f // blur before applying lighting
                )
                // Apply the deboss mask filter
                debossFilter
            }
            else -> {
                null
            }
        }
    }
}