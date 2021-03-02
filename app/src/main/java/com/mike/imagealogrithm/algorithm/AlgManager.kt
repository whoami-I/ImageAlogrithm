package com.mike.imagealogrithm.algorithm

import android.graphics.Bitmap
import java.nio.channels.FileLock
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

object AlgManager {
    /**
     * h: 0-360
     * s: 0.0-1.0
     * v: 0-255
     */
    fun hsv2rgb(h: Float, s: Float, v: Float): Int {
        var R: Int = 0
        var G: Int = 0
        var B: Int = 0
        if (s.equals(0)) {
            R = v.toInt()
            G = v.toInt()
            B = v.toInt()
        } else {
            var hh: Float = h / 60;

            val i: Int = floor(hh.toDouble()).toInt();
            val f: Float = hh - i
            val p = v * (1 - s)

            val q = v * (1 - s * f)

            val t = v * (1 - s * (1 - f))

            when (i) {
                0 -> {
//                    makeColor(v, t, p)
                    R = v.toInt()
                    G = t.toInt()
                    B = p.toInt()
                }

                1 -> {
//                    makeColor(q, v, p)
                    R = q.toInt()
                    G = v.toInt()
                    B = p.toInt()
                }


                2 -> {
//                    makeColor(p, v, t)
                    R = p.toInt()
                    G = v.toInt()
                    B = t.toInt()
                }

                3 -> {
//                    makeColor(p, q, v)
                    R = p.toInt()
                    G = q.toInt()
                    B = v.toInt()
                }

                4 -> {
//                    makeColor(t, p, v)
                    R = t.toInt()
                    G = p.toInt()
                    B = v.toInt()
                }
                else -> {
//                    makeColor(v.toInt(), p, q);
                    R = v.toInt()
                    G = p.toInt()
                    B = q.toInt()
                }
            }
        }
        return makeColor(R, G, B)
    }

    fun makeColor(r: Int, g: Int, b: Int): Int {
        return ((r and 0xFF) shl 16) or ((g and 0xFF) shl 8) or (b and 0xFF)
    }

    /**
     * h: 0-360
     * s: 0.0-1.0
     * l: 0.0-1.0
     */
    fun hsl2rgb(h: Float, s: Float, l: Float): Int {
        var r = 0f
        var g = 0f
        var b = 0f
        if (s.equals(0.0f)) {
            r = l
            g = l
            b = l
        } else {
            var q = 0.0f
            var p = 0.0f
            q = if (l < 1 / 2.0f) l * (1 + s) else {
                l + s - l * s
            }
            p = 2 * l - q
            var hh = h / 360
            var tr = hh + 1 / 3.0f
            var tg = hh
            var tb = hh - 1 / 3.0f
            var tc = arrayOf(tr, tg, tb)
            for (i in tc.indices) {
                var c = tc[i]
                if (c < 0) {
                    c += 1.0f
                } else if (c > 1.0f) {
                    c -= 1.0f
                }
                var color = 0.0f
                if (c < 1 / 6.0f) {
                    color = p + (q - p) * 6 * c
                } else if (c >= 1 / 6.0f && c < 1 / 2.0f) {
                    color = q
                } else if (c >= 1 / 2.0f && c < 2 / 3.0f) {
                    color = p + ((q - p) * 6 * (2 / 3.0f - c))
                } else {
                    color = p
                }
                if (i == 0) {
                    r = color
                } else if (i == 1) {
                    g = color
                } else {
                    b = color
                }
            }
        }
        return makeColor(float2Color(r), float2Color(g), float2Color(b))
    }

    fun rgb2hsl(r: Int, g: Int, b: Int): HSL {
        var rr = r / 255.0f
        var gg = g / 255.0f
        var bb = b / 255.0f
        rr = if (rr > 1.0f) 1.0f else if (rr < 0f) 0f else rr
        gg = if (gg > 1.0f) 1.0f else if (gg < 0f) 0f else gg
        bb = if (bb > 1.0f) 1.0f else if (bb < 0f) 0f else bb
        var h: Float = 0f
        var s = 0f
        var l = 0f
        var max = max(rr, max(gg, bb))
        var min = min(rr, min(gg, bb))
        if (max.equals(min)) {
            h = 0f
        } else if (max.equals(rr) && gg >= bb) {
            h = 60 * (gg - bb) / (max - min)
        } else if (max.equals(rr) && gg < bb) {
            h = 60 * (gg - bb) / (max - min) + 360
        } else if (max.equals(gg)) {
            h = 60 * (bb - rr) / (max - min) + 120
        } else {
            h = 60 * (rr - gg) / (max - min) + 240
        }
        l = (max + min) / 2f
        if (l.equals(0f) || max.equals(min)) {
            s = 0f
        } else if (s > 0 && s <= 0.5f) {
            s = (max - min) / (max + min)
        } else {
            s = (max - min) / (2 - (max + min))
        }
        return HSL(h, s, l)
    }

    fun addSaturation(v: Float, bitmap: Bitmap) {
        if (!bitmap?.isRecycled) {
            for (i in (0 until bitmap.width)) {
                for (j in 0 until bitmap.height) {
                    val pixel = bitmap.getPixel(i, j)
                    val hsl = rgb2hsl(
                        pixel shr 16 and 0xff,
                        pixel shr 8 and 0xff,
                        pixel and 0xff
                    )
                    hsl.s *= (1 + v)
                    hsl.h = max(0f, min(hsl.h, 360f))
                    hsl.s = max(0f, min(1f, hsl.s))
                    hsl.l = max(0f, min(1f, hsl.l))
                    val rgb = hsl2rgb(hsl.h, hsl.s, hsl.l)
                    bitmap.setPixel(i, j, rgb or (0xff shl 24))
                }
            }
        }
    }

    fun float2Color(v: Float): Int {
        return (v * 255).toInt()
    }

    fun getR(color: Int): Int {
        return (color shr 16) and 0xFF
    }

    fun getG(color: Int): Int {
        return (color shr 8) and 0xFF
    }

    fun getB(color: Int): Int {
        return (color and 0xFF)
    }

    class HSL {
        constructor(h: Float, s: Float, l: Float) {
            this.h = h
            this.s = s
            this.l = l
        }

        var h: Float = 0F
        var s: Float = 0F
        var l: Float = 0F

        override fun toString(): String {
            return "h:$h   s:$s   l:$l"
        }
    }
}
