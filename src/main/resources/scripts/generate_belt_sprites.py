from PIL import Image  # type: ignore
import numpy as np  # type: ignore
import os

INPUT_IMAGE = "conveyor.png"  # if you need change to "...-left" or "-right"
OUTPUT_DIR = "sprites"
FRAMES = 11
SHIFT_PER_FRAME = -16

os.makedirs(OUTPUT_DIR, exist_ok=True)

img = Image.open(INPUT_IMAGE).convert("RGBA")
arr = np.array(img)

height, width, channels = arr.shape

for frame in range(FRAMES):
    shifted = np.roll(arr, shift=SHIFT_PER_FRAME * frame, axis=0)

    out_img = Image.fromarray(shifted, "RGBA")
    out_img.save(f"{OUTPUT_DIR}/right_{frame:02d}.png")

print("Done! Generated", FRAMES, "sprites.")
