package com.mike.imagealogrithm.algorithm

import kotlin.math.floor

object AlgManager {
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

    fun getR(color: Int): Int {
        return (color shr 16) and 0xFF
    }

    fun getG(color: Int): Int {
        return (color shr 8) and 0xFF
    }

    fun getB(color: Int): Int {
        return (color and 0xFF)
    }
}
