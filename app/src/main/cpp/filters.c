#include "filters.h"
#include <math.h>
#include <memory.h>
#include <stdlib.h>

void JNIFUNCF(AlgManager, nativeAddSaturationN, float saturation, jobject bitmap,
              jint width, jint height) {
    unsigned char *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);
    int tot_len = height * width * 4;
    float hsl[3];
    for (int i = 0; i < tot_len; i += 4) {
        unsigned char *pixel = destination + i;
        // first convert every pixel from rgb to hsl
        rgb2hsl(pixel, hsl);
        // add saturation for every pixel with same ratio
        hsl[1] *= (1.0f + saturation);
        // second convert hsl to rgb
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

void JNIFUNCF(AlgManager, nativeGrayScaleLog, jobject bitmap,
              jint width, jint height) {
    unsigned char *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);
    int tot_len = height * width * 4;
    float value[256];
    for (int i = 0; i < 256; ++i) {
        value[i] = log2f(1 + i);
    }
    float max_norm = log2f(256) / 255.0f;
    for (int i = 0; i < tot_len; i += 4) {
        unsigned char *pixel = destination + i;
        for (int j = 0; j < 3; ++j) {
            float tmp = value[pixel[j]];
            tmp = tmp / max_norm;
            pixel[j] = (unsigned char) CLAMP(tmp, 0.0f, 255.0f);
        }
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}

void JNIFUNCF(AlgManager, nativeGrayScaleGamma, float gamma, jobject bitmap,
              jint width, jint height) {
    unsigned char *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);
    int tot_len = height * width * 4;
    float value[256];
    for (int i = 0; i < 256; ++i) {
        value[i] = powf(i, gamma);
    }
    float max_norm = value[255] / 255.0f;
    for (int i = 0; i < tot_len; i += 4) {
        unsigned char *pixel = destination + i;
        for (int j = 0; j < 3; ++j) {
            float tmp = value[pixel[j]];
            tmp = tmp / max_norm;
            pixel[j] = (unsigned char) CLAMP(tmp, 0.0f, 255.0f);
        }
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}

/**
 * 均值模糊 ,一般算法，速度非常慢
 * @param env
 * @param obj
 * @param box_size
 * @param bitmap
 * @param width
 * @param height
 */
void JNIFUNCF(AlgManager, nativeMeanBlur, int box_size, jobject bitmap,
              jint width, jint height) {

    if (box_size > width || box_size > height) return;
    unsigned int *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);

    unsigned int *tmp = (unsigned int *) malloc((size_t) (4 * width * height));
    for (int i = 0; i < width; ++i) {
        for (int j = 0; j < height; ++j) {
            unsigned int c = 0;
            unsigned long long sum = 0;
            for (int bi = -box_size / 2; bi < box_size / 2; ++bi) {
                for (int bj = -box_size / 2; bj < box_size / 2; ++bj) {
                    if (i + bi >= 0 && i + bi < width && j + bj >= 0 && j + bj < height) {
                        c++;
                        sum += destination[j * width + i];
                    }
                }
            }
            tmp[j * width + i] = (unsigned int) (sum / c);
        }
    }
    LOG("long %d", (int) sizeof(long));
    LOG("long long %d", (int) sizeof(long long));
    memcpy(destination, tmp, (size_t) (4 * width * height));
    free(tmp);
    AndroidBitmap_unlockPixels(env, bitmap);
}


/**
 * 均值模糊 ,快速算法，速度非常慢
 * @param env
 * @param obj
 * @param box_size
 * @param bitmap
 * @param width
 * @param height
 */
