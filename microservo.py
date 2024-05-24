import RPi.GPIO as GPIO
from time import sleep

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(12, GPIO.OUT)
p = GPIO.PWM(12, 50)
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
        if commande == "m":
            monter_bras()
        elif commande == "d":
            descendre_bras()
        else:
            print("Commande invalide. Entrez monter ou descendre.")

except KeyboardInterrupt:
    p.stop()
    GPIO.cleanup()