# 🔗 URL Shortener - Efficient Link Management

> A high-performance URL shortening service with analytics, custom aliases, and real-time tracking. Built with Java for scalability and reliability.

[![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=flat&logo=mongodb&logoColor=white)](https://mongodb.com)

---

## 🎯 Project Overview

URL Shortener is a robust backend service designed to convert long URLs into short, shareable links. With built-in analytics, custom alias support, and high availability, it's perfect for enterprises and content creators.

### Key Features

🔗 **URL Shortening**
- Convert long URLs to compact short links
- Custom alias support
- Automatic slug generation
- Expiration management

📊 **Analytics Dashboard**
- Click tracking and statistics
- Visitor demographics
- Device and browser analytics
- Time-series tracking

⚙️ **Advanced Features**
- QR code generation
- Link expiration
- Password protection
- Rate limiting

🔐 **Security**
- Input validation
- URL verification
- DDoS protection
- User authentication

---

## 🛠️ Tech Stack

### Backend
- **Java 11+** - Programming language
- **Spring Boot** - Framework
- **Spring Data MongoDB** - Data access
- **Spring Security** - Authentication
- **Maven** - Build tool

### Database
- **MongoDB** - NoSQL database
- **Redis** - Caching layer

### Additional Libraries
- **Lombok** - Boilerplate reduction
- **Jackson** - JSON processing
- **JUnit** - Testing

---

## 🚀 Getting Started

### Prerequisites
- Java 11+
- Maven 3.6+
- MongoDB
- Redis (optional)

### Installation

```bash
# Clone the repository
git clone https://github.com/yogi941/urlShorterner.git
cd urlShorterner

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Configuration

Create `application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/urlshortener
spring.application.name=urlshortener
server.port=8080

# Redis (optional)
spring.redis.host=localhost
spring.redis.port=6379

# JWT Configuration
app.jwt.secret=your_secret_key
app.jwt.expiration=86400000

# Base URL for shortened links
app.base.url=http://localhost:8080
```

---

## 📁 Project Structure

```
urlShorterner/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yogi/urlshortener/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── security/
│   │   │       └── config/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## 🔄 API Endpoints

### URL Operations

**Create Short URL**
```http
POST /api/v1/urls/shorten
Content-Type: application/json

{
  "originalUrl": "https://example.com/very/long/url",
  "customAlias": "mylink",  // optional
  "expirationDate": "2024-12-31"  // optional
}

Response:
{
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123",
  "originalUrl": "https://example.com/very/long/url",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Redirect to Original URL**
```http
GET /abc123

Response: 301 Redirect to original URL
```

**Get URL Details**
```http
GET /api/v1/urls/abc123

Response:
{
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123",
  "originalUrl": "https://example.com/very/long/url",
  "clicks": 150,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Analytics

**Get Click Statistics**
```http
GET /api/v1/analytics/abc123

Response:
{
  "shortCode": "abc123",
  "totalClicks": 150,
  "uniqueClicks": 120,
  "clicksByDate": [...],
  "topReferrers": [...],
  "topDevices": [...]
}
```

### User Management

**Register User**
```http
POST /api/v1/auth/register
```

**Login**
```http
POST /api/v1/auth/login
```

---

## 🗄️ Database Schema

### URLs Collection
```javascript
{
  _id: ObjectId,
  shortCode: String (unique),
  originalUrl: String,
  customAlias: String,
  userId: ObjectId,
  createdAt: Date,
  expirationDate: Date,
  isActive: Boolean,
  clicks: Number,
  password: String (hashed, optional)
}
```

### Analytics Collection
```javascript
{
  _id: ObjectId,
  shortCode: String,
  clickTime: Date,
  referrer: String,
  userAgent: String,
  ipAddress: String,
  device: String,
  browser: String,
  country: String
}
```

---

## 🔒 Security Features

- ✅ JWT authentication
- ✅ Password hashing with bcrypt
- ✅ Input validation and sanitization
- ✅ Rate limiting per IP
- ✅ HTTPS support
- ✅ CORS configuration
- ✅ SQL injection prevention
- ✅ Password-protected short URLs

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UrlServiceTest

# Generate coverage report
mvn jacoco:report
```

---

## 📊 Performance Optimization

- **Caching:** Redis for frequently accessed URLs
- **Indexing:** MongoDB indexes on shortCode and userId
- **Connection Pooling:** Optimized database connections
- **Async Processing:** Non-blocking I/O for analytics
- **Compression:** GZIP compression for responses

---

## 📈 Scaling Considerations

- Horizontal scaling with load balancer
- Database sharding by shortCode prefix
- CDN for static content
- Redis cluster for distributed caching
- Kafka for event streaming (future)

---

## 🚀 Deployment

### Docker

```bash
# Build Docker image
docker build -t urlshortener .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/urlshortener \
  urlshortener
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: urlshortener
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: urlshortener
        image: urlshortener:latest
        ports:
        - containerPort: 8080
```

---

## 🐛 Common Issues

**Duplicate Short Code**
- Short codes are generated using Base62 encoding
- Collision probability is extremely low

**URL Not Redirecting**
- Check if URL is expired
- Verify custom alias is correct
- Check database connection

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/Enhancement`)
3. Commit changes (`git commit -m 'Add Enhancement'`)
4. Push to branch (`git push origin feature/Enhancement`)
5. Open Pull Request

---

## 📝 License

MIT License - see LICENSE file

---

## 📞 Contact

- 📧 Email: [your-email@example.com]
- 🐦 Twitter: [@yogi941]
- 💬 Discussions: [GitHub Discussions]
- 🐛 Issues: [Report Issues]

---

## 🌟 Acknowledgments

- Spring Boot community
- MongoDB team
- Contributors and testers

---

**Shorten links, expand possibilities! 🚀**
