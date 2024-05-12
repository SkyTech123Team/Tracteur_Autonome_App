import moveCar1
from flask import Flask,request


app = Flask(__name__)

@app.route('/move_to_right', methods=['GET'])
def move_to_right():
    # Code pour déplacer la voiture vers la droite
    print('hello gys')
    moveCar1.turnRight180()
    return 'Moved to right'

@app.route('/move_to_left', methods=['GET'])
def move_to_left():
    # Code pour déplacer la voiture vers la gauche
    moveCar1.turnLeft180()
    return 'Moved to left'

@app.route('/move_to_forward', methods=['GET'])
def move_to_forward():
    # Code pour déplacer la voiture vers l'avant
    command = 'python3 /home/pi/Desktop/Mauvements/moveCar1.py forward'  # Exemple de commande à exécuter
    return command
    
    


@app.route('/move_to_backward', methods=['GET'])
def move_to_backward():
    # Code pour déplacer la voiture vers l'arrière
    moveCar1.backward()
    return 'Moved to backward'

@app.route('/stop', methods=['GET'])
def stop_car():
    # Code pour arrêter la voiture
    moveCar1.stopCar()
    return 'Car stopped'

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)