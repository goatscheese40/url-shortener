# URL Shortener Application

This project is a Dockerized URL shortener application, consisting of:

- **Frontend**: Vite + React (served via Nginx)  
- **Backend**: Spring Boot (Java 21, Azul Zulu JDK)  
- **Database**: PostgreSQL  

Everything can be run with **Docker Compose**, no local installations required.

---

## Prerequisites

- [Docker](https://www.docker.com/get-started) installed  
- [Docker Compose](https://docs.docker.com/compose/install/) installed  

> No need to install Java, Gradle, Node, or NPM locally — all dependencies are handled by Docker.

---

## How to Build and Run Locally

1. **Clone the repository**:

```bash
git clone https://github.com/goatscheese40/url-shortener.git
cd url-shortener
```

2. **Start all services**:

```bash
docker-compose up --build
```

- This will build and start **Postgres**, **backend**, and **frontend** containers.  

3. **Access the frontend**:

```
http://localhost:5173
```

4. **Backend API**:

```
http://localhost:8080/api/url-shortener
```

5. **Stopping the application**:

```bash
docker-compose down
```

- Use `-v` if you want to remove database volumes as well:

```bash
docker-compose down -v
```

6. **Restarting the application**:

```bash
docker-compose up
```

---

## Example Usage

### Frontend

- Open the frontend UI at `http://localhost:5173`.
- Enter a full URL and optional custom alias to shorten it.
- Use the UI buttons to get, delete, or view all URLs.

### API

**Shorten a URL:**
```bash
curl -X POST http://localhost:8080/api/url-shortener/shorten \
     -H "Content-Type: application/json" \
     -d '{"fullUrl": "https://example.com/very/long/url", "customAlias": "myalias"}'
```

**Get Full URL by Alias:**
```bash
curl http://localhost:8080/api/url-shortener/myalias
```

**Delete a URL by Alias:**
```bash
curl -X DELETE http://localhost:8080/api/url-shortener/myalias
```

**Get All Shortened URLs:**
```bash
curl http://localhost:8080/api/url-shortener/urls
```

---

## Notes / Assumptions

- The backend uses **Postgres** inside Docker. Credentials are set in `docker-compose.yml`.  
- The frontend proxies `/api` requests to the backend automatically using Nginx.  
- No Gradle, Node, or Java installation is required locally.