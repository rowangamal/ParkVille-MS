import requests
import random
import string
import time

# Configuration
BASE_URL = "http://localhost:8080/api/managers"
SIGNUP_ENDPOINT = f"{BASE_URL}/signup"
LOGIN_ENDPOINT = f"{BASE_URL}/login"
CREATE_LOT_ENDPOINT = f"{BASE_URL}/create-parking-lot"

# Generate random data for simulation
def random_string(length=8):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def random_coordinates():
    return f"{random.uniform(-180, 180):.6f}", f"{random.uniform(-90, 90):.6f}"

def simulate_managers_and_lots(num_managers=10, lots_per_manager=5):
    for i in range(num_managers):
        # Step 1: Sign up manager
        username = random_string()
        email = f"{username}@example.com"
        password = random_string(12)

        signup_data = {
            "username": username,
            "email": email,
            "password": password
        }

        signup_response = requests.post(SIGNUP_ENDPOINT, json=signup_data)
        if signup_response.status_code != 200:
            print(f"Failed to sign up manager {username}: {signup_response.text}")
            continue

        print(f"Manager {username} signed up successfully.")

        # Step 2: Log in manager
        login_data = {
            "username": username,
            "password": password
        }

        login_response = requests.post(LOGIN_ENDPOINT, json=login_data)
        if login_response.status_code != 200:
            print(f"Failed to log in manager {username}: {login_response.text}")
            continue

        login_json = login_response.json()
        token = login_json.get("jwtToken")
        manager_id = login_json.get("id")
        role = login_json.get("role")

        if not token or not manager_id:
            print(f"Failed to retrieve token or ID for manager {username}.")
            continue

        print(f"Manager {username} logged in successfully with ID {manager_id} and role {role}.")

        # Step 3: Create parking lots
        headers = {"Authorization": f"Bearer {token}"}

        start_time = time.time()
        for j in range(lots_per_manager):
            longitude, latitude = random_coordinates()
            lot_data = {
                "longitude": longitude,
                "latitude": latitude,
                "pricePerHour": 20,
                "numberOfSlots": 50,
                # "pricePerHour": round(random.uniform(5, 50), 2),
                # "numberOfSlots": random.randint(10, 100),
                "parkingType": random.choice(["regular", "disabled", "ev charging"])
            }

            lot_response = requests.post(CREATE_LOT_ENDPOINT, json=lot_data, headers=headers)

            if lot_response.status_code != 201:
                print(f"Failed to create parking lot for manager {username}: {lot_response.text}")
            else:
                print(f"Parking lot {j+1} created for manager {username}.")
        end_time = time.time()
        # Add delay to simulate real-world usage
        # time.sleep(0.5)
        # print(f"Manager {username} created {lots_per_manager} parking lots in {end_time - start_time:.2f} seconds.")
        # print("On average {:.2f} seconds per parking lot".format((end_time - start_time) / lots_per_manager))

if __name__ == "__main__":
    simulate_managers_and_lots(num_managers=10, lots_per_manager=20)
