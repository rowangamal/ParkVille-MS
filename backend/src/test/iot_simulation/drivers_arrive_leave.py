import requests
import random
import string
import time

# Configuration
BASE_URL = "http://localhost:8080/api/drivers"
SIGNUP_ENDPOINT = f"{BASE_URL}/signup"
LOGIN_ENDPOINT = f"{BASE_URL}/login"
RESERVE_SPOT_ENDPOINT = f"{BASE_URL}/spot/reserve"
ARRIVE_ENDPOINT = f"{BASE_URL}/spot/arrive"
LEAVE_ENDPOINT = f"{BASE_URL}/spot/leave"

# Generate random data for simulation
def random_string(length=8):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def random_license_plate():
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=7))

def simulate_drivers_and_reservations(num_drivers=10, reservations_per_driver=10):
    for i in range(num_drivers):
        # Step 1: Sign up driver
        username = random_string()
        email = f"{username}@example.com"
        password = random_string(12)
        license_plate = random_license_plate()
        payment_method = random.choice(["credit_card", "paypal", "cash"])

        signup_data = {
            "username": username,
            "email": email,
            "password": password,
            "licensePlateNumber": license_plate,
            "paymentMethod": payment_method
        }

        signup_response = requests.post(SIGNUP_ENDPOINT, json=signup_data)
        if signup_response.status_code != 200:
            print(f"Failed to sign up driver {username}: {signup_response.text}")
            continue

        print(f"Driver {username} signed up successfully.")

        # Step 2: Log in driver
        login_data = {
            "username": username,
            "password": password
        }

        login_response = requests.post(LOGIN_ENDPOINT, json=login_data)
        if login_response.status_code != 200:
            print(f"Failed to log in driver {username}: {login_response.text}")
            continue

        login_json = login_response.json()
        token = login_json.get("jwtToken")
        driver_id = login_json.get("id")
        role = login_json.get("role")

        if not token or not driver_id:
            print(f"Failed to retrieve token or ID for driver {username}.")
            continue

        print(f"Driver {username} logged in successfully with ID {driver_id} and role {role}.")

        # Step 3: Reserve parking spots
        headers = {"Authorization": f"Bearer {token}"}
        successful_reservations = []

        for j in range(reservations_per_driver):
            parking_lot_id = random.randint(5, 200)  # Assuming lot IDs are in this range
            parking_spot_id = random.randint(1, 10)  # Assuming spot IDs are in this range
            duration = random.randint(1, 24)        # Duration in hours

            reserve_data = {
                "parkingSpotId": parking_spot_id,
                "parkingLotId": parking_lot_id,
                "duration": duration
            }

            reserve_response = requests.post(RESERVE_SPOT_ENDPOINT, json=reserve_data, headers=headers)

            if reserve_response.status_code == 201:
                print(f"Spot {parking_spot_id} reserved for driver {username} in lot {parking_lot_id} for {duration} hours.")
                successful_reservations.append({"parkingLotId": parking_lot_id, "parkingSpotId": parking_spot_id})
            else:
                print(f"Failed to reserve spot {parking_spot_id} for driver {username}: {reserve_response.text}")

        # Step 4: Simulate arrivals and leaves only for successful reservations
        for reservation in successful_reservations:
            parking_lot_id = reservation["parkingLotId"]
            parking_spot_id = reservation["parkingSpotId"]

            # Simulate arrival
            arrival_data = {
                "parkingSpotId": parking_spot_id,
                "parkingLotId": parking_lot_id
            }

            arrive_response = requests.put(ARRIVE_ENDPOINT, json=arrival_data, headers=headers)
            if arrive_response.status_code != 200:
                print(f"Driver {username} failed to arrive at spot {parking_spot_id}: {arrive_response.text}")
            else:
                print(f"Driver {username} arrived at spot {parking_spot_id} in lot {parking_lot_id}.")

            # Simulate some delay before leaving
            time.sleep(random.uniform(0.1, 0.5))

            # Simulate leaving
            leave_response = requests.put(LEAVE_ENDPOINT, json=arrival_data, headers=headers)
            if leave_response.status_code != 200:
                print(f"Driver {username} failed to leave spot {parking_spot_id}: {leave_response.text}")
            else:
                print(f"Driver {username} left spot {parking_spot_id} in lot {parking_lot_id}.")
            successful_reservations=[]

if __name__ == "__main__":
    simulate_drivers_and_reservations(num_drivers=10, reservations_per_driver=10)
