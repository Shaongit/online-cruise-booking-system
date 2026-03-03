# Database Diagram - Online Cruise Booking System

## Table of Contents
1. [User Authentication & Registration Module](#user-authentication--registration-module)
2. [Cruise Reservation Module](#cruise-reservation-module)
3. [Payment Integration Module](#payment-integration-module)
4. [Modify & Cancel Reservation Module](#modify--cancel-reservation-module)
5. [Customer Profile Management Module](#customer-profile-management-module)

---

## User Authentication & Registration Module

### Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ USER_ROLES : has
    ROLES ||--o{ USER_ROLES : assigned_to
    USERS ||--o{ PASSWORD_RESET_TOKENS : requests
    USERS ||--o{ USER_SESSIONS : creates

    USERS {
        bigint user_id PK
        varchar email UK
        varchar username UK
        varchar password_hash
        varchar first_name
        varchar last_name
        varchar phone_number
        date date_of_birth
        varchar address
        varchar city
        varchar state
        varchar postal_code
        varchar country
        boolean is_active
        boolean email_verified
        timestamp created_at
        timestamp updated_at
        timestamp last_login
    }

    ROLES {
        int role_id PK
        varchar role_name UK
        varchar description
        timestamp created_at
    }

    USER_ROLES {
        bigint user_role_id PK
        bigint user_id FK
        int role_id FK
        timestamp assigned_at
    }

    PASSWORD_RESET_TOKENS {
        bigint token_id PK
        bigint user_id FK
        varchar token UK
        timestamp expires_at
        boolean is_used
        timestamp created_at
    }

    USER_SESSIONS {
        bigint session_id PK
        bigint user_id FK
        varchar session_token UK
        varchar ip_address
        varchar user_agent
        timestamp login_at
        timestamp expires_at
        boolean is_active
    }
```

---

## Table Definitions

### 1. USERS Table
**Purpose:** Stores user account information for both sign-in and registration

| Column Name      | Data Type      | Constraints                    | Description                          |
|------------------|----------------|--------------------------------|--------------------------------------|
| user_id          | BIGINT         | PRIMARY KEY, AUTO_INCREMENT    | Unique identifier for each user      |
| email            | VARCHAR(255)   | UNIQUE, NOT NULL               | User's email address (used for login)|
| username         | VARCHAR(50)    | UNIQUE, NOT NULL               | Unique username (optional login)     |
| password_hash    | VARCHAR(255)   | NOT NULL                       | Bcrypt/Argon2 hashed password        |
| first_name       | VARCHAR(100)   | NOT NULL                       | User's first name                    |
| last_name        | VARCHAR(100)   | NOT NULL                       | User's last name                     |
| phone_number     | VARCHAR(20)    | NULL                           | Contact phone number                 |
| date_of_birth    | DATE           | NULL                           | User's date of birth                 |
| address          | VARCHAR(255)   | NULL                           | Street address                       |
| city             | VARCHAR(100)   | NULL                           | City                                 |
| state            | VARCHAR(100)   | NULL                           | State/Province                       |
| postal_code      | VARCHAR(20)    | NULL                           | Postal/ZIP code                      |
| country          | VARCHAR(100)   | NULL                           | Country                              |
| is_active        | BOOLEAN        | DEFAULT TRUE                   | Account active status                |
| email_verified   | BOOLEAN        | DEFAULT FALSE                  | Email verification status            |
| created_at       | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP      | Account creation timestamp           |
| updated_at       | TIMESTAMP      | ON UPDATE CURRENT_TIMESTAMP    | Last update timestamp                |
| last_login       | TIMESTAMP      | NULL                           | Last successful login                |

**Indexes:**
- PRIMARY KEY: `user_id`
- UNIQUE INDEX: `email`
- UNIQUE INDEX: `username`
- INDEX: `email_verified`, `is_active`

---

### 2. ROLES Table
**Purpose:** Defines user roles (Customer, Admin, Staff, etc.)

| Column Name | Data Type    | Constraints                 | Description                    |
|-------------|--------------|----------------------------|--------------------------------|
| role_id     | INT          | PRIMARY KEY, AUTO_INCREMENT| Unique role identifier         |
| role_name   | VARCHAR(50)  | UNIQUE, NOT NULL           | Role name (CUSTOMER, ADMIN)    |
| description | VARCHAR(255) | NULL                       | Role description               |
| created_at  | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP  | Role creation timestamp        |

**Default Roles:**
- `CUSTOMER` - Regular cruise booking customers
- `ADMIN` - System administrators
- `STAFF` - Cruise company staff
- `MANAGER` - Cruise managers

**Indexes:**
- PRIMARY KEY: `role_id`
- UNIQUE INDEX: `role_name`

---

### 3. USER_ROLES Table
**Purpose:** Many-to-many relationship between users and roles

| Column Name   | Data Type | Constraints                       | Description                      |
|---------------|-----------|-----------------------------------|----------------------------------|
| user_role_id  | BIGINT    | PRIMARY KEY, AUTO_INCREMENT       | Unique identifier                |
| user_id       | BIGINT    | FOREIGN KEY (USERS.user_id)       | Reference to user                |
| role_id       | INT       | FOREIGN KEY (ROLES.role_id)       | Reference to role                |
| assigned_at   | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP         | Role assignment timestamp        |

**Indexes:**
- PRIMARY KEY: `user_role_id`
- UNIQUE INDEX: `user_id`, `role_id` (composite)
- FOREIGN KEY: `user_id` REFERENCES `USERS(user_id)` ON DELETE CASCADE
- FOREIGN KEY: `role_id` REFERENCES `ROLES(role_id)` ON DELETE CASCADE

---

### 4. PASSWORD_RESET_TOKENS Table
**Purpose:** Manages password reset requests

| Column Name | Data Type    | Constraints                          | Description                        |
|-------------|--------------|--------------------------------------|------------------------------------|
| token_id    | BIGINT       | PRIMARY KEY, AUTO_INCREMENT          | Unique token identifier            |
| user_id     | BIGINT       | FOREIGN KEY (USERS.user_id)          | User requesting reset              |
| token       | VARCHAR(255) | UNIQUE, NOT NULL                     | Reset token (UUID/random string)   |
| expires_at  | TIMESTAMP    | NOT NULL                             | Token expiration time              |
| is_used     | BOOLEAN      | DEFAULT FALSE                        | Whether token has been used        |
| created_at  | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP            | Token creation timestamp           |

**Indexes:**
- PRIMARY KEY: `token_id`
- UNIQUE INDEX: `token`
- FOREIGN KEY: `user_id` REFERENCES `USERS(user_id)` ON DELETE CASCADE
- INDEX: `expires_at`, `is_used`

---

### 5. USER_SESSIONS Table
**Purpose:** Tracks user login sessions for security

| Column Name    | Data Type    | Constraints                       | Description                        |
|----------------|--------------|-----------------------------------|------------------------------------|
| session_id     | BIGINT       | PRIMARY KEY, AUTO_INCREMENT       | Unique session identifier          |
| user_id        | BIGINT       | FOREIGN KEY (USERS.user_id)       | User who created session           |
| session_token  | VARCHAR(255) | UNIQUE, NOT NULL                  | JWT or session token               |
| ip_address     | VARCHAR(45)  | NULL                              | User's IP address (IPv6 compatible)|
| user_agent     | VARCHAR(255) | NULL                              | Browser/device information         |
| login_at       | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP         | Login timestamp                    |
| expires_at     | TIMESTAMP    | NOT NULL                          | Session expiration time            |
| is_active      | BOOLEAN      | DEFAULT TRUE                      | Session active status              |

**Indexes:**
- PRIMARY KEY: `session_id`
- UNIQUE INDEX: `session_token`
- FOREIGN KEY: `user_id` REFERENCES `USERS(user_id)` ON DELETE CASCADE
- INDEX: `user_id`, `is_active`

---

## User Authentication Workflows

### 1. User Registration Flow

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Backend
    participant Database

    User->>Frontend: Fill registration form
    Frontend->>Backend: POST /api/auth/register
    Backend->>Backend: Validate input data
    Backend->>Backend: Hash password
    Backend->>Database: INSERT INTO USERS
    Database-->>Backend: user_id
    Backend->>Database: INSERT INTO USER_ROLES (CUSTOMER role)
    Backend->>Backend: Generate verification email
    Backend-->>Frontend: Success response
    Frontend-->>User: Registration successful
```

**Steps:**
1. User provides: email, username, password, first_name, last_name, etc.
2. Backend validates:
   - Email format and uniqueness
   - Username availability
   - Password strength (min 8 chars, complexity rules)
3. Password is hashed using bcrypt/Argon2
4. User record created with `is_active=true`, `email_verified=false`
5. Default role `CUSTOMER` assigned in USER_ROLES
6. Verification email sent (optional)
7. Success response returned

---

### 2. User Sign-In Flow

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Backend
    participant Database

    User->>Frontend: Enter email & password
    Frontend->>Backend: POST /api/auth/login
    Backend->>Database: SELECT FROM USERS WHERE email=?
    Database-->>Backend: User record
    Backend->>Backend: Verify password hash
    Backend->>Backend: Check is_active status
    Backend->>Database: UPDATE last_login timestamp
    Backend->>Backend: Generate JWT/session token
    Backend->>Database: INSERT INTO USER_SESSIONS
    Backend-->>Frontend: Token + user info
    Frontend-->>User: Redirect to dashboard
```

**Steps:**
1. User provides: email/username and password
2. Backend queries USERS table by email/username
3. Verify password hash matches
4. Check `is_active=true` and optionally `email_verified=true`
5. Update `last_login` timestamp
6. Generate JWT token or session token
7. Create session record in USER_SESSIONS
8. Return token and user profile to frontend

---

## SQL Schema Creation Scripts

### Create USERS Table
```sql
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_active_verified (is_active, email_verified)
);
```

### Create ROLES Table
```sql
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_role_name (role_name)
);

-- Insert default roles
INSERT INTO roles (role_name, description) VALUES
('CUSTOMER', 'Regular cruise booking customer'),
('ADMIN', 'System administrator'),
('STAFF', 'Cruise company staff member'),
('MANAGER', 'Cruise operation manager');
```

### Create USER_ROLES Table
```sql
CREATE TABLE user_roles (
    user_role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_role (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
);
```

### Create PASSWORD_RESET_TOKENS Table
```sql
CREATE TABLE password_reset_tokens (
    token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_expires (expires_at, is_used)
);
```

### Create USER_SESSIONS Table
```sql
CREATE TABLE user_sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_token VARCHAR(255) NOT NULL UNIQUE,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_session_token (session_token),
    INDEX idx_user_active (user_id, is_active)
);
```

---

## Security Considerations

1. **Password Storage:**
   - Never store plain-text passwords
   - Use bcrypt, Argon2, or PBKDF2 for hashing
   - Set minimum password complexity requirements

2. **Session Management:**
   - Use secure, random session tokens (JWT or UUID)
   - Set appropriate expiration times
   - Implement token refresh mechanism
   - Store tokens securely (httpOnly cookies)

3. **Email Verification:**
   - Send verification link after registration
   - Set `email_verified=false` initially
   - Require verification before certain actions

4. **Account Security:**
   - Implement rate limiting for login attempts
   - Lock accounts after multiple failed attempts
   - Log all authentication events
   - Use HTTPS for all authentication endpoints

5. **Data Protection:**
   - Encrypt sensitive data at rest
   - Use parameterized queries to prevent SQL injection
   - Implement proper authorization checks
   - Regular security audits

---

## Sample Queries

### Register New User
```sql
-- 1. Insert user
INSERT INTO users (email, username, password_hash, first_name, last_name, is_active, email_verified)
VALUES ('user@example.com', 'johndoe', '$2a$10$...', 'John', 'Doe', TRUE, FALSE);

-- 2. Assign default CUSTOMER role
INSERT INTO user_roles (user_id, role_id)
VALUES (LAST_INSERT_ID(), (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'));
```

### Authenticate User (Sign-In)
```sql
-- 1. Get user by email
SELECT u.user_id, u.email, u.username, u.password_hash, u.first_name, u.last_name, 
       u.is_active, u.email_verified, r.role_name
FROM users u
LEFT JOIN user_roles ur ON u.user_id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.role_id
WHERE u.email = 'user@example.com'
AND u.is_active = TRUE;

-- 2. If password verified, update last login
UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?;

-- 3. Create session
INSERT INTO user_sessions (user_id, session_token, ip_address, user_agent, expires_at)
VALUES (?, 'jwt-token-here', '192.168.1.1', 'Mozilla/5.0...', DATE_ADD(NOW(), INTERVAL 24 HOUR));
```

### Get User Profile
```sql
SELECT u.user_id, u.email, u.username, u.first_name, u.last_name, 
       u.phone_number, u.address, u.city, u.state, u.country,
       GROUP_CONCAT(r.role_name) as roles
FROM users u
LEFT JOIN user_roles ur ON u.user_id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.role_id
WHERE u.user_id = ?
GROUP BY u.user_id;
```

---

## Additional Features to Consider

1. **Email Verification System**
   - Add `email_verification_tokens` table
   - Implement verification workflow

2. **Two-Factor Authentication (2FA)**
   - Add `user_2fa_settings` table
   - Store TOTP secrets, backup codes

3. **Social Login Integration**
   - Add `user_social_accounts` table
   - Support Google, Facebook OAuth

4. **Login History/Audit Trail**
   - Add `login_attempts` table
   - Track successful and failed attempts

5. **Account Preferences**
   - Add `user_preferences` table
   - Store notification settings, language, etc.

---

---

# Cruise Reservation Module

## Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ RESERVATIONS : makes
    SHIPS ||--o{ CRUISES : operates
    SHIPS ||--o{ SHIP_AMENITIES : has
    AMENITIES ||--o{ SHIP_AMENITIES : offered_on
    PORTS ||--o{ CRUISES : departs_from
    PORTS ||--o{ CRUISES : arrives_at
    CRUISES ||--o{ CRUISE_CABINS : has
    CABIN_TYPES ||--o{ CRUISE_CABINS : defines
    CRUISES ||--o{ RESERVATIONS : booked_for
    RESERVATIONS ||--o{ PASSENGERS : includes
    CRUISES ||--o{ ITINERARY_STOPS : includes

    SHIPS {
        bigint ship_id PK
        varchar ship_name UK
        varchar ship_image_url
        varchar ship_logo_url
        varchar description
        int total_capacity
        int crew_size
        decimal tonnage
        int year_built
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    AMENITIES {
        int amenity_id PK
        varchar amenity_name UK
        varchar category
        varchar description
        varchar icon_url
    }

    SHIP_AMENITIES {
        bigint ship_amenity_id PK
        bigint ship_id FK
        int amenity_id FK
        varchar details
        timestamp added_at
    }

    PORTS {
        int port_id PK
        varchar port_name UK
        varchar city
        varchar country
        varchar port_code UK
        decimal latitude
        decimal longitude
        varchar region
        text description
        varchar image_url
        boolean is_active
    }

    CABIN_TYPES {
        int cabin_type_id PK
        varchar type_name UK
        varchar description
        int max_occupancy
        decimal base_price_multiplier
        int display_order
    }

    CRUISES {
        bigint cruise_id PK
        bigint ship_id FK
        int departure_port_id FK
        int arrival_port_id FK
        date departure_date
        date return_date
        int duration_days
        decimal base_price
        int total_cabins
        int available_cabins
        varchar status
        varchar highlights
        timestamp created_at
        timestamp updated_at
    }

    CRUISE_CABINS {
        bigint cruise_cabin_id PK
        bigint cruise_id FK
        int cabin_type_id FK
        varchar cabin_number
        decimal price_per_person
        int max_occupancy
        boolean is_available
        varchar deck_level
    }

    ITINERARY_STOPS {
        bigint stop_id PK
        bigint cruise_id FK
        int port_id FK
        int stop_order
        date arrival_date
        time arrival_time
        date departure_date
        time departure_time
        varchar description
    }

    RESERVATIONS {
        bigint reservation_id PK
        bigint user_id FK
        bigint cruise_id FK
        bigint cruise_cabin_id FK
        varchar reservation_number UK
        int total_passengers
        int adult_count
        int child_count
        decimal total_price
        varchar status
        varchar payment_status
        timestamp reservation_date
        timestamp confirmed_at
        timestamp cancelled_at
        timestamp created_at
        timestamp updated_at
    }

    PASSENGERS {
        bigint passenger_id PK
        bigint reservation_id FK
        varchar first_name
        varchar last_name
        date date_of_birth
        varchar gender
        varchar passenger_type
        varchar nationality
        varchar passport_number
        date passport_expiry
        varchar special_requirements
    }
```

---

## Table Definitions

### 1. SHIPS Table
**Purpose:** Stores cruise ship information

| Column Name      | Data Type      | Constraints                    | Description                          |
|------------------|----------------|--------------------------------|--------------------------------------|
| ship_id          | BIGINT         | PRIMARY KEY, AUTO_INCREMENT    | Unique ship identifier               |
| ship_name        | VARCHAR(100)   | UNIQUE, NOT NULL               | Name of the ship                     |
| ship_image_url   | VARCHAR(500)   | NULL                           | URL to ship image                    |
| ship_logo_url    | VARCHAR(500)   | NULL                           | URL to ship logo                     |
| description      | TEXT           | NULL                           | Ship description and features        |
| total_capacity   | INT            | NOT NULL                       | Maximum passenger capacity           |
| crew_size        | INT            | NULL                           | Number of crew members               |
| tonnage          | DECIMAL(10,2)  | NULL                           | Ship tonnage                         |
| year_built       | INT            | NULL                           | Year the ship was built              |
| is_active        | BOOLEAN        | DEFAULT TRUE                   | Ship active status                   |
| created_at       | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP      | Record creation timestamp            |
| updated_at       | TIMESTAMP      | ON UPDATE CURRENT_TIMESTAMP    | Last update timestamp                |

**Indexes:**
- PRIMARY KEY: `ship_id`
- UNIQUE INDEX: `ship_name`
- INDEX: `is_active`

---

### 2. AMENITIES Table
**Purpose:** Master list of onboard amenities

| Column Name   | Data Type    | Constraints                 | Description                    |
|---------------|--------------|----------------------------|--------------------------------|
| amenity_id    | INT          | PRIMARY KEY, AUTO_INCREMENT| Unique amenity identifier      |
| amenity_name  | VARCHAR(100) | UNIQUE, NOT NULL           | Amenity name                   |
| category      | VARCHAR(50)  | NULL                       | Category (Dining, Entertainment, etc.) |
| description   | VARCHAR(255) | NULL                       | Amenity description            |
| icon_url      | VARCHAR(500) | NULL                       | Icon/image URL                 |

**Common Amenities:**
- Swimming Pool, Spa, Fitness Center, Casino, Theater, Restaurants, Kids Club, etc.

**Indexes:**
- PRIMARY KEY: `amenity_id`
- UNIQUE INDEX: `amenity_name`
- INDEX: `category`

---

### 3. SHIP_AMENITIES Table
**Purpose:** Links ships with their amenities

| Column Name      | Data Type    | Constraints                       | Description                      |
|------------------|--------------|-----------------------------------|----------------------------------|
| ship_amenity_id  | BIGINT       | PRIMARY KEY, AUTO_INCREMENT       | Unique identifier                |
| ship_id          | BIGINT       | FOREIGN KEY (SHIPS.ship_id)       | Reference to ship                |
| amenity_id       | INT          | FOREIGN KEY (AMENITIES.amenity_id)| Reference to amenity             |
| details          | VARCHAR(255) | NULL                              | Specific details for this ship   |
| added_at         | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP         | When amenity was added           |

**Indexes:**
- PRIMARY KEY: `ship_amenity_id`
- UNIQUE INDEX: `ship_id`, `amenity_id` (composite)
- FOREIGN KEY: `ship_id` REFERENCES `SHIPS(ship_id)` ON DELETE CASCADE
- FOREIGN KEY: `amenity_id` REFERENCES `AMENITIES(amenity_id)` ON DELETE CASCADE

---

### 4. PORTS Table
**Purpose:** Stores port information for both departures and arrivals

| Column Name | Data Type     | Constraints                 | Description                    |
|-------------|---------------|----------------------------|--------------------------------|
| port_id     | INT           | PRIMARY KEY, AUTO_INCREMENT| Unique port identifier         |
| port_name   | VARCHAR(100)  | UNIQUE, NOT NULL           | Port name                      |
| city        | VARCHAR(100)  | NOT NULL                   | City name                      |
| country     | VARCHAR(100)  | NOT NULL                   | Country name                   |
| port_code   | VARCHAR(10)   | UNIQUE, NOT NULL           | Port code (e.g., MIA, NYC)     |
| latitude    | DECIMAL(10,8) | NULL                       | Geographic latitude            |
| longitude   | DECIMAL(11,8) | NULL                       | Geographic longitude           |
| region      | VARCHAR(100)  | NULL                       | Region (Caribbean, Mediterranean, etc.) |
| description | TEXT          | NULL                       | Port/destination description   |
| image_url   | VARCHAR(500)  | NULL                       | Port/destination image URL     |
| is_active   | BOOLEAN       | DEFAULT TRUE               | Port active status             |

**Indexes:**
- PRIMARY KEY: `port_id`
- UNIQUE INDEX: `port_name`
- UNIQUE INDEX: `port_code`
- INDEX: `country`, `is_active`
- INDEX: `region`

---

### 5. CABIN_TYPES Table
**Purpose:** Defines different cabin types and their characteristics

| Column Name           | Data Type     | Constraints                 | Description                    |
|-----------------------|---------------|----------------------------|--------------------------------|
| cabin_type_id         | INT           | PRIMARY KEY, AUTO_INCREMENT| Unique cabin type identifier   |
| type_name             | VARCHAR(50)   | UNIQUE, NOT NULL           | Cabin type name                |
| description           | TEXT          | NULL                       | Cabin type description         |
| max_occupancy         | INT           | NOT NULL                   | Maximum number of guests       |
| base_price_multiplier | DECIMAL(5,2)  | DEFAULT 1.00               | Price multiplier vs base       |
| display_order         | INT           | DEFAULT 0                  | Display order in UI            |

**Default Cabin Types:**
- `Interior` - Inside cabins without windows
- `Ocean View` - Cabins with windows or portholes
- `Balcony` - Cabins with private balconies
- `Suite` - Luxury suites with enhanced amenities

**Indexes:**
- PRIMARY KEY: `cabin_type_id`
- UNIQUE INDEX: `type_name`
- INDEX: `display_order`

---

### 6. CRUISES Table
**Purpose:** Stores cruise itineraries and schedules

| Column Name        | Data Type     | Constraints                       | Description                      |
|--------------------|---------------|-----------------------------------|----------------------------------|
| cruise_id          | BIGINT        | PRIMARY KEY, AUTO_INCREMENT       | Unique cruise identifier         |
| ship_id            | BIGINT        | FOREIGN KEY (SHIPS.ship_id)       | Ship operating this cruise       |
| departure_port_id  | INT           | FOREIGN KEY (PORTS.port_id)       | Departure port                   |
| arrival_port_id    | INT           | FOREIGN KEY (PORTS.port_id)       | Final arrival/destination port   |
| departure_date     | DATE          | NOT NULL                          | Cruise departure date            |
| return_date        | DATE          | NOT NULL                          | Cruise return date               |
| duration_days      | INT           | NOT NULL                          | Cruise duration in days          |
| base_price         | DECIMAL(10,2) | NOT NULL                          | Base price per person            |
| total_cabins       | INT           | NOT NULL                          | Total cabins available           |
| available_cabins   | INT           | NOT NULL                          | Currently available cabins       |
| status             | VARCHAR(20)   | DEFAULT 'SCHEDULED'               | SCHEDULED, BOARDING, SAILING, COMPLETED, CANCELLED |
| highlights         | TEXT          | NULL                              | Itinerary highlights             |
| created_at         | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP         | Record creation timestamp        |
| updated_at         | TIMESTAMP     | ON UPDATE CURRENT_TIMESTAMP       | Last update timestamp            |

**Indexes:**
- PRIMARY KEY: `cruise_id`
- FOREIGN KEY: `ship_id` REFERENCES `SHIPS(ship_id)`
- FOREIGN KEY: `departure_port_id` REFERENCES `PORTS(port_id)`
- FOREIGN KEY: `arrival_port_id` REFERENCES `PORTS(port_id)`
- INDEX: `departure_date`, `return_date`
- INDEX: `status`, `available_cabins`

---

### 7. CRUISE_CABINS Table
**Purpose:** Stores specific cabin information for each cruise

| Column Name      | Data Type     | Constraints                         | Description                      |
|------------------|---------------|-------------------------------------|----------------------------------|
| cruise_cabin_id  | BIGINT        | PRIMARY KEY, AUTO_INCREMENT         | Unique cabin identifier          |
| cruise_id        | BIGINT        | FOREIGN KEY (CRUISES.cruise_id)     | Reference to cruise              |
| cabin_type_id    | INT           | FOREIGN KEY (CABIN_TYPES.c_type_id) | Cabin type                       |
| cabin_number     | VARCHAR(20)   | NOT NULL                            | Cabin number (e.g., A101)        |
| price_per_person | DECIMAL(10,2) | NOT NULL                            | Price per person for this cabin  |
| max_occupancy    | INT           | NOT NULL                            | Maximum guests for this cabin    |
| is_available     | BOOLEAN       | DEFAULT TRUE                        | Availability status              |
| deck_level       | VARCHAR(20)   | NULL                                | Deck level (e.g., Deck 7)        |

**Indexes:**
- PRIMARY KEY: `cruise_cabin_id`
- UNIQUE INDEX: `cruise_id`, `cabin_number` (composite)
- FOREIGN KEY: `cruise_id` REFERENCES `CRUISES(cruise_id)` ON DELETE CASCADE
- FOREIGN KEY: `cabin_type_id` REFERENCES `CABIN_TYPES(cabin_type_id)`
- INDEX: `is_available`, `cabin_type_id`

---

### 8. ITINERARY_STOPS Table
**Purpose:** Stores port stops during the cruise

| Column Name    | Data Type    | Constraints                     | Description                      |
|----------------|--------------|--------------------------------|----------------------------------|
| stop_id        | BIGINT       | PRIMARY KEY, AUTO_INCREMENT     | Unique stop identifier           |
| cruise_id      | BIGINT       | FOREIGN KEY (CRUISES.cruise_id) | Reference to cruise              |
| port_id        | INT          | FOREIGN KEY (PORTS.port_id)     | Port of call                     |
| stop_order     | INT          | NOT NULL                        | Order in itinerary (1, 2, 3...)  |
| arrival_date   | DATE         | NOT NULL                        | Arrival date                     |
| arrival_time   | TIME         | NULL                            | Arrival time                     |
| departure_date | DATE         | NOT NULL                        | Departure date                   |
| departure_time | TIME         | NULL                            | Departure time                   |
| description    | TEXT         | NULL                            | Stop description/activities      |

**Indexes:**
- PRIMARY KEY: `stop_id`
- FOREIGN KEY: `cruise_id` REFERENCES `CRUISES(cruise_id)` ON DELETE CASCADE
- FOREIGN KEY: `port_id` REFERENCES `PORTS(port_id)`
- INDEX: `cruise_id`, `stop_order` (composite)

---

### 9. RESERVATIONS Table
**Purpose:** Stores customer cruise reservations/bookings

| Column Name        | Data Type     | Constraints                         | Description                      |
|--------------------|---------------|-------------------------------------|----------------------------------|
| reservation_id     | BIGINT        | PRIMARY KEY, AUTO_INCREMENT         | Unique reservation identifier    |
| user_id            | BIGINT        | FOREIGN KEY (USERS.user_id)         | Customer who made reservation    |
| cruise_id          | BIGINT        | FOREIGN KEY (CRUISES.cruise_id)     | Booked cruise                    |
| cruise_cabin_id    | BIGINT        | FOREIGN KEY (CRUISE_CABINS.cc_id)   | Assigned cabin                   |
| reservation_number | VARCHAR(20)   | UNIQUE, NOT NULL                    | Unique booking reference number  |
| total_passengers   | INT           | NOT NULL                            | Total number of passengers       |
| adult_count        | INT           | NOT NULL                            | Number of adults                 |
| child_count        | INT           | DEFAULT 0                           | Number of children               |
| total_price        | DECIMAL(10,2) | NOT NULL                            | Total booking price              |
| status             | VARCHAR(20)   | DEFAULT 'PENDING'                   | PENDING, CONFIRMED, CANCELLED, COMPLETED |
| payment_status     | VARCHAR(20)   | DEFAULT 'UNPAID'                    | UNPAID, PARTIAL, PAID, REFUNDED  |
| reservation_date   | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP           | When reservation was made        |
| confirmed_at       | TIMESTAMP     | NULL                                | Confirmation timestamp           |
| cancelled_at       | TIMESTAMP     | NULL                                | Cancellation timestamp           |
| created_at         | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP           | Record creation timestamp        |
| updated_at         | TIMESTAMP     | ON UPDATE CURRENT_TIMESTAMP         | Last update timestamp            |

**Indexes:**
- PRIMARY KEY: `reservation_id`
- UNIQUE INDEX: `reservation_number`
- FOREIGN KEY: `user_id` REFERENCES `USERS(user_id)`
- FOREIGN KEY: `cruise_id` REFERENCES `CRUISES(cruise_id)`
- FOREIGN KEY: `cruise_cabin_id` REFERENCES `CRUISE_CABINS(cruise_cabin_id)`
- INDEX: `user_id`, `status`
- INDEX: `reservation_date`

---

### 10. PASSENGERS Table
**Purpose:** Stores individual passenger information for each reservation

| Column Name         | Data Type    | Constraints                           | Description                      |
|---------------------|--------------|---------------------------------------|----------------------------------|
| passenger_id        | BIGINT       | PRIMARY KEY, AUTO_INCREMENT           | Unique passenger identifier      |
| reservation_id      | BIGINT       | FOREIGN KEY (RESERVATIONS.res_id)     | Reference to reservation         |
| first_name          | VARCHAR(100) | NOT NULL                              | Passenger first name             |
| last_name           | VARCHAR(100) | NOT NULL                              | Passenger last name              |
| date_of_birth       | DATE         | NOT NULL                              | Passenger date of birth          |
| gender              | VARCHAR(10)  | NULL                                  | Gender                           |
| passenger_type      | VARCHAR(20)  | NOT NULL                              | ADULT or CHILD                   |
| nationality         | VARCHAR(100) | NULL                                  | Passenger nationality            |
| passport_number     | VARCHAR(50)  | NULL                                  | Passport number                  |
| passport_expiry     | DATE         | NULL                                  | Passport expiry date             |
| special_requirements| TEXT         | NULL                                  | Dietary/accessibility needs      |

**Indexes:**
- PRIMARY KEY: `passenger_id`
- FOREIGN KEY: `reservation_id` REFERENCES `RESERVATIONS(reservation_id)` ON DELETE CASCADE
- INDEX: `reservation_id`

---

## Cruise Reservation Workflow

### Cruise Search & Selection Flow

```mermaid
sequenceDiagram
    participant Customer
    participant Frontend
    participant Backend
    participant Database

    Customer->>Frontend: Access Cruise Reservation Page
    Frontend->>Backend: GET /api/cruises/search
    Note over Backend: Query parameters: <br/>departure_date, return_date,<br/>departure_port, arrival_port
    Backend->>Database: SELECT FROM CRUISES<br/>JOIN SHIPS, PORTS
    Database-->>Backend: Available cruises with details
    Backend->>Database: SELECT ship amenities
    Database-->>Backend: Amenities list
    Backend-->>Frontend: Cruise list with full details
    Frontend-->>Customer: Display cruise options
    
    Customer->>Frontend: Select a cruise
    Frontend->>Backend: GET /api/cruises/{cruise_id}
    Backend->>Database: Get cruise details, cabins, itinerary
    Database-->>Backend: Complete cruise information
    Backend-->>Frontend: Cruise details with pricing
    Frontend-->>Customer: Show cruise details page
```

### Cabin Selection & Booking Flow

```mermaid
sequenceDiagram
    participant Customer
    participant Frontend
    participant Backend
    participant Database

    Customer->>Frontend: Select cabin type & enter passenger count
    Frontend->>Backend: GET /api/cruises/{id}/cabins?type=BALCONY
    Backend->>Database: SELECT available cabins
    Database-->>Backend: Available cabins with prices
    Backend-->>Frontend: Cabin options with pricing
    Frontend-->>Customer: Display available cabins
    
    Customer->>Frontend: Enter passenger details
    Frontend->>Frontend: Validate: adults + children ≤ max_occupancy
    Customer->>Frontend: Confirm reservation
    Frontend->>Backend: POST /api/reservations
    
    Backend->>Backend: Validate passenger count
    Backend->>Backend: Calculate total price
    Backend->>Database: BEGIN TRANSACTION
    Backend->>Database: INSERT INTO RESERVATIONS
    Database-->>Backend: reservation_id
    Backend->>Database: INSERT INTO PASSENGERS (for each)
    Backend->>Database: UPDATE CRUISE_CABINS<br/>SET is_available = FALSE
    Backend->>Database: UPDATE CRUISES<br/>SET available_cabins = available_cabins - 1
    Backend->>Database: COMMIT TRANSACTION
    
    Backend-->>Frontend: Reservation confirmation
    Frontend-->>Customer: Display booking confirmation<br/>with reservation number
```

---

## SQL Schema Creation Scripts

### Create SHIPS Table
```sql
CREATE TABLE ships (
    ship_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ship_name VARCHAR(100) NOT NULL UNIQUE,
    ship_image_url VARCHAR(500),
    ship_logo_url VARCHAR(500),
    description TEXT,
    total_capacity INT NOT NULL,
    crew_size INT,
    tonnage DECIMAL(10,2),
    year_built INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_ship_name (ship_name),
    INDEX idx_active (is_active)
);
```

### Create AMENITIES Table
```sql
CREATE TABLE amenities (
    amenity_id INT AUTO_INCREMENT PRIMARY KEY,
    amenity_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50),
    description VARCHAR(255),
    icon_url VARCHAR(500),
    INDEX idx_amenity_name (amenity_name),
    INDEX idx_category (category)
);

-- Insert common amenities
INSERT INTO amenities (amenity_name, category, description) VALUES
('Swimming Pool', 'Recreation', 'Main swimming pool'),
('Spa & Wellness Center', 'Wellness', 'Full-service spa'),
('Fitness Center', 'Wellness', '24-hour fitness facility'),
('Casino', 'Entertainment', 'Full-service casino'),
('Theater', 'Entertainment', 'Live entertainment venue'),
('Main Dining Room', 'Dining', 'Formal dining restaurant'),
('Buffet Restaurant', 'Dining', 'Casual buffet dining'),
('Specialty Restaurants', 'Dining', 'Premium dining options'),
('Kids Club', 'Family', 'Supervised children activities'),
('Water Slides', 'Recreation', 'Water park attractions'),
('Rock Climbing Wall', 'Recreation', 'Climbing facility'),
('Mini Golf', 'Recreation', 'Miniature golf course'),
('Library', 'Leisure', 'Reading room and library'),
('Art Gallery', 'Culture', 'Onboard art exhibitions'),
('Shopping', 'Retail', 'Duty-free shops');
```

### Create SHIP_AMENITIES Table
```sql
CREATE TABLE ship_amenities (
    ship_amenity_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ship_id BIGINT NOT NULL,
    amenity_id INT NOT NULL,
    details VARCHAR(255),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_ship_amenity (ship_id, amenity_id),
    FOREIGN KEY (ship_id) REFERENCES ships(ship_id) ON DELETE CASCADE,
    FOREIGN KEY (amenity_id) REFERENCES amenities(amenity_id) ON DELETE CASCADE,
    INDEX idx_ship_id (ship_id),
    INDEX idx_amenity_id (amenity_id)
);
```

### Create PORTS Table
```sql
CREATE TABLE ports (
    port_id INT AUTO_INCREMENT PRIMARY KEY,
    port_name VARCHAR(100) NOT NULL UNIQUE,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    port_code VARCHAR(10) NOT NULL UNIQUE,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    region VARCHAR(100),
    description TEXT,
    image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_port_name (port_name),
    INDEX idx_port_code (port_code),
    INDEX idx_country_active (country, is_active),
    INDEX idx_region (region)
);

-- Insert sample ports with destination information
INSERT INTO ports (port_name, city, country, port_code, latitude, longitude, region, description, image_url) VALUES
('Port of Miami', 'Miami', 'USA', 'MIA', 25.7743, -80.1937, 'Caribbean', 'Gateway to Caribbean cruises with tropical beaches', NULL),
('Port Canaveral', 'Cape Canaveral', 'USA', 'PCV', 28.4091, -80.6098, 'Caribbean', 'Close to Orlando theme parks and Caribbean destinations', NULL),
('Port Everglades', 'Fort Lauderdale', 'USA', 'FLL', 26.0922, -80.1166, 'Caribbean', 'Major cruise port serving Caribbean routes', NULL),
('Port of Barcelona', 'Barcelona', 'Spain', 'BCN', 41.3493, 2.1675, 'Mediterranean', 'Historic Mediterranean port with stunning architecture', NULL),
('Port of Southampton', 'Southampton', 'UK', 'SOU', 50.8998, -1.4044, 'Northern Europe', 'Gateway to Norwegian Fjords and Northern Europe', NULL),
('Port of Singapore', 'Singapore', 'Singapore', 'SIN', 1.2644, 103.8367, 'Southeast Asia', 'Modern Asian hub for Southeast Asia cruises', NULL),
('Nassau', 'Nassau', 'Bahamas', 'NAS', 25.0443, -77.3504, 'Caribbean', 'Beautiful Bahamian islands paradise', NULL),
('Cozumel', 'Cozumel', 'Mexico', 'CZM', 20.5083, -86.9458, 'Caribbean', 'Mexican Caribbean island with coral reefs', NULL),
('Juneau', 'Juneau', 'USA', 'JNU', 58.3019, -134.4197, 'Alaska', 'Alaska capital with breathtaking glaciers', NULL),
('Venice', 'Venice', 'Italy', 'VCE', 45.4408, 12.3155, 'Mediterranean', 'Historic Italian city on canals', NULL);
```

### Create CABIN_TYPES Table
```sql
CREATE TABLE cabin_types (
    cabin_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    max_occupancy INT NOT NULL,
    base_price_multiplier DECIMAL(5,2) DEFAULT 1.00,
    display_order INT DEFAULT 0,
    INDEX idx_type_name (type_name),
    INDEX idx_display_order (display_order)
);

-- Insert default cabin types
INSERT INTO cabin_types (type_name, description, max_occupancy, base_price_multiplier, display_order) VALUES
('Interior', 'Cozy inside cabin, perfect for budget-conscious travelers', 4, 1.00, 1),
('Ocean View', 'Cabin with window or porthole for natural light and ocean views', 4, 1.25, 2),
('Balcony', 'Private balcony with outdoor seating and ocean views', 4, 1.75, 3),
('Suite', 'Spacious luxury suite with premium amenities and services', 6, 2.50, 4);
```

### Create CRUISES Table
```sql
CREATE TABLE cruises (
    cruise_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ship_id BIGINT NOT NULL,
    departure_port_id INT NOT NULL,
    arrival_port_id INT NOT NULL,
    departure_date DATE NOT NULL,
    return_date DATE NOT NULL,
    duration_days INT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    total_cabins INT NOT NULL,
    available_cabins INT NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    highlights TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ship_id) REFERENCES ships(ship_id),
    FOREIGN KEY (departure_port_id) REFERENCES ports(port_id),
    FOREIGN KEY (arrival_port_id) REFERENCES ports(port_id),
    INDEX idx_departure_date (departure_date),
    INDEX idx_return_date (return_date),
    INDEX idx_status_availability (status, available_cabins),
    CHECK (return_date > departure_date),
    CHECK (available_cabins <= total_cabins),
    CHECK (status IN ('SCHEDULED', 'BOARDING', 'SAILING', 'COMPLETED', 'CANCELLED'))
);
```

### Create CRUISE_CABINS Table
```sql
CREATE TABLE cruise_cabins (
    cruise_cabin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cruise_id BIGINT NOT NULL,
    cabin_type_id INT NOT NULL,
    cabin_number VARCHAR(20) NOT NULL,
    price_per_person DECIMAL(10,2) NOT NULL,
    max_occupancy INT NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    deck_level VARCHAR(20),
    UNIQUE KEY unique_cruise_cabin (cruise_id, cabin_number),
    FOREIGN KEY (cruise_id) REFERENCES cruises(cruise_id) ON DELETE CASCADE,
    FOREIGN KEY (cabin_type_id) REFERENCES cabin_types(cabin_type_id),
    INDEX idx_cruise_cabin_type (cruise_id, cabin_type_id),
    INDEX idx_availability (is_available)
);
```

### Create ITINERARY_STOPS Table
```sql
CREATE TABLE itinerary_stops (
    stop_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cruise_id BIGINT NOT NULL,
    port_id INT NOT NULL,
    stop_order INT NOT NULL,
    arrival_date DATE NOT NULL,
    arrival_time TIME,
    departure_date DATE NOT NULL,
    departure_time TIME,
    description TEXT,
    FOREIGN KEY (cruise_id) REFERENCES cruises(cruise_id) ON DELETE CASCADE,
    FOREIGN KEY (port_id) REFERENCES ports(port_id),
    INDEX idx_cruise_order (cruise_id, stop_order),
    UNIQUE KEY unique_cruise_stop_order (cruise_id, stop_order)
);
```

### Create RESERVATIONS Table
```sql
CREATE TABLE reservations (
    reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    cruise_id BIGINT NOT NULL,
    cruise_cabin_id BIGINT NOT NULL,
    reservation_number VARCHAR(20) NOT NULL UNIQUE,
    total_passengers INT NOT NULL,
    adult_count INT NOT NULL,
    child_count INT DEFAULT 0,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_status VARCHAR(20) DEFAULT 'UNPAID',
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (cruise_id) REFERENCES cruises(cruise_id),
    FOREIGN KEY (cruise_cabin_id) REFERENCES cruise_cabins(cruise_cabin_id),
    INDEX idx_reservation_number (reservation_number),
    INDEX idx_user_status (user_id, status),
    INDEX idx_reservation_date (reservation_date),
    CHECK (total_passengers = adult_count + child_count),
    CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),
    CHECK (payment_status IN ('UNPAID', 'PARTIAL', 'PAID', 'REFUNDED'))
);
```

### Create PASSENGERS Table
```sql
CREATE TABLE passengers (
    passenger_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10),
    passenger_type VARCHAR(20) NOT NULL,
    nationality VARCHAR(100),
    passport_number VARCHAR(50),
    passport_expiry DATE,
    special_requirements TEXT,
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE,
    INDEX idx_reservation_id (reservation_id),
    CHECK (passenger_type IN ('ADULT', 'CHILD')),
    CHECK (gender IN ('MALE', 'FEMALE', 'OTHER', NULL))
);
```

---

## Business Logic & Constraints

### Reservation Business Rules

1. **Passenger Count Validation:**
   - Total passengers (adults + children) must not exceed cabin max occupancy
   - At least one adult required per reservation
   - Children count includes passengers under 18 years

2. **Pricing Calculation:**
   ```
   total_price = (cruise.base_price × cabin_type.base_price_multiplier × total_passengers)
   ```

3. **Availability Management:**
   - Cruise cabin `is_available` must be TRUE before booking
   - Update cruise `available_cabins` count after each reservation
   - Prevent double-booking with database transactions

4. **Reservation Number Generation:**
   - Format: `CR-{YEAR}{MONTH}{DAY}-{RANDOM5}` (e.g., CR-20260315-A7B9C)
   - Must be unique across all reservations

5. **Status Transitions:**
   - PENDING → CONFIRMED (after payment)
   - PENDING/CONFIRMED → CANCELLED (customer request)
   - CONFIRMED → COMPLETED (after cruise ends)

---

## Sample Queries

### Search Available Cruises
```sql
SELECT 
    c.cruise_id,
    c.departure_date,
    c.return_date,
    c.duration_days,
    c.base_price,
    c.available_cabins,
    s.ship_name,
    s.ship_image_url,
    s.ship_logo_url,
    p_dep.port_name AS departure_port,
    p_dep.city AS departure_city,
    p_arr.port_name AS arrival_port,
    p_arr.city AS arrival_city,
    p_arr.region AS destination_region,
    c.highlights
FROM cruises c
JOIN ships s ON c.ship_id = s.ship_id
JOIN ports p_dep ON c.departure_port_id = p_dep.port_id
JOIN ports p_arr ON c.arrival_port_id = p_arr.port_id
WHERE c.departure_date >= CURDATE()
  AND c.status = 'SCHEDULED'
  AND c.available_cabins > 0
  AND c.departure_date BETWEEN '2026-06-01' AND '2026-08-31'
ORDER BY c.departure_date;
```

### Get Cruise Details with Amenities
```sql
-- Get cruise with ship amenities
SELECT 
    c.*,
    s.ship_name,
    s.ship_image_url,
    s.description AS ship_description,
    GROUP_CONCAT(DISTINCT a.amenity_name ORDER BY a.amenity_name) AS amenities
FROM cruises c
JOIN ships s ON c.ship_id = s.ship_id
LEFT JOIN ship_amenities sa ON s.ship_id = sa.ship_id
LEFT JOIN amenities a ON sa.amenity_id = a.amenity_id
WHERE c.cruise_id = ?
GROUP BY c.cruise_id;
```

### Get Available Cabins for a Cruise
```sql
SELECT 
    cc.cruise_cabin_id,
    cc.cabin_number,
    cc.price_per_person,
    cc.max_occupancy,
    cc.deck_level,
    ct.type_name,
    ct.description AS cabin_description
FROM cruise_cabins cc
JOIN cabin_types ct ON cc.cabin_type_id = ct.cabin_type_id
WHERE cc.cruise_id = ?
  AND cc.is_available = TRUE
  AND ct.type_name = 'Balcony'  -- Optional filter
ORDER BY ct.display_order, cc.cabin_number;
```

### Get Cruise Itinerary
```sql
SELECT 
    its.stop_order,
    its.arrival_date,
    its.arrival_time,
    its.departure_date,
    its.departure_time,
    its.description,
    p.port_name,
    p.city,
    p.country
FROM itinerary_stops its
JOIN ports p ON its.port_id = p.port_id
WHERE its.cruise_id = ?
ORDER BY its.stop_order;
```

### Create Reservation with Passengers
```sql
-- Step 1: Create reservation (generate reservation number first)
INSERT INTO reservations (
    user_id, cruise_id, cruise_cabin_id, reservation_number,
    total_passengers, adult_count, child_count, total_price,
    status, payment_status
) VALUES (
    ?, ?, ?, 'CR-20260315-A7B9C',
    3, 2, 1, 4500.00,
    'PENDING', 'UNPAID'
);

-- Step 2: Add passengers
INSERT INTO passengers (
    reservation_id, first_name, last_name, date_of_birth,
    gender, passenger_type, nationality, passport_number, passport_expiry
) VALUES
    (LAST_INSERT_ID(), 'John', 'Doe', '1985-03-15', 'MALE', 'ADULT', 'USA', 'P12345678', '2030-03-15'),
    (LAST_INSERT_ID(), 'Jane', 'Doe', '1987-07-22', 'FEMALE', 'ADULT', 'USA', 'P87654321', '2029-07-22'),
    (LAST_INSERT_ID(), 'Jimmy', 'Doe', '2015-11-10', 'MALE', 'CHILD', 'USA', 'P11223344', '2028-11-10');

-- Step 3: Update cabin availability
UPDATE cruise_cabins 
SET is_available = FALSE 
WHERE cruise_cabin_id = ?;

-- Step 4: Update cruise available cabins count
UPDATE cruises 
SET available_cabins = available_cabins - 1 
WHERE cruise_id = ?;
```

### Get User Reservations
```sql
SELECT 
    r.reservation_id,
    r.reservation_number,
    r.total_passengers,
    r.total_price,
    r.status,
    r.payment_status,
    r.reservation_date,
    c.departure_date,
    c.return_date,
    s.ship_name,
    p_dep.port_name AS departure_port,
    p_arr.port_name AS arrival_port,
    p_arr.region AS destination_region,
    ct.type_name AS cabin_type,
    cc.cabin_number
FROM reservations r
JOIN cruises c ON r.cruise_id = c.cruise_id
JOIN ships s ON c.ship_id = s.ship_id
JOIN ports p_dep ON c.departure_port_id = p_dep.port_id
JOIN ports p_arr ON c.arrival_port_id = p_arr.port_id
JOIN cruise_cabins cc ON r.cruise_cabin_id = cc.cruise_cabin_id
JOIN cabin_types ct ON cc.cabin_type_id = ct.cabin_type_id
WHERE r.user_id = ?
ORDER BY r.reservation_date DESC;
```

---

---

# Payment Integration Module

## Entity Relationship Diagram

```mermaid
erDiagram
    RESERVATIONS ||--o{ PAYMENT_TRANSACTIONS : paid_via

    RESERVATIONS {
        bigint reservation_id PK
        bigint user_id FK
        bigint cruise_id FK
        bigint cruise_cabin_id FK
        varchar reservation_number UK
        int total_passengers
        int adult_count
        int child_count
        decimal total_price
        varchar status
        varchar payment_status
        varchar cancellation_reason
        timestamp reservation_date
        timestamp confirmed_at
        timestamp cancelled_at
        timestamp created_at
        timestamp updated_at
    }

    PAYMENT_TRANSACTIONS {
        bigint payment_id PK
        bigint reservation_id FK
        varchar payment_method
        varchar gateway_transaction_id UK
        decimal amount
        varchar currency
        varchar status
        timestamp payment_date
        decimal refund_amount
        timestamp refund_date
        varchar notes
        timestamp created_at
        timestamp updated_at
    }
```

> **Note:** No payment card details (card number, CVV, expiry) are stored. Only the `gateway_transaction_id` from the payment provider (Stripe, PayPal, etc.) is persisted.

---

## Table Definitions

### 1. PAYMENT_TRANSACTIONS Table
**Purpose:** Records payment attempts and confirmations for each reservation without storing sensitive payment details

| Column Name           | Data Type     | Constraints                              | Description                                              |
|-----------------------|---------------|------------------------------------------|----------------------------------------------------------|
| payment_id            | BIGINT        | PRIMARY KEY, AUTO_INCREMENT              | Unique payment transaction identifier                    |
| reservation_id        | BIGINT        | FOREIGN KEY (RESERVATIONS.reservation_id)| Reference to the reservation being paid                  |
| payment_method        | VARCHAR(20)   | NOT NULL                                 | CREDIT_CARD, DEBIT_CARD, PAYPAL, APPLE_PAY, GOOGLE_PAY  |
| gateway_transaction_id| VARCHAR(255)  | UNIQUE, NOT NULL                         | External transaction ID from payment gateway             |
| amount                | DECIMAL(10,2) | NOT NULL                                 | Payment amount                                           |
| currency              | VARCHAR(10)   | DEFAULT 'USD'                            | ISO currency code                                        |
| status                | VARCHAR(20)   | DEFAULT 'PENDING'                        | PENDING, COMPLETED, FAILED, REFUNDED                     |
| payment_date          | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                | When payment was processed                               |
| refund_amount         | DECIMAL(10,2) | NULL                                     | Amount refunded (if applicable)                          |
| refund_date           | TIMESTAMP     | NULL                                     | When refund was processed                                |
| notes                 | VARCHAR(500)  | NULL                                     | Additional notes or failure reason                       |
| created_at            | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                | Record creation timestamp                                |
| updated_at            | TIMESTAMP     | ON UPDATE CURRENT_TIMESTAMP              | Last update timestamp                                    |

**Security Note:** Fields intentionally **not stored**: card number, CVV, expiry date, billing address. These are handled exclusively by the PCI-compliant payment gateway.

**Supported Payment Methods:**
- `CREDIT_CARD` — Visa, MasterCard, Amex (tokenised by gateway)
- `DEBIT_CARD` — Debit card via payment gateway
- `PAYPAL` — PayPal transaction
- `APPLE_PAY` — Apple Pay via Stripe / Braintree
- `GOOGLE_PAY` — Google Pay via Stripe / Braintree

**Indexes:**
- PRIMARY KEY: `payment_id`
- UNIQUE INDEX: `gateway_transaction_id`
- FOREIGN KEY: `reservation_id` REFERENCES `RESERVATIONS(reservation_id)`
- INDEX: `reservation_id`, `status`
- INDEX: `payment_date`

---

## RESERVATIONS Table — Updated Column

The `RESERVATIONS` table is extended with a `cancellation_reason` column to support the cancel reservation feature (Requirement 6):

| Column Name         | Data Type    | Constraints                | Description                                 |
|---------------------|--------------|----------------------------|---------------------------------------------|
| cancellation_reason | VARCHAR(500) | NULL                       | Reason provided by customer upon cancellation|

---

## Payment Workflow

### Checkout & Payment Flow

```mermaid
sequenceDiagram
    participant Customer
    participant Frontend
    participant Backend
    participant PaymentGateway
    participant Database

    Customer->>Frontend: Review booking summary
    Frontend->>Customer: Display payment options<br/>(Credit/Debit Card, PayPal,<br/>Apple Pay, Google Pay)

    Customer->>Frontend: Select payment method & submit
    Frontend->>PaymentGateway: Tokenise card details (client-side SDK)
    PaymentGateway-->>Frontend: Payment token (no card data on server)

    Frontend->>Backend: POST /api/payments/process<br/>{ reservation_id, payment_method, token, amount }
    Backend->>PaymentGateway: Charge request with token
    PaymentGateway-->>Backend: gateway_transaction_id + status

    alt Payment Successful
        Backend->>Database: INSERT INTO PAYMENT_TRANSACTIONS<br/>(status = 'COMPLETED')
        Backend->>Database: UPDATE RESERVATIONS<br/>SET status='CONFIRMED', payment_status='PAID',<br/>confirmed_at=NOW()
        Backend-->>Frontend: Success + confirmation data
        Frontend-->>Customer: Show confirmation dialog<br/>(reservation_number, ship_name, departure_date)
    else Payment Failed
        Backend->>Database: INSERT INTO PAYMENT_TRANSACTIONS<br/>(status = 'FAILED')
        Backend-->>Frontend: Failure message
        Frontend-->>Customer: Display error, retry options
    end
```

---

## SQL Schema Creation Scripts

### Create PAYMENT_TRANSACTIONS Table
```sql
CREATE TABLE payment_transactions (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    gateway_transaction_id VARCHAR(255) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USD',
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    refund_amount DECIMAL(10,2) NULL,
    refund_date TIMESTAMP NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
    INDEX idx_reservation_id (reservation_id),
    INDEX idx_gateway_txn (gateway_transaction_id),
    INDEX idx_status_date (status, payment_date),
    CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'APPLE_PAY', 'GOOGLE_PAY')),
    CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED'))
);
```

### Alter RESERVATIONS Table (add cancellation_reason)
```sql
ALTER TABLE reservations
    ADD COLUMN cancellation_reason VARCHAR(500) NULL
        AFTER cancelled_at;
```

---

## Sample Queries

### Process Payment
```sql
-- 1. Insert payment transaction after gateway confirmation
INSERT INTO payment_transactions (
    reservation_id, payment_method, gateway_transaction_id,
    amount, currency, status
) VALUES (
    ?, 'PAYPAL', 'PAY-9XY123ABC456789',
    4500.00, 'USD', 'COMPLETED'
);

-- 2. Confirm the reservation
UPDATE reservations
SET status = 'CONFIRMED',
    payment_status = 'PAID',
    confirmed_at = CURRENT_TIMESTAMP
WHERE reservation_id = ?;
```

### Get Payment Confirmation Details
```sql
SELECT
    r.reservation_number,
    s.ship_name,
    c.departure_date,
    c.return_date,
    p_arr.port_name AS arrival_port,
    p_arr.region AS destination_region,
    r.total_price,
    pt.payment_method,
    pt.gateway_transaction_id,
    pt.payment_date
FROM reservations r
JOIN cruises c ON r.cruise_id = c.cruise_id
JOIN ships s ON c.ship_id = s.ship_id
JOIN ports p_arr ON c.arrival_port_id = p_arr.port_id
JOIN payment_transactions pt ON pt.reservation_id = r.reservation_id
WHERE r.reservation_id = ?
  AND pt.status = 'COMPLETED';
```

---

---

# Modify & Cancel Reservation Module

## Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ RESERVATION_MODIFICATIONS : makes
    RESERVATIONS ||--o{ RESERVATION_MODIFICATIONS : tracked_by
    CABIN_TYPES ||--o{ RESERVATION_MODIFICATIONS : old_cabin_type
    CABIN_TYPES ||--o{ RESERVATION_MODIFICATIONS : new_cabin_type

    RESERVATION_MODIFICATIONS {
        bigint modification_id PK
        bigint reservation_id FK
        bigint modified_by FK
        varchar modification_type
        int old_cabin_type_id FK
        int new_cabin_type_id FK
        date old_departure_date
        date new_departure_date
        int old_passenger_count
        int new_passenger_count
        decimal price_difference
        text notes
        timestamp modified_at
    }
```

---

## Table Definitions

### 1. RESERVATION_MODIFICATIONS Table
**Purpose:** Audit log of all changes made to a reservation (dates, cabin type, passenger count)

| Column Name          | Data Type     | Constraints                                   | Description                                    |
|----------------------|---------------|-----------------------------------------------|------------------------------------------------|
| modification_id      | BIGINT        | PRIMARY KEY, AUTO_INCREMENT                   | Unique modification record identifier          |
| reservation_id       | BIGINT        | FOREIGN KEY (RESERVATIONS.reservation_id)     | Reservation that was modified                  |
| modified_by          | BIGINT        | FOREIGN KEY (USERS.user_id)                   | Customer who made the change                   |
| modification_type    | VARCHAR(50)   | NOT NULL                                      | DATES_CHANGE, CABIN_CHANGE, PASSENGER_COUNT_CHANGE, MULTIPLE |
| old_cabin_type_id    | INT           | FOREIGN KEY (CABIN_TYPES.cabin_type_id), NULL | Previous cabin type (NULL if not changed)      |
| new_cabin_type_id    | INT           | FOREIGN KEY (CABIN_TYPES.cabin_type_id), NULL | New cabin type (NULL if not changed)           |
| old_departure_date   | DATE          | NULL                                          | Previous departure date (NULL if not changed)  |
| new_departure_date   | DATE          | NULL                                          | New departure date (NULL if not changed)       |
| old_passenger_count  | INT           | NULL                                          | Previous total passengers                      |
| new_passenger_count  | INT           | NULL                                          | New total passengers                           |
| price_difference     | DECIMAL(10,2) | DEFAULT 0.00                                  | Price adjustment (+/-). Positive = extra charge|
| notes                | TEXT          | NULL                                          | Customer or system notes                       |
| modified_at          | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                     | When the modification was made                 |

**Indexes:**
- PRIMARY KEY: `modification_id`
- FOREIGN KEY: `reservation_id` REFERENCES `RESERVATIONS(reservation_id)` ON DELETE CASCADE
- FOREIGN KEY: `modified_by` REFERENCES `USERS(user_id)`
- FOREIGN KEY: `old_cabin_type_id` REFERENCES `CABIN_TYPES(cabin_type_id)`
- FOREIGN KEY: `new_cabin_type_id` REFERENCES `CABIN_TYPES(cabin_type_id)`
- INDEX: `reservation_id`, `modified_at`

---

## Modify Reservation Workflow

```mermaid
sequenceDiagram
    participant Customer
    participant Frontend
    participant Backend
    participant Database

    Customer->>Frontend: Access reservation & click Edit
    Frontend->>Backend: GET /api/reservations/{id}
    Backend->>Database: SELECT reservation + cruise + cabin details
    Database-->>Backend: Current reservation data
    Backend-->>Frontend: Pre-fill form with current values

    Customer->>Frontend: Update cabin type / dates / passengers
    Frontend->>Backend: PUT /api/reservations/{id}
    Backend->>Backend: Validate new passenger count ≤ cabin max_occupancy
    Backend->>Backend: Calculate price difference
    Backend->>Database: BEGIN TRANSACTION
    Backend->>Database: UPDATE RESERVATIONS (new cabin, passengers, price)
    Backend->>Database: INSERT INTO RESERVATION_MODIFICATIONS (audit log)
    Backend->>Database: COMMIT TRANSACTION
    Backend-->>Frontend: Updated confirmation details
    Frontend-->>Customer: Display updated booking summary
```

---

## Cancel Reservation Workflow

### Cancellation Policy
- A reservation can only be cancelled if the **departure date is more than 10 days away** from the current date.
- The backend enforces this rule regardless of frontend validation.

```mermaid
sequenceDiagram
    participant Customer
    participant Frontend
    participant Backend
    participant Database

    Customer->>Frontend: Click "Cancel Reservation"
    Frontend->>Backend: DELETE /api/reservations/{id}
    Backend->>Database: SELECT departure_date FROM cruises<br/>WHERE cruise_id = reservation.cruise_id
    Database-->>Backend: departure_date

    Backend->>Backend: Check: (departure_date - TODAY) > 10 days

    alt Cancellation Allowed
        Backend->>Database: BEGIN TRANSACTION
        Backend->>Database: UPDATE RESERVATIONS<br/>SET status='CANCELLED',<br/>payment_status='REFUNDED',<br/>cancelled_at=NOW(),<br/>cancellation_reason=?
        Backend->>Database: UPDATE CRUISE_CABINS<br/>SET is_available=TRUE
        Backend->>Database: UPDATE CRUISES<br/>SET available_cabins = available_cabins + 1
        Backend->>Database: UPDATE PAYMENT_TRANSACTIONS<br/>SET status='REFUNDED', refund_date=NOW()
        Backend->>Database: COMMIT TRANSACTION
        Backend-->>Frontend: Cancellation confirmed
        Frontend-->>Customer: Show cancellation confirmation
    else Within 10-Day Window
        Backend-->>Frontend: 400 Bad Request<br/>{ error: "Cannot cancel within 10 days of departure" }
        Frontend-->>Customer: Display policy error message
    end
```

---

## SQL Schema Creation Scripts

### Create RESERVATION_MODIFICATIONS Table
```sql
CREATE TABLE reservation_modifications (
    modification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    modified_by BIGINT NOT NULL,
    modification_type VARCHAR(50) NOT NULL,
    old_cabin_type_id INT NULL,
    new_cabin_type_id INT NULL,
    old_departure_date DATE NULL,
    new_departure_date DATE NULL,
    old_passenger_count INT NULL,
    new_passenger_count INT NULL,
    price_difference DECIMAL(10,2) DEFAULT 0.00,
    notes TEXT,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE,
    FOREIGN KEY (modified_by) REFERENCES users(user_id),
    FOREIGN KEY (old_cabin_type_id) REFERENCES cabin_types(cabin_type_id),
    FOREIGN KEY (new_cabin_type_id) REFERENCES cabin_types(cabin_type_id),
    INDEX idx_reservation_mod (reservation_id, modified_at),
    CHECK (modification_type IN ('DATES_CHANGE', 'CABIN_CHANGE', 'PASSENGER_COUNT_CHANGE', 'MULTIPLE'))
);
```

---

## Business Logic & Constraints

### Modify Reservation Rules

1. **Dates Change:** New departure date must be in the future and availability must be confirmed.
2. **Cabin Change:** New cabin must be available (`is_available = TRUE`) and support the current passenger count.
3. **Passenger Count Change:** New count must not exceed the cabin's `max_occupancy`. At least one adult required.
4. **Price Adjustment:** Any price difference is charged or refunded via the payment gateway; a new `PAYMENT_TRANSACTIONS` record is created.

### Cancel Reservation Rules

1. **10-Day Policy:**
   ```
   DATEDIFF(cruise.departure_date, CURDATE()) > 10
   ```
2. **Idempotency:** Cannot cancel an already-cancelled or completed reservation.
3. **Refund:** Full refund issued; `payment_transactions.status` updated to `REFUNDED` and `refund_amount` set.

---

## Sample Queries

### Modify Reservation — Cabin Change
```sql
-- 1. Log the modification
INSERT INTO reservation_modifications (
    reservation_id, modified_by, modification_type,
    old_cabin_type_id, new_cabin_type_id, price_difference
) VALUES (?, ?, 'CABIN_CHANGE', 3, 4, 500.00);

-- 2. Release old cabin, assign new cabin
UPDATE cruise_cabins SET is_available = TRUE  WHERE cruise_cabin_id = ?;  -- old
UPDATE cruise_cabins SET is_available = FALSE WHERE cruise_cabin_id = ?;  -- new

-- 3. Update reservation
UPDATE reservations
SET cruise_cabin_id = ?,
    total_price = total_price + 500.00,
    updated_at = CURRENT_TIMESTAMP
WHERE reservation_id = ?;
```

### Cancel Reservation with Policy Check
```sql
-- Check 10-day cancellation policy
SELECT
    r.reservation_id,
    r.status,
    c.departure_date,
    DATEDIFF(c.departure_date, CURDATE()) AS days_until_departure
FROM reservations r
JOIN cruises c ON r.cruise_id = c.cruise_id
WHERE r.reservation_id = ?
  AND r.status NOT IN ('CANCELLED', 'COMPLETED');
-- Application checks days_until_departure > 10 before proceeding

-- Perform cancellation
UPDATE reservations
SET status = 'CANCELLED',
    payment_status = 'REFUNDED',
    cancelled_at = CURRENT_TIMESTAMP,
    cancellation_reason = 'Customer requested cancellation'
WHERE reservation_id = ?;

-- Restore cabin availability
UPDATE cruise_cabins SET is_available = TRUE WHERE cruise_cabin_id = ?;
UPDATE cruises SET available_cabins = available_cabins + 1 WHERE cruise_id = ?;

-- Record refund
UPDATE payment_transactions
SET status = 'REFUNDED',
    refund_amount = ?,
    refund_date = CURRENT_TIMESTAMP
WHERE reservation_id = ? AND status = 'COMPLETED';
```

### Get Modification History for a Reservation
```sql
SELECT
    rm.modified_at,
    rm.modification_type,
    rm.price_difference,
    rm.notes,
    CONCAT(u.first_name, ' ', u.last_name) AS modified_by,
    oct.type_name AS old_cabin_type,
    nct.type_name AS new_cabin_type,
    rm.old_departure_date,
    rm.new_departure_date,
    rm.old_passenger_count,
    rm.new_passenger_count
FROM reservation_modifications rm
JOIN users u ON rm.modified_by = u.user_id
LEFT JOIN cabin_types oct ON rm.old_cabin_type_id = oct.cabin_type_id
LEFT JOIN cabin_types nct ON rm.new_cabin_type_id = nct.cabin_type_id
WHERE rm.reservation_id = ?
ORDER BY rm.modified_at DESC;
```

---

---

# Customer Profile Management Module

The `USERS` table (defined in the [User Authentication & Registration Module](#user-authentication--registration-module)) already contains all fields required for customer profile management.

## Editable Profile Fields

| Field          | Column in USERS | Notes                                         |
|----------------|-----------------|-----------------------------------------------|
| Address        | `address`, `city`, `state`, `postal_code`, `country` | Full address decomposition |
| Phone number   | `phone_number`  |                                               |
| Email          | `email`         | Triggers re-verification on change            |
| Password       | `password_hash` | Requires current password verification; invalidates other sessions |

---

## Customer Profile Update Workflow

```mermaid
sequenceDiagram
    participant Customer
    participant Frontend
    participant Backend
    participant Database

    Customer->>Frontend: Navigate to Profile Page
    Frontend->>Backend: GET /api/users/profile
    Backend->>Database: SELECT FROM USERS WHERE user_id = ?
    Database-->>Backend: Current profile data
    Backend-->>Frontend: Profile data (excluding password_hash)
    Frontend-->>Customer: Display pre-filled profile form

    Customer->>Frontend: Edit fields and submit
    Frontend->>Backend: PUT /api/users/profile
    Backend->>Backend: Validate input (phone format, email format, etc.)

    alt Email Changed
        Backend->>Backend: Check email uniqueness
        Backend->>Database: UPDATE USERS SET email=?, email_verified=FALSE
        Backend->>Backend: Send verification email
        Backend-->>Frontend: Success + "Verify new email" notice
    else Password Changed
        Backend->>Backend: Verify current password hash
        Backend->>Backend: Hash new password
        Backend->>Database: UPDATE USERS SET password_hash=?
        Backend->>Database: DELETE FROM USER_SESSIONS<br/>WHERE user_id=? AND session_token != currentToken
        Backend-->>Frontend: Success + re-login notice
    else Other Fields
        Backend->>Database: UPDATE USERS SET address=?, phone_number=?, etc.
        Backend-->>Frontend: Success response
    end

    Frontend-->>Customer: Display updated profile confirmation
```

---

## Sample Queries

### Get Customer Profile
```sql
SELECT
    user_id, first_name, last_name, email, username,
    phone_number, address, city, state, postal_code, country,
    email_verified, is_active, created_at, last_login
FROM users
WHERE user_id = ?;
```

### Update Personal Information
```sql
UPDATE users
SET phone_number  = ?,
    address       = ?,
    city          = ?,
    state         = ?,
    postal_code   = ?,
    country       = ?,
    updated_at    = CURRENT_TIMESTAMP
WHERE user_id = ?;
```

### Update Email (Triggers Re-verification)
```sql
UPDATE users
SET email          = ?,
    email_verified = FALSE,
    updated_at     = CURRENT_TIMESTAMP
WHERE user_id = ?;
-- Application must send a verification email after this update
```

### Change Password
```sql
-- 1. Verify current password (in application layer via bcrypt compare)
-- 2. Update to new hash
UPDATE users
SET password_hash = ?,
    updated_at    = CURRENT_TIMESTAMP
WHERE user_id = ?;

-- 3. Invalidate all other sessions
DELETE FROM user_sessions
WHERE user_id = ?
  AND session_token != ?;  -- keep current session token
```

---

## Security Considerations for Profile Updates

1. **Email Changes:** Set `email_verified = FALSE` and resend verification email; do not allow certain features until re-verified.
2. **Password Changes:** Hash with bcrypt/Argon2 before storage; invalidate all previously issued JWT tokens / sessions.
3. **Rate Limiting:** Apply rate limiting to profile update endpoints to prevent abuse.
4. **Audit Logging:** Consider storing a `USER_PROFILE_AUDIT` table to track changes to sensitive fields (email, password) over time.
