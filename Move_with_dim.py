"""
<h3> Ce fichier contient la partie d envoi des dimensions de l'application mobile vers la vehicule.</h3>


<h3>Auteurs :SAFRANI Fatima Ezzahra </h3>


<h3>Version : 1.0</h3>

"""
import RPi.GPIO as GPIO
from time import sleep
from flask import Flask, request, jsonify

app = Flask(__name__)

# Define GPIO pins
in1 = 24
in2 = 23
en = 5
in3 = 22
in4 = 27
en_a = 18

# GPIO setup
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)  # Disable GPIO warnings
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
    """
    Cette fonction permet de faire une mauvement vers l'avant
    """
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)

def backward():
    """
    Cette fonction permet de faire une mauvement vers l'arrier
    """
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def stopCar():
    """
    Cette fonction permet de faire stopper la vehicule
    """
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.LOW)

def turnRight():
    """
    Cette fonction permet de faire un tour a droite
    """
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)

def turnLeft():
    """
    Cette fonction permet de faire un tour a gauche
    """
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def move_forward_distance(distance_cm):
    """
    Cette fonction permet de faire en avant avec une distance donnee
    """
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

@app.route('/sendInfo', methods=['POST'])
def cover_rectangle():
    """
    Cette fonction permet de couvrir un rectangle en donnant ces dimenssions
    """
    if request.method == 'POST':
        data = request.json
        if data is None:
            return abort(400, description="No data provided.")

        length = data.get('height')
        width = data.get('width')

        # Check if necessary parameters are present and are of correct type
        if length is None or width is None:
            return abort(400, description="Missing 'height' or 'width' parameters.")

        try:
            length = float(length)  # Ensure length is a float (in case it's a decimal)
            width = float(width)  # Ensure width is a float (in case it's a decimal)
            pass_width = 10  # Width of each pass, adjust based on your tractor's effective width

            number_of_passes = int(width / pass_width)
        except ValueError:
            return abort(400, description="Invalid 'height' or 'width'. Must be a number.")

        for pass_num in range(number_of_passes):
            # Move forward the full length of the rectangle
            move_forward_distance(length)
            # Turn around 180 degrees to come back
            turnRight()
            sleep(1)  # Short delay to stabilize after turn
            turnRight()
            sleep(1)  # Short delay to stabilize after turn

            # Move forward the full length of the rectangle
            move_forward_distance(length)

            # Prepare for the next pass, if there's more area to cover
            if pass_num < number_of_passes - 1:
                # Turn 90 degrees to the right to align for the next pass
                turnRight()
                sleep(1)  # Short delay to stabilize after turn
                # Move forward the width of one pass
                move_forward_distance(pass_width)
                # Turn 90 degrees to the right again to realign for the next length pass
                turnRight()
                sleep(1)  # Short delay to stabilize after turn

        # Return a response to the client
        return jsonify({'message': 'Operation successful'})

# Start the Flask app
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
