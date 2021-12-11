from flask import Flask, jsonify, request
import json
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
import base64
from flask.helpers import make_response

from flask.wrappers import Response

app = Flask(__name__)

def imagePanel():
    app.logger.debug('パネルきてます')
    img_resize = np.array(Image.open('result.png').resize((128, 128)))
    img_tile = np.tile(img_resize, (8, 12, 1))
    Image.fromarray(img_tile).save('result-1.png')

def imageBin():
    app.logger.debug('二値化きてます')
    img_gray = np.array(Image.open('result.png').convert('L'))
    thresh = 128
    maxval = 255
    img_bin = (img_gray > thresh) * maxval
    Image.fromarray(np.uint8(img_bin)).save('result-1.png')

def imageInvert():
    app.logger.debug('反転きてます')
    img_arr = np.array(Image.open('result.png'))
    img_inv = 255 - img_arr
    Image.fromarray(img_inv).save('result-1.png')

def imageProccesses(key):
    dic = {'panel': imagePanel, 'bin': imageBin, 'invert': imageInvert}
    if key in dic:
        dic[key]()
    return
