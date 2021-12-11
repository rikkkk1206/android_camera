FROM python:3.8.6

RUN apt-get update && apt-get upgrade -y
RUN pip install --upgrade pip
RUN apt-get install -y libgl1-mesa-dev

ARG project_dir=/app/

WORKDIR $project_dir

RUN pip install flask && pip install pillow && pip install numpy && pip install opencv-python && pip install opencv-contrib-python

CMD ["python", "main.py"]