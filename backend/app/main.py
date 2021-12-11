from flask import Flask, jsonify, request
import json
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
import base64
from imageProccess import imageProccesses
from flask.helpers import make_response

from flask.wrappers import Response

def imageProccesses(key):
    if key == 'panel':
        app.logger.debug('パネルきてます')
        img_resize = np.array(Image.open('result.png').resize((128, 128)))
        img_tile = np.tile(img_resize, (8, 12, 1))
        Image.fromarray(img_tile).save('result-1.png')
    elif key == 'bin':
        app.logger.debug('二値化きてます')
        img_gray = np.array(Image.open('result.png').convert('L'))
        thresh = 128
        maxval = 255
        img_bin = (img_gray > thresh) * maxval
        Image.fromarray(np.uint8(img_bin)).save('result-1.png')
    elif key == 'invert':
        app.logger.debug('反転きてます')
        img_arr = np.array(Image.open('result.png'))
        img_inv = 255 - img_arr
        Image.fromarray(img_inv).save('result-1.png')
    return

app = Flask(__name__)


@app.route("/", methods=['GET'])
def hello():
    return "route get. Hello!"


@app.route('/reply', methods=['POST'])
def reply():
    data = json.loads(request.data)
    answer = "route post. keyword is %s!\n" % data["keyword"]
    result = {
        "Content-Type": "application/json",
        "Answer": {"Text": answer}
    }
    # return answer
    return jsonify(result)


@app.route('/json', methods=['GET'])
def setJson():
    res = "Json Message"
    app.logger.debug(res)
    return jsonify(res)


@app.route('/jsonpost', methods=['POST'])
def setJsonPost():
    aaa = request.get_data()
    app.logger.debug(aaa)

    result = ''
    if request.method == 'POST':
        result += 'POST, '
        result += str(request.get_data())
    return result


@app.route('/image', methods=['POST'])
def setImage():
    response = []
    if request.method == 'POST':
        req_key = request.form['key']
        req_img = request.form['pic']
        app.logger.debug(req_key)
        img_stream = base64.b64decode(req_img)
        png = np.frombuffer(img_stream, dtype=np.uint8)
        img = cv2.imdecode(png, cv2.IMREAD_COLOR)
        cv2.imwrite('result.png', img)

        imageProccesses(req_key)
        with open('result-1.png', "rb") as f:
            img_base64 = base64.b64encode(f.read()).decode('utf-8')
        response = jsonify(img_base64)
        return response
    return

    # filename = secure_filename(img.filename)
    # return response


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001, debug=True)
