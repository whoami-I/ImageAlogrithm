#include "filters.h"

void JNIFUNCF(AlgManager, nativeAddSaturation_n, float saturation, jobject bitmap,
              jint width, jint height) {

}


void rgb2hsl(unsigned char *rgb, float *hsl) {
    float r = rgb[0] / 255.0f;
    float g = rgb[1] / 255.0f;
    float b = rgb[2] / 255.0f;
    float h, s, l = 0f;
    r = CLAMP(r, 0f, 1f);
    g = CLAMP(g, 0f, 1f);
    b = CLAMP(b, 0f, 1f);
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
    h = CLAMP(h, 0, 360);
    s = CLAMP(s, 0, 1);
    l = CLAMP(l, 0, 1);
    float q, p, hk = 0;
    if (l < 0.5) {
        q = l * (1 + s);
    } else {
        q = l + s - (l * s);
    }
    p = 2 * l - q;
    hk = h / 360f;
    float t[3];
    for (int i = 0; i < 3; i++) {

    }
}