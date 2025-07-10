# ♻️ EcoPulse – Smart Waste Management System

EcoPulse is an AI-powered IoT waste monitoring solution that tracks dustbin fill levels in real-time and helps authorities or organizations optimize waste collection efficiently.

🏆 *Top 6 Finalist – HexaFalls Hackathon by JIS University*

---

## 🌟 Key Features

- 📶 **Real-Time Monitoring**: Measures fill level using ultrasonic sensors and updates live via **Server-Sent Events (SSE)**.
- 📡 **IoT Integration**: Each bin (ESP32-based) transmits data over HTTP/MQTT to a Spring Boot backend.
- 🧑‍💼 **Role-Based Dashboard**: Manage users and groups with Admin/Viewer roles.
- 📊 **Detailed Analytics**: Track fill history, average trends, last emptied time, and more.
- ⚙️ **Custom Configurable Devices**: WiFi, device ID, and bin depth setup via local web portal.
- 🔒 **Secure Auth**: JWT-based authentication for users and group-based access.

---

## 🔧 Tech Stack

### ⚙️ Backend
- **Spring Boot** (REST API + SSE endpoints)
- **PostgreSQL** (data storage)
- **Spring Security + JWT** (auth & access control)
- **SSE (Server-Sent Events)** for real-time frontend updates
- **MQTT (Port 8883)** and/or HTTP ingestion from ESP32
- **NVS/Preferences** used in ESP for persistent config

### 💻 Frontend

- Built using **React**  
- Uses **SSE** for real-time fill level updates  
- Displays live status, charts, device analytics, and group dashboards

🔗 **Frontend Repo**: [EcoMonitor-Frontend (React)](https://github.com/Snedit/EcoPulse-frontend.git) <!-- Replace this with actual URL -->

### 📲 IoT Device

- **ESP32 / NodeMCU**
- **HC-SR04** Ultrasonic Sensor
- **Li-ion Battery** (2S with buck/boost converter)
- On-boot fallback to **WiFi Setup Mode (AP)** with Web Portal
- Sends data over secure MQTT or HTTP to the server

---

