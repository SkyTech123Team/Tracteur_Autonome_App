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
    GPIO.setmode(GPIO.BOARD)
    GPIO.setwarnings(False)

    #ultrasonic1
    TRIG1 = 8
    ECHO1 = 11
    #ultrasonic2
    
    TRIG2 = 36
    ECHO2 = 37
    #ultrasonic3
    
    TRIG3 = 32
    ECHO3 = 31

    print("Mesure de la distance ")
    #ultrasonic1
    GPIO.setup(TRIG1,GPIO.OUT)
    GPIO.setup(ECHO1,GPIO.IN)
    GPIO.output(TRIG1,False)
    
    #ultrasonic2
    GPIO.setup(TRIG2,GPIO.OUT)
    GPIO.setup(ECHO2,GPIO.IN)
    GPIO.output(TRIG2,False)
    
    #ultrasonic3
    GPIO.setup(TRIG3,GPIO.OUT)
    GPIO.setup(ECHO3,GPIO.IN)
    GPIO.output(TRIG3,False)
    print("Waiting for sensor 3 to send signal")
    time.sleep(2)
    GPIO.output(TRIG3,True)
    time.sleep(0.00001)
    GPIO.output(TRIG3,False)
    print("Reading sensor")
    pulse_start = time.time()
    pulse_end = time.time()
    while GPIO.input(ECHO3) == 0:
        pulse_start = time.time()
    while GPIO.input(ECHO3) == 1:
        pulse_end = time.time()
    pulse_dur = pulse_end-pulse_start
    distance = pulse_dur * 340 * 100 / 2
    distance = round(distance,1)
    print("Distance : ",distance , "cm")
    
    #Ultrasonic1
    print("Waiting for sensor 1 to send signal")
    time.sleep(2)
    GPIO.output(TRIG1,True)
    time.sleep(0.00001)
    GPIO.output(TRIG1,False)
    print("Reading sensor")
    pulse_start = time.time()
    pulse_end = time.time()
    while GPIO.input(ECHO1) == 0:
        pulse_start = time.time()
    while GPIO.input(ECHO1) == 1:
        pulse_end = time.time()
    pulse_dur = pulse_end-pulse_start
    distance = pulse_dur * 340 * 100 / 2
    distance = round(distance,1)
    print("Distance : ",distance , "cm")
    
    #Ultrasonic2
    print("Waiting for sensor 2 to send signal")
    time.sleep(2)
    GPIO.output(TRIG2,True)
    time.sleep(0.00001)
    GPIO.output(TRIG2,False)
    print("Reading sensor")
    pulse_start = time.time()
    pulse_end = time.time()
    while GPIO.input(ECHO2) == 0:
        pulse_start = time.time()
    while GPIO.input(ECHO2) == 1:
        pulse_end = time.time()
    pulse_dur = pulse_end-pulse_start
    distance = pulse_dur * 340 * 100 / 2
    distance = round(distance,1)
    print("Distance : ",distance , "cm")
    
    
    
    
mesure_distance()
