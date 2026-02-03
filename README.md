# Weather Application

Web application for tracking current weather in your saved locations. Register, add cities, and view real-time weather data.

## Features

- User registration and authentication
- Search locations by name
- Add/remove locations to personal collection
- View current temperature for saved locations

## Tech Stack
Java • Spring MVC • Spring Data JPA • Hibernate • PostgreSQL • Caffeine cache • Liquibase • Thymeleaf • Bootstrap 5

## Pages

**Main Page**
- Header with login/register buttons (or username + logout for authenticated users)
- Search field for locations
- List of saved locations with temperature and delete button

**Search Results**
- Same header and search field
- List of found locations with "Add" button

**Auth Pages**
- Registration form
- Login form

## Authentication

Manual session and cookie management (no Spring Security):
- On login, backend creates session with unique ID and sets it in cookies
- Session stores user ID and expiration time
- Each request validates session from cookies
- Custom session handling for educational purposes

## External API Integration

The application integrates with OpenWeatherMap API using Spring's RestTemplate:

1. **Location Search** - RestTemplate sends GET request to Geocoding API with location name, receives JSON array of matching locations with coordinates
2. **Weather Retrieval** - RestTemplate fetches current weather data by passing latitude/longitude to Current Weather API
3. **Response Mapping** - Jackson automatically deserializes JSON responses into Java DTOs
4. **Caching** - Weather data is cached using Caffeine to minimize API calls and stay within rate limits (60 req/min)