void JNIFUNCF(AlgManager, nativeFastMeanBlur, int box_size, jobject bitmap,
              jint width, jint height) {

    if (box_size > width || box_size > height) return;
    unsigned int *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);

    long long *tmp = (long long *) malloc((size_t) (sizeof(long long) * width * height));
    for (int i = 0; i < width; ++i) {
        for (int j = 0; j < height; ++j) {
            long sum = 0;
            sum += destination[j * width + i];
            if (i - 1 >= 0) {
                sum += tmp[j * width + i - 1];
            }
            if (j - 1 >= 0) {
                sum += tmp[(j - 1) * width + i];
            }
            if (i - 1 >= 0 && j - 1 >= 0) {
                sum -= tmp[(j - 1) * width + i - 1];
            }
            tmp[j * width + i] = sum;
        }
    }

    for (int i = 0; i < width; ++i) {
        for (int j = 0; j < height; ++j) {
            int w = box_size / 2;
            long top_left = 0;
            long top_right = 0;
            long bottom_left = 0;
            long bottom_right = 0;
            int col, row = 0;

            col = j - w - 1;
            row = i - w - 1;
            if (col >= 0 && row >= 0) {
                top_left = tmp[col * width + row];
            }
            col = j - w - 1;
            row = i + w + 1;
            if (col >= 0 && row < height) {
                bottom_left = tmp[col * width + row];
            }
            col = j + w;
            row = i - w - 1;
            if (col < width && row >= 0) {
                top_right = tmp[col * width + row];
            }
            col = j + w;
            row = i + w;
            if (col < width && row < height) {
                top_right = tmp[col * width + row];
            }
            destination[j * width + i] = (unsigned int) (
                    (bottom_right + top_left - top_right - bottom_left) / (box_size * box_size));
        }
    }

    free(tmp);
    AndroidBitmap_unlockPixels(env, bitmap);
}


void JNIFUNCF(AlgManager, nativeRobert, jobject bitmap,
              jint width, jint height) {

    unsigned int *destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &destination);

    unsigned int *tmp1 = (unsigned int *) malloc((size_t) (4 * width * height));
    unsigned int *tmp2 = (unsigned int *) malloc((size_t) (4 * width * height));
    for (int i = 0; i < width; ++i) {
        for (int j = 0; j < height; ++j) {
            //当前遍历的点为左上角的点
            int top_left = getGray((unsigned char *) (destination + j * width + i));
            int bottom_left = 0;
            int top_right = 0;
            int bottom_right = 0;
            if (j + 1 < height) {
                bottom_left = getGray((unsigned char *) (destination + (j + 1) * width + i));
            }
            if (i + 1 < width) {
                top_right = getGray((unsigned char *) (destination + j * width + i + 1));
            }
            if (j + 1 < height && i + 1 < width) {
                bottom_right = getGray((unsigned char *) (destination + (j + 1) * width + i + 1));
            }

            int c = top_left - bottom_right;
            c = CLAMP(c, 0, 255);
            unsigned char *p = (unsigned char *) (tmp1 + j * width + i);
            *p = (unsigned char) c;
            *(p + 1) = (unsigned char) c;
            *(p + 2) = (unsigned char) c;

            c = bottom_left - top_right;
            c = CLAMP(c, 0, 255);
            p = (unsigned char *) (tmp2 + j * width + i);
            *p = (unsigned char) c;
            *(p + 1) = (unsigned char) c;
            *(p + 2) = (unsigned char) c;
        }
    }
    for (int i = 0; i < width; ++i) {
        for (int j = 0; j < height; ++j) {
            int c = getGray((unsigned char *) (tmp1 + j * width + i)) + getGray(
                    (unsigned char *) (tmp2 + j * width + i));
            c = CLAMP(c, 0, 255);
            unsigned char *p = (unsigned char *) (destination + j * width + i);
            *p = (unsigned char) c;
            *(p + 1) = (unsigned char) c;
            *(p + 2) = (unsigned char) c;
        }
    }
    free(tmp1);
    free(tmp2);
    AndroidBitmap_unlockPixels(env, bitmap);
}

int getGray(unsigned char *p) {
    int r = p[0];
    int g = p[1];
    int b = p[2];
    return (unsigned char) ((r + g + b) / 3);
}

void setGray(unsigned char *p, unsigned char c) {
    *p = c;
}

