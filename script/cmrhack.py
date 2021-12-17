import os, sys

import numpy as np
import cv2 as cv
import matplotlib.pyplot as plt
from random import seed
from random import random

def set_line(x1, x2, rand, neigh):
   for y in range(shape[0]):

      r1, r2, g1, g2, b1, b2 = [0, 0, 0, 0, 0, 0]
      for i in range(neigh):
         r1 += int(picture[y,x1-i,0])
         r2 += int(picture[y,x2+i,0])
         g1 += int(picture[y,x1-i,1])
         g2 += int(picture[y,x2+i,1])
         b1 += int(picture[y,x1-i,2])
         b2 += int(picture[y,x2+i,2])
      r1 /= neigh
      r2 /= neigh
      g1 /= neigh
      g2 /= neigh
      b1 /= neigh
      b2 /= neigh

      for x in range(x1,x2+1):
         r = int(r1 + (x-x1+1) * ((r2-r1)/(x2-x1)) - rand + 2*rand*random())
         g = int(g1 + (x-x1+1) * ((g2-g1)/(x2-x1)) - rand + 2*rand*random())
         b = int(b1 + (x-x1+1) * ((b2-b1)/(x2-x1)) - rand + 2*rand*random())
         
         if r < 0:
            r = 0
         if g < 0:
            g = 0
         if b < 0:
            b = 0

         picture[y,x] = [r,g,b]

picture = cv.imread('2021-10-31 23.51.25.jpg')
shape = picture.shape
print(shape)
seed(1)

# print(picture[1178,295])
# print(picture[1178,312])
# print([int(picture[1178,295,0]/2+picture[1178,312,0]/2), int(picture[1178,295,0]/2+picture[1178,312,1]/2), int(picture[1178,295,2]/2+picture[1178,312,2]/2)])


pixels1 = np.array(cv.cvtColor(picture, cv.COLOR_BGR2RGB))

set_line(296,314, 7, 5)
set_line(510,527, 7, 5)
set_line(935,947, 7, 5)
set_line(1020,1030, 7, 5)
set_line(1200,1212, 7, 5)
set_line(1280,1290, 7, 5)
set_line(1393,1403, 7, 5)
set_line(1455,1468, 7, 5)
set_line(1482,1495, 7, 5)

# displaying image
# pixels2 = np.array(cv.cvtColor(picture, cv.COLOR_BGR2RGB))
# plt.imshow(pixels2)
# plt.show()

cv.imwrite('./generated/sample.jpg', picture)
print("Script executed successfully.")