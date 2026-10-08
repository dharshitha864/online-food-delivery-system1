# 🍔 QuickBite - Online Food Delivery System

A complete, production-ready, full-stack **Online Food Delivery System** built with **Java 17**, **Spring Boot 3**, **Maven**, and a modern responsive web frontend (**HTML5, CSS3, JavaScript**).

Created specifically for an **Agile / DevOps College Project** following the exact industry workflow:
```
VS Code  ──►  GitHub  ──►  Jenkins CI  ──►  Maven (Build & Test)
```

---

## 📋 Table of Contents
1. [Project Overview](#-project-overview)
2. [DevOps Workflow (VS Code → GitHub → Jenkins → Maven)](#-devops-workflow)
3. [Required Project Structure](#-required-project-structure)
4. [Technology Stack](#-technology-stack)
5. [Architecture & Components](#-architecture--components)
6. [Quick Start & Running Locally](#-quick-start--running-locally)
7. [Running from VS Code](#-running-from-vs-code)
8. [Maven Commands & Testing](#-maven-commands--testing)
9. [REST API Documentation](#-rest-api-documentation)
10. [Web Application Features](#-web-application-features)
11. [Jenkins CI/CD Pipeline Configuration](#-jenkins-cicd-pipeline-configuration)

---

## 🌟 Project Overview

**QuickBite** is an interactive, fully working online food delivery web platform. Customers can explore top restaurants, browse delicious menus by cuisine and category, search for specific dishes in real time, customize and manage their shopping cart, and place orders with instant validation and simulated live delivery tracking.

- **No external database required:** Powered by high-performance, thread-safe in-memory Java collections (`ConcurrentHashMap`, `AtomicInteger`, synchronized lists).
- **Embedded Web Server:** Runs locally out-of-the-box on Spring Boot's embedded Tomcat server at **`http://localhost:8081`** (avoiding port conflicts with Jenkins on 8080).
- **Comprehensive Automated Testing:** 14 automated tests covering Spring context loading, MockMvc REST API validation, and full business logic.

---

## 🔄 DevOps Workflow

```
┌─────────────┐       git push       ┌─────────────┐     webhook / poll     ┌─────────────┐
│   VS Code   │ ───────────────────► │   GitHub    │ ─────────────────────► │   Jenkins   │
│ (Developer) │                      │(Repository) │                        │ (CI Server) │
└─────────────┘                      └─────────────┘                        └──────┬──────┘
                                                                                   │
                                                                   mvn clean test  │
                                                                   mvn package     ▼
                                                                            ┌─────────────┐
                                                                            │    Maven    │
                                                                            │BUILD SUCCESS│
                                                                            └─────────────┘
```

1. **VS Code:** Developer edits code, runs `App.java` locally, verifies UI at `http://localhost:8080`, and executes `mvn clean test`.
2. **GitHub:** Developer pushes code commits and pull requests to GitHub remote repository (`main` or feature branch).
3. **Jenkins:** Jenkins triggers on commit or poll SCM, pulling the latest code into its build workspace.
4. **Maven:** Jenkins runs the automated build:
   - `mvn compile`
   - `mvn clean test` (All 14 tests execute and report XML test results)
   - `mvn package` (Produces executable JAR in `target/`)
   - **Result:** `BUILD SUCCESS` 🎉

---

## 📁 Required Project Structure

```
online-food-delivery-system/
│
├── pom.xml                                   # Complete Maven build configuration (Java 17, Spring Boot 3)
├── README.md                                 # Complete documentation & DevOps guide
├── .gitignore                                # Git ignore rules for Java, Maven, VS Code
├── Jenkinsfile                               # Declarative Jenkins CI/CD pipeline script
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── fooddelivery/
    │   │           ├── App.java                 # Main Spring Boot entry point (main() method)
    │   │           ├── FoodItem.java            # Domain model: Food item details & pricing
    │   │           ├── Restaurant.java          # Domain model: Restaurant entity & menu list
    │   │           ├── Order.java               # Domain model: Order, items, and billing totals
    │   │           ├── FoodDeliveryService.java # Business logic service with in-memory sample data
    │   │           └── FoodController.java      # Spring REST API Controller (@RestController)
    │   │
    │   └── resources/
    │       ├── application.properties        # Spring Boot configuration (server.port=8080)
    │       └── static/
    │           ├── index.html                   # Modern QuickBite UI markup
    │           ├── style.css                    # Professional responsive styling & animations
    │           └── script.js                    # Dynamic frontend logic & API client
    │
    └── test/
        └── java/
            └── com/
                └── fooddelivery/
                    └── AppTest.java             # JUnit 5 & Spring Boot MockMvc test suite
```

---

## 🛠️ Technology Stack

| Layer | Technology | Version | Purpose |
|---|---|---|---|
| **Language** | Java | 17 LTS / 21 LTS | Core backend programming language |
| **Framework** | Spring Boot | 3.2.5 | Web application framework and embedded server |
| **Starter** | `spring-boot-starter-web` | 3.2.5 | Embedded Tomcat, Spring MVC, Jackson JSON |
| **Build Tool** | Apache Maven | 3.9+ | Dependency management, compilation, and packaging |
| **Testing** | JUnit 5 & MockMvc | Latest (via starter-test) | Unit and integration testing |
| **Frontend** | HTML5 / CSS3 / Vanilla JS | ES6+ | Responsive customer interface served by Spring Boot |
| **IDE** | Visual Studio Code | Latest | Code editing, debugging, Git integration |
| **CI / CD** | Jenkins | 2.4+ | Automated continuous integration pipeline |

---

## 🏛️ Architecture & Components

### 1. `App.java`
- Contains `public static void main(String[] args)` annotated with `@SpringBootApplication`.
- Launches the Spring Boot application context: `SpringApplication.run(App.class, args)`.
- Serves static assets from `src/main/resources/static/` at `http://localhost:8080/`.

### 2. `FoodItem.java`
- Models individual menu items: `id`, `name`, `category`, `price`, `restaurantName`, `description`, and `emoji`.

### 3. `Restaurant.java`
- Models dining partners: `id`, `name`, `cuisine`, `rating`, `deliveryTime`, and associated `List<FoodItem>`.

### 4. `Order.java` & `OrderItem`
- Represents customer orders: `orderId` (`QB-XXXX`), `customerName`, `customerPhone`, `deliveryAddress`, `paymentMethod`, `items`, `subtotal`, `deliveryFee` (Free on orders $30+), `tax` (8%), and `totalAmount`.

### 5. `FoodDeliveryService.java`
- Spring `@Service` managing thread-safe in-memory data structures.
- Pre-populated on startup with 5 realistic restaurants and diverse menus:
  1. **Pizza Palace** (*Italian & Pizzeria • 4.8★*)
  2. **Burger House** (*American Burgers & Grill • 4.6★*)
  3. **South Indian Kitchen** (*Authentic South Indian • 4.9★*)
  4. **Chinese Wok** (*Pan-Asian & Chinese • 4.5★*)
  5. **Healthy Bites** (*Salads, Bowls & Organic • 4.7★*)

### 6. `FoodController.java`
- Spring `@RestController` providing REST endpoints at `/api/*` with full CORS support.

---

## 🚀 Quick Start & Running Locally

### Prerequisites
- **Java 17 or Java 21** installed (`java -version`)
- **Apache Maven 3.9+** installed (`mvn -version`)

### 1. Clone or Open the Repository
```bash
git clone https://github.com/<your-username>/online-food-delivery-system.git
cd online-food-delivery-system
```

### 2. Run the Application with Maven
```bash
mvn spring-boot:run
```

### 3. Open in Browser
Open your browser and navigate to:
```
http://localhost:8081
```

---

## 💻 Running from VS Code

1. Open **Visual Studio Code**.
2. Select **File → Open Folder...** and choose the `online-food-delivery-system` folder.
3. Ensure the recommended extensions are installed:
   - **Extension Pack for Java** (by Microsoft)
   - **Spring Boot Extension Pack** (by VMware)
4. Open `src/main/java/com/fooddelivery/App.java`.
5. Click **Run** or press `F5` / `Ctrl+F5`.
6. The Spring Boot application starts in the integrated terminal:
   ```
   :: Spring Boot ::                (v3.2.5)
   Tomcat started on port 8081 (http) with context path ''
   ```
7. Open **`http://localhost:8081`** in Chrome, Edge, or Firefox.

---

## 🧪 Maven Commands & Testing

Execute these standard Maven commands from the project root:

### Clean and Run All Tests
```bash
mvn clean test
```
**Expected Output:**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.fooddelivery.AppTest
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Package into Executable JAR
```bash
mvn clean package
```

### Run the Packaged JAR
```bash
java -jar target/online-food-delivery-system-1.0.0.jar
```

---

## 📡 REST API Documentation

| HTTP Method | Endpoint | Description | Response Code |
|---|---|---|---|
| `GET` | `/api/restaurants` | Retrieve all 5 featured restaurants with menus | `200 OK` |
| `GET` | `/api/restaurants/{id}` | Retrieve single restaurant by ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/foods` | Retrieve food items (supports `?search=...` and `?category=...`) | `200 OK` |
| `GET` | `/api/foods/{id}` | Retrieve single food item by ID | `200 OK` / `404 Not Found` |
| `POST` | `/api/orders` | Place a new order with items and customer details | `201 Created` |
| `GET` | `/api/orders` | Retrieve order history | `200 OK` |
| `GET` | `/api/orders/{id}` | Retrieve order details by Order ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/orders/{id}/status` | Update order delivery status | `200 OK` |
| `GET` | `/api/cart` | Get current active cart summary and totals | `200 OK` |
| `POST` | `/api/cart/add` | Add item and quantity to cart | `200 OK` |
| `DELETE` | `/api/cart/clear` | Clear cart | `200 OK` |
| `GET` | `/api/health` | Health check endpoint returning platform status | `200 OK` |

### Sample POST `/api/orders` Payload:
```json
{
  "customerName": "Jane Doe",
  "customerPhone": "555-4321",
  "deliveryAddress": "456 Elm Street, Apt 3B",
  "paymentMethod": "Cash on Delivery",
  "items": [
    { "id": 101, "quantity": 2 },
    { "id": 201, "quantity": 1 }
  ]
}
```

---

## 🎨 Web Application Features

1. **Header & Navigation:** QuickBite logo, quick links, search box, and reactive Cart icon with item badge.
2. **Hero Banner:** Eye-catching design highlighting 30-min guaranteed delivery, customer satisfaction ratings, and Free Delivery threshold.
3. **Restaurant Partner Cards:** Interactive cards showing cuisine type, delivery time, star rating, and direct "View Menu" filter.
4. **Category Filter Pills:** One-click filtering by *All, Pizza, Burgers, South Indian, Chinese, Healthy, Sides, Beverages, Desserts*.
5. **Interactive Menu Grid:** Food cards with dish pictures, tags, descriptions, pricing, and dynamic "+ Add to Cart" / quantity controls.
6. **Cart Drawer Modal:** Slide-out drawer with real-time subtotal, 8% tax calculation, dynamic delivery fee ($2.99 or FREE for $30+), and checkout button.
7. **Checkout Modal:** Fast form for name, phone, delivery address, and payment method selection.
8. **Confirmation & Receipt:** Instant order confirmation showing order ID (`QB-XXXX`), formatted receipt, and estimated delivery timer.
9. **Order History Section:** Live persistent session log of placed orders.

---

## ⚙️ Jenkins CI/CD Pipeline Configuration

The repository includes a ready-to-run **`Jenkinsfile`**.

### How to Configure in Jenkins:
1. Log in to Jenkins (`http://localhost:8080` or your Jenkins server).
2. Click **New Item** → Select **Pipeline** → Name it `quickbite-food-delivery-ci`.
3. In Pipeline configuration:
   - **Definition:** Pipeline script from SCM
   - **SCM:** Git
   - **Repository URL:** `https://github.com/<your-username>/online-food-delivery-system.git`
   - **Branch Specifier:** `*/main`
   - **Script Path:** `Jenkinsfile`
4. Under **Build Triggers**, select **GitHub hook trigger for GITScm polling** (or Poll SCM).
5. Click **Save** and **Build Now**.
6. Jenkins executes all stages: **Checkout → Compile → Unit & Integration Tests → Package → Archive Artifacts** with result: `BUILD SUCCESS`!

---

## 📄 License
This project is developed for educational and academic Agile/DevOps demonstration purposes.
