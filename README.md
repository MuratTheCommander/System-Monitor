# 🖥️ System Monitor (Dockerized Microservice Architecture)

This project demonstrates a **Java Spring Boot microservice system** for monitoring CPU, memory, disk, and Wi-Fi usage. The services are containerized with Docker and visualized using **Prometheus + Grafana**.

> 🎓 Built for educational purposes to explore modern DevOps tools and microservice patterns.

---

##  Architecture Overview

- **CPU Service** – Reports CPU usage
- **Memory Service** – Reports memory usage
- **Disk Service** – ⚠️ *Currently not working as expected*
- **Wi-Fi Service** – ⚠️ *Currently not working as expected*
- **Frontend** – Displays metrics and embedded dashboards
- **Prometheus** – Scrapes and stores metrics from services
- **Grafana** – Visualizes metrics

All components are orchestrated using **Docker Compose**.

---

## 🚀 Getting Started

### 1. Clone the Project
git clone https://github.com/YOUR_USERNAME/system-monitor-github.git
cd system-monitor-github

### 2. Configure Environment

Copy the example .env file:

cp .env.example .env

Then edit the .env file and set your own secure password:

GRAFANA_ADMIN_USER=admin
GRAFANA_ADMIN_PASSWORD=your_secure_password

### 3.Start All Services

docker compose up -d

Accessing the System

| Component  | URL                                            |
| ---------- | ---------------------------------------------- |
| Frontend   | [http://localhost:8080](http://localhost:8080) |
| Grafana    | [http://localhost:3000](http://localhost:3000) |
| Prometheus | [http://localhost:9090](http://localhost:9090) |

⚠️ Known Limitations

* The Wi-Fi Service and Disk Service are included but currently not functional.

**📊 Grafana Dashboard Setup (Manual)**
Grafana and Prometheus are pre-connected, but dashboards must be created manually. You're free to design your own panels to visualize system metrics like CPU, memory, disk, and Wi-Fi.

**Where to Find Metric Definitions**
Each service defines its own metrics using Micrometer. You can inspect these files to see exactly which metrics are exposed:

| Resource   | Java Source File (Metrics)                                                          |
| ---------- | ----------------------------------------------------------------------------------- |
| **CPU**    | `cpu-service/src/main/java/com/example/cpu/service/CpuMetricsService.java`          |
| **Memory** | `memory-service/src/main/java/com/example/memory/service/MemoryMetricsService.java` |
| **Disk**   | `disk-service/src/main/java/com/example/disk/service/DiskMetricsService.java`       |
| **Wi-Fi**  | `wifi-service/src/main/java/com/example/wifi/service/WifiMetricsService.java`       |

Use these files as a reference to build custom Grafana panels for each metric.

**🛠️ How to Set Up Dashboards**
1. Go to http://localhost:3000
2. Log in using your .env credentials
3. Add a Prometheus data source:
   Name: Prometheus
   URL: http://prometheus:9090
   Save and test
4. Create dashboards using Prometheus metric names found in the files above
5. Choose visual types like time series, gauge, or stat
6. Customize titles, refresh rates, and colors as needed

**🌐 Embedding Dashboards**
To integrate Grafana dashboards into the frontend:
1. In Grafana, click on any panel’s title → Share
2. Go to the Embed tab
3. Copy the <iframe> code
4. Paste it into the appropriate file in the Frontend Service (e.g., frontend/templates/index.html) for display

💡 This setup lets you visualize only what you need, how you want — the project encourages hands-on customization of dashboards.

🛠️ Tech Stack
* Java 17 / Spring Boot
* Docker & Docker Compose
* Prometheus
* Grafana
* HTML/CSS (Frontend)

📝 License
This project was created for **educational and demonstration purposes**.
It is licensed under the [MIT License](LICENSE).

> ⚠️ You are free to use, modify, and distribute this software under the terms of the MIT License.  
> However, the authors are **not liable** for any damages, losses, or issues arising from the use of this code in any context, including commercial applications.  
> Use it **at your own risk**.












