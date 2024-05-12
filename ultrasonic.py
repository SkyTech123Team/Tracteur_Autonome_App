"""
<h3>Ce fichier contient une methode qui mesure la distance entre la vehicule et un obstacle.</h3>


<h3>Auteur :  EL-MANANI Fatima </h3>


<h3>Version : 2.0</h3>
"""

import RPi.GPIO as GPIO
import time


def mesure_distance():
    """
    Cette fonction mesure la distance entre le véhicule et un obstacle en utilisant un capteur ultrasonique.
    Elle configure les broches TRIG et ECHO du capteur, envoie une impulsion, puis calcule et affiche la distance mesurée en centimètres.
    """
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)

    TRIG = 21
    ECHO = 16

    print("Mesure de la distance ")
    GPIO.setup(TRIG,GPIO.OUT)
    GPIO.setup(ECHO,GPIO.IN)
    GPIO.output(TRIG,False)
    print("Waiting ")
    time.sleep(2)
    GPIO.output(TRIG,True)
    time.sleep(0.00001)
    GPIO.output(TRIG,False)
    while GPIO.input(ECHO) == 0:
        pulse_start = time.time()
    while GPIO.input(ECHO) == 1:
        pulse_end = time.time()
    pulse_dur = pulse_end-pulse_start
    distance = pulse_dur * 340 * 100 / 2
    distance = round(distance,1)
    print("Distance : ",distance , "cm")