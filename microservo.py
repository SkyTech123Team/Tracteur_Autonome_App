"""
<h3>Ce fichier contient deux fonction qui controlent le mauvement du micro servo soit monter ou descendre le bras</h3>


<h3>Auteurs : SAFRANI Fatima Ezzahra</h3>


<h3>Version : 1.0</h3>
"""

import RPi.GPIO as GPIO
from time import sleep

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(11, GPIO.OUT)
p = GPIO.PWM(11, 50)
p.start(0)

def monter_bras():
    p.ChangeDutyCycle(3)  # Position pour monter le bras
    sleep(1)  # Attendre 1 seconde
    p.ChangeDutyCycle(0)  # Arrêter le servo après le mouvement
    sleep(1)  # Attendre 1 seconde

def descendre_bras():
    p.ChangeDutyCycle(12)  # Position pour descendre le bras
    sleep(1)  # Attendre 1 seconde
    p.ChangeDutyCycle(0)  # Arrêter le servo après le mouvement
    sleep(1)  # Attendre 1 seconde

try:
    while True:
        commande = input("Entrez la commande (monter ou descendre): ")
        if commande == "monter":
            monter_bras()
        elif commande == "descendre":
            descendre_bras()
        else:
            print("Commande invalide. Entrez monter ou descendre.")

except KeyboardInterrupt:
    p.stop()
    GPIO.cleanup()