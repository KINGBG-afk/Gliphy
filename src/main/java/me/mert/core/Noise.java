package me.mert.core;

// standart perling noise formula
public class Noise {
    private final int[] perm;

    public Noise(int seed) {
        perm = buildPermTable(seed);
    }

    public float getNoise(float x, float y) {
        return perlin(perm, x, y);
    }

    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private float grad(int h, float x, float y) {
        int r = h & 3;
        float u = r < 2 ? x : y;
        float v = r < 2 ? y : x;
        return ((r & 1) != 0 ? -u : u)
                + ((r & 2) != 0 ? -v : v);
    }

    private int[] buildPermTable(int seed) {
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        int s = seed & 0xffffffff;

        for (int i = 255; i > 0; i--) {
            s = (s * 1664525 + 1013904223);
            int j = Integer.remainderUnsigned(s, i + 1);

            // this looks so wrong idk why
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }

        int[] perm = new int[512];
        for (int i = 0; i < 512; i++) {
            perm[i] = p[i & 255];
        }
        return perm;
    }

    private float perlin(int[] perm, float x, float y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;

        x -= (float) Math.floor(x);
        y -= (float) Math.floor(y);

        float u = fade(x);
        float v = fade(y);

        long a = perm[X] + Y;
        long aa = perm[(int) a];
        long ab = perm[(int) a + 1];

        long b = perm[X + 1] + Y;
        long ba = perm[(int) b];
        long bb = perm[(int) b + 1];

        return lerp(
                lerp(grad(perm[(int) aa], x, y),
                        grad(perm[(int) ba], x - 1, y), u),

                lerp(grad(perm[(int) ab], x, y - 1),
                        grad(perm[(int) bb], x - 1, y - 1), u),
                v);
    }

}
