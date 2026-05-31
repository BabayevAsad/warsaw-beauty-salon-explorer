#  Warsaw Beauty Salon Explorer

<img width="2559" height="1522" alt="image" src="https://github.com/user-attachments/assets/43ad860b-674b-472e-84bb-d0d6ea7b9685" />


## 📝 Project Overview:
Warsaw Beauty Salon Explorer is a full-stack application designed to help users find and manage beauty salons in Warsaw. All data is collected directly from the Booksy website. It is built to be a simple, easy-to-use tool that lets you browse salons and manage their services.

The project uses a standard Model-View-Controller (MVC) structure, which keeps the code organized by separating the data-fetching part, the server-side logic, and the user interface.

<img width="2559" height="1530" alt="image" src="https://github.com/user-attachments/assets/d1e767da-245f-4d82-9277-9d159cfc903a" />

<img width="2559" height="1522" alt="image" src="https://github.com/user-attachments/assets/a91262c4-23c4-47a6-9b25-b913e75218be" />



## ⚙️ Core Functionality & CRUD Operations
The app allows you to view and edit salon information, while providing full control over managing services (treatments) offered by each salon.

Automated Data Collection: The app automatically gathers salon information from Booksy. I chose Booksy because its data is well-organized and reliable, making it perfect for an "Explorer" application.
RESTful API: This is the bridge that allows the web interface to request and update salon data.
Treatment CRUD: You can easily Add, Update, or Remove services from any salon catalog.

## 🌐 Data Acquisition & Parsing Technology
The project utilizes a specialized stack for web data extraction, focusing on parsing efficiency and structured data retrieval:

Jsoup: The primary engine for the project. It handles the entire lifecycle of data acquisition:

HTTP Networking: Executes secure GET requests while mimicking a real-world browser via custom User-Agent headers.

DOM Traversal: Converts raw HTML into a traversable Document Object Model (DOM), allowing for precise data extraction using CSS selectors.

Data Cleaning: Normalizes and sanitizes extracted HTML content into structured Java objects.


## 📋 Data Quality & Reliability
To make sure the application stays stable and the data stays clean, I implemented these safety measures:

Validation Layer: The system automatically checks that important fields (like names and addresses) are not missing or empty before saving them.

Data Cleaning: The "scraper" (data collector) automatically cleans up incoming data, replacing missing phone numbers with "N/A" and removing incomplete records that don't have 
enough information.

Global Exception Handling: If something goes wrong, the app sends a clean, simple error message instead of crashing or showing complex technical code.

## 🛡️ Backend Robustness & Testing

H2 In-Memory Database: I used an H2 database because it is very easy to set up. It doesn't require complex installation, making it perfect for quick demos.
<img width="784" height="573" alt="image" src="https://github.com/user-attachments/assets/4bfb947c-8846-479b-8b4c-8d1d6465a311" />


Pagination: I implemented pagination across all list endpoints to ensure high performance, prevent server overload, and improve load times as the dataset grows.

Search Functionality: Added robust search capabilities allowing users to filter salons by Name and Address. The implementation supports partial matching and case-insensitive queries, seamlessly integrated with pagination to ensure efficient data retrieval.

CORS Enabled: This ensures the frontend and backend can talk to each other securely.

Testing Strategy: I wrote "Unit Tests" to check individual parts of the logic and "Integration Tests" to ensure the API works perfectly with the backend service.

## ⚙️ Configuration
You can adjust the number of salons fetched by the application in the src/main/resources/application.properties file. 
* (salon.fetch.limit=20)

<img width="1813" height="864" alt="image" src="https://github.com/user-attachments/assets/9403e7a1-9556-404a-aac5-afef215127a9" />


## 🛠️ Core Technologies:

Java 21

Spring Boot 4.0.6 

Spring Web MVC 

Spring Data JPA 

H2 Database

Spring Security

Spring Validation

JSoup (v1.17.2)

Frontend: React


## ⚙️ Setup & Installation
Prerequisites
JDK 21

Maven 3.x

Node.js (for React)


# Instructions to run application:
Clone the Repository:

git clone https://github.com/BabayevAsad/warsaw-beauty-salon-explorer.git
cd warsaw-beauty-salon-explorer

Build and Run Backend:  (port:8080)

Bash:
* mvn clean install
* mvn test
* mvn spring-boot:run   

Run Frontend (React + Vite):

Bash:
* cd frontend
* npm install
* npm run dev -- --port 5174


Access the App:
* H2 Console: http://localhost:8080/h2-console
* API Endpoints: http://localhost:8080/rest/api/salons
* Frontend: http://localhost:5174



## 🚀 Future Improvements & Scaling

National Scaling: Currently, the app only covers Warsaw. By simply updating the URL parameters from "warsaw" to Poland, we can easily scale the application to access hair salons across all of Poland.

Security: I will add "Spring Security" with JWT tokens. This will allow an Administrator to securely log in to add or remove salons, while regular users can only search and view.

Database Upgrade: As the data grows to cover all of Poland, I will switch from the H2 memory database to a professional database like PostgreSQL for better performance.

Advanced Search: I want to add better search filters so users can find salons by price range or specific services more easily.

Automatic Updates: I plan to add scheduled tasks that automatically refresh salon ratings and prices from Booksy periodically.

## I stayed within the borders of the requirements, though there are certainly more features that could be applied to the application in the future.

## 📧 Contact
* Developer: Asad Babayev
* Email: asad_babayev@outlook.com
