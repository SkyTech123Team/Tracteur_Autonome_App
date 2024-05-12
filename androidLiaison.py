from flask import Flask, render_template, request
import RPi.GPIO as GPIO
import time

app = Flask(__name__)

# Configuration des broches GPIO pour les moteurs
in1 = 24
in2 = 23
en = 25
in3 = 22
in4 = 27
en_a = 18
temp1 = 1

#Initialisation de la bibliothèque GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(in1, GPIO.OUT)
GPIO.setup(in2, GPIO.OUT)
GPIO.setup(in3, GPIO.OUT)
GPIO.setup(in4, GPIO.OUT)
GPIO.setup(en, GPIO.OUT)
GPIO.setup(en_a, GPIO.OUT)
GPIO.output(in1, GPIO.LOW)
GPIO.output(in2, GPIO.LOW)
GPIO.output(in3, GPIO.LOW)
GPIO.output(in4, GPIO.LOW)

p = GPIO.PWM(en, 1000)
p_a = GPIO.PWM(en_a, 1000)
p.start(25)
p_a.start(18)

@app.route('/')
def index():
    return render_template('index.html')

# Définir les routes pour les mouvements du véhicule
@app.route('/move_forward')
def move_forward():
    forward()
    return 'Moving forward'

@app.route('/move_backward')
def move_backward():
    backward()
    return 'Moving backward'

@app.route('/move_right')
def move_right():
    turnRight180()
    return 'Turning right'

@app.route('/move_left')
def move_left():
    turnLeft180()
    return 'Turning left'

@app.route('/stop')
def stop():
    stopCar()
    return 'Stopping'

# Fonctions de mouvement du véhicule
def forward():
    print('helooooooooooooooooooooo')
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)

def backward():
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def stopCar():
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.LOW)

def turnRight180():
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)
    time.sleep(5)
    forward()

def turnLeft180():
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)
    time.sleep(5)
    forward()

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
