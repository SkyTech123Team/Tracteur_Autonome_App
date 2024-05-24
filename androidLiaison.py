from flask import Flask, render_template,Response ,request, jsonify

import io
import picamera
from time import sleep


#from moveCar1 import forward, backward, turnRight180, turnLeft180, stopCar
import methods 
#pip install firebase-admin
'''import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

cred = credentials.Certificate("./arauth-c10d2-firebase-adminsdk-2r4dm-15e730230f.json")
firebase_admin.initialize_app(cred)

db = firestore.client()
import socket

def get_ip_address():
    """
    Cette fonction retourne l'adresse IP de la voiture.
    """
    try:
        # Créer un socket pour obtenir l'adresse IP
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        # Connecter à un serveur fictif
        s.connect(("8.8.8.8", 80))
        # Obtenir l'adresse IP du socket connecté
        ip_address = s.getsockname()[0]
        # Fermer le socket
        s.close()
        return ip_address
    except Exception as e:
        print("Erreur lors de la récupération de l'adresse IP :", str(e))
        return None
    
data=get_ip_address()


# Update the document
doc_ref = db.collection('adresseIP').document('1')
doc_ref.update({'adresse': data})


'''

app = Flask(__name__)

@app.route('/move_forward')
def move_forward():
    #forward()
    methods.forward()
    return 'Moving forward'


@app.route('/move_backward')
def move_backward():
    methods.backward()
    return 'Moving backward'

@app.route('/move_right')
def move_right():
    methods.turnRight()
    return 'Turning right'

@app.route('/move_left')
def move_left():
    methods.turnLeft()
    return 'Turning left'

@app.route('/video_feed')
def video_feed():
    return Response(methods.generate_frames(),mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/stop')
def stop():
    methods.stopCar()
    return 'Stopping'

@app.route('/sendInfo', methods=['POST'])
def cover_rectangle():
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
            methods.move_forward_distance(length)
            # Turn around 180 degrees to come back
            methods.turnRight()
            sleep(1)  # Short delay to stabilize after turn
            methods.turnRight()
            sleep(1)  # Short delay to stabilize after turn

            # Move forward the full length of the rectangle
            methods.move_forward_distance(length)

            # Prepare for the next pass, if there's more area to cover
            if pass_num < number_of_passes - 1:
                # Turn 90 degrees to the right to align for the next pass
                methods.turnRight()
                sleep(1)  # Short delay to stabilize after turn
                # Move forward the width of one pass
                methods.move_forward_distance(pass_width)
                # Turn 90 degrees to the right again to realign for the next length pass
                methods.turnRight()
                sleep(1)  # Short delay to stabilize after turn

        # Return a response to the client
        return jsonify({'message': 'Operation successful'})

if __name__ == '__main__' :
    app.run(host='0.0.0.0' , port=5000,threaded=True)
