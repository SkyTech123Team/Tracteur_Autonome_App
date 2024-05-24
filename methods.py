import RPi.GPIO as GPIO
from time import sleep
import io
import picamera


# Define GPIO pins
in1 = 24
in2 = 23
en = 5
in3 = 22
in4 = 27
en_a = 18

# GPIO setup
GPIO.setmode(GPIO.BCM)
GPIO.setup(in1, GPIO.OUT)
GPIO.setup(in2, GPIO.OUT)
GPIO.setup(en, GPIO.OUT)
GPIO.setup(in3, GPIO.OUT)
GPIO.setup(in4, GPIO.OUT)
GPIO.setup(en_a, GPIO.OUT)

# Ensure all pins are low at startup
GPIO.output(in1, GPIO.LOW)
GPIO.output(in2, GPIO.LOW)
GPIO.output(in3, GPIO.LOW)
GPIO.output(in4, GPIO.LOW)

# PWM setup
p = GPIO.PWM(en, 1000)
p_a = GPIO.PWM(en_a, 1000)
p.start(25)
p_a.start(25)

# Control functions
def forward():
    print("forward")
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)
def backward():
    print("backward")
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def stopCar():
    print("stop")
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.LOW)

def turnRight():
    print("right")
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)

def generate_frames():
    try :
        with picamera.PiCamera() as camera :
            camera.resolution = (640,480)
            camera.framerate = 24
            stream = io.BytesIO()
            
            for _ in camera.capture_continuous(stream,'jpeg',use_video_port=True):
                stream.seek(0)
                yield b'--frame\r\nContent-Type: image/jpeg\r\n\r\n' + stream.read() + b'\r\n'
                stream.seek(0)
                stream.truncate()
    except Exception as e:
        print(f"An error occurred: {e}")

def turnLeft():
    print("left")
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def move_forward_distance(distance_cm):
    # Constants
    wheel_circumference = 18.85  # calculated circumference
    time_per_cm = 0.2  # This needs calibration: measure time for known distance

    # Calculate total time to move the requested distance
    total_time = distance_cm * time_per_cm

    # Start moving forward
    forward()

    # Sleep while moving
    sleep(total_time)

    # Stop the car
    stopCar()