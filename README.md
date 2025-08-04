
# Lucky Credit App

A backend API that powers **smart, controlled donation distribution**. It lets donors send money to selected users based on deep filtering rules and ranked randomization logic. Think of it as **targeted mass-giveaway platform** with precision.

## What It Does

- **Register user profiles** with gender, occupation, nationality, height, family size, and more
- **generate bank-like accounts** per user
- **Donate to multiple users at once**, filtered by custom criteria
- **Intelligent Donations** A donor can transfer money to multiple recipients filtered by parameters like gender, occupation, height, and more.
- **Rank and randomize recipients intelligently** for fair and diverse selection (Something like a score ranking system used in dating apps/job platforms
- **AI-powered Filtering** Recipients are selected automatically based on request criteria.
- **Track transactions**, with note and donor reference
- **Manual + fake data seeding** on startup

---

## API Overview

### Create a User

**POST** `/api/user`

```json
{
  "username": "ahmad",
  "realName": "Ahmad Adewumi",
  "email": "ahmad@example.com",
  "phoneNumber": "08012345678",
  "occupation": "Software Engineer",
  "gender": "MALE",
  "nationality": "Nigerian",
  "description": "Junior Java dev with a knack for problm solving",
  "height_in_cm": 180.5,
  "maritalStatus": "SINGLE",
  "familySize": 68,
  "siblingsCount": 37,
  "skinColor": "BLACK"
}
```

---

### Create an Account for a User

**POST** `/api/account/create/{userId}`  
Links a account to a user. Needed for donating or receiving funds.

---

### Run a Giveaway

**POST** `/api/giveaway?amountPerUser=500&note=a sadaqah for you all&donorAccountId=12f95e79-1afa-4319-b812-1d70f703b433`

**Request Body (filter criteria):**

```json
{
  "gender": "FEMALE",
  "color": "BLACK",
  "occupation": "Doctor",
  "nationality": "Canada",
  "maritalStatus": "SINGLE",
  "minHeight": 10,
  "maxHeight": 190,
  "minFamilySize": 3,
  "maxFamilySize": 8,
  "minSiblingsCount": 1,
  "maxSiblingsCount": 4,
  "userCount": 2
}
```

### Behind the Scenes

- **Filtering**: Matches users who meet all the above criteria.
- **Ranking**: Candidates are **ranked by multiple traits** (e.g. height, family size, siblings).
- **Randomization**: Then **randomized** from the ranked pool to keep it fair but meaningful.
- **Final Transfer**: Amount is deducted from the donor and sent to each recipient.

---

## Seeding

Fake users and accounts are loaded via `DataLoader.java`.

After first run, **comment out the `@Component` annotation** to prevent reseeding:

```java
//@Component // disable after initial seed
public class DataLoader { ... }
```

---

## Tech Stack

- Java 21
- Spring Boot
- Flyway for Databse Versioning
- PostgreSQL
- Spring Data JPA
- Jackson
- Lombok
- Maven

---

## Notes

- No authentication added yet.
- Use Postman or cURL to test all endpoints.
- All monetary transfers and account operations are transactional and traceable (Future implementation).

---
