#include "filters.h"

void JNIFUNCF(AlgManager, nativeAddSaturationN, float saturation, jobject bitmap,
              jint width, jint height) {
    unsigned char *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);
    int tot_len = height * width * 4;
    float hsl[3];
    for (int i = 0; i < tot_len; i += 4) {
        unsigned char *pixel = destination + i;
        rgb2hsl(pixel, hsl);
        // add saturation for every pixel with same ratio
        hsl[1] *= (1.0f + saturation);
        hsl2rgb(hsl, pixel);
    }

    float hsln[3] = {344, 1, 0.57};
    unsigned char rgb[3] = {255, 36, 96};
    hsl2rgb(hsln, rgb);
    LOG("hsl 2 rgb %u %u %u", rgb[0], rgb[1], rgb[2]);

    AndroidBitmap_unlockPixels(env, bitmap);
}


void rgb2hsl(unsigned char *rgb, float *hsl) {
    float r = rgb[0] / 255.0f;
    float g = rgb[1] / 255.0f;
    float b = rgb[2] / 255.0f;
    float h, s, l = 0.0f;
    r = CLAMP(r, 0.0f, 1.0f);
    g = CLAMP(g, 0.0f, 1.0f);
    b = CLAMP(b, 0.0f, 1.0f);
    float max = MAX(r, MAX(g, b));
    float min = MIN(r, MIN(g, b));
    if (max == min) {
        h = 0;
    } else if (max == r && g >= b) {
        h = 60.0f * (g - b) / (max - min);
    } else if (max == r && g < b) {
        h = 60.0f * (g - b) / (max - min) + 360;
    } else if (max == g) {
        h = 60.0f * (b - r) / (max - min) + 120;
    } else {
        h = 60.0f * (r - g) / (max - min) + 240;
    }
    l = (max + min) / 2;
    if (l == 0 || max == min) {
        s = 0;
    } else if (l > 0 && l <= 0.5) {
        s = (max - min) / (max + min);
    } else {
        s = (max - min) / (2 - max - min);
    }
    hsl[0] = h;
    hsl[1] = s;
    hsl[2] = l;
}

void hsl2rgb(float *hsl, unsigned char *rgb) {
    float h = hsl[0];
    float s = hsl[1];
    float l = hsl[2];
    if (s == 0.0f) {
        rgb[0] = rgb[1] = rgb[2] = (unsigned char) (255 * l);
    } else {
        h = CLAMP(h, 0, 360);
        s = CLAMP(s, 0, 1);
        l = CLAMP(l, 0, 1);
        float q, p, hk = 0;
        if (l < 0.5f) {
            q = l * (1 + s);
        } else {
            q = l + s - (l * s);
        }
        p = 2 * l - q;
        hk = h / 360.0f;
        float t[3];
        t[0] = hk + 1 / 3.0f;
        t[1] = hk;
        t[2] = hk - 1 / 3.0f;
        for (int i = 0; i < 3; ++i) {
            if (t[i] < 0) {
                t[i] = 1.0f + t[i];
            } else if (t[i] > 1) {
                t[i] = t[i] - 1.0f;
            }
            float c = t[i];
            float color = 0.0f;
            if (c < 1 / 6.0f) {
                color = p + (q - p) * 6 * c;
            } else if (c >= 1 / 6.0f && c < 1 / 2.0f) {
                color = q;
            } else if (c >= 1 / 2.0f && c < 2 / 3.0f) {
                color = p + ((q - p) * 6 * (2 / 3.0f - c));
            } else {
                color = p;
            }
            color *= 255;
            color = CLAMP(color, 0, 255);
            if (i == 0) {
                rgb[0] = (unsigned char) color;
            } else if (i == 1) {
                rgb[1] = (unsigned char) color;
            } else {
                rgb[2] = (unsigned char) color;
            }
        }
    }
}

void JNIFUNCF(AlgManager, nativeNegative, jobject bitmap,
              jint width, jint height) {
    unsigned char *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);
    int tot_len = height * width * 4;
    for (int i = 0; i < tot_len; i += 4) {
        unsigned char *pixel = destination + i;
        pixel[0] = (unsigned char) (255 - pixel[0]);
        pixel[1] = (unsigned char) (255 - pixel[1]);
        pixel[2] = (unsigned char) (255 - pixel[2]);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}
