# Code-Challenge Backend

## Run with Docker

### Build with gradle

    ./gradlew clean build

### Build docker container

    docker build -t challenge .

### Run docker container

    docker run -it --rm -d -p8080:8080 challenge
 

## Api:

### Post

```http
POST /post
```
#### Request Body(JSON)

| Parameter | Type | Required | Validation | Description |
| :--- | :--- | :--- | :--- | :--- |
| `handle`  | `String` | &#9745; | min 1; max 20  | Username |
| `message` | `String` | &#9745; | min 1; max 140 | Message body|

#### Response

| Type | Code | Description |
| :--- | :--- | :--- |
| Ok | 200 | See payload below |
| Not Found | 404 | The user was not found |
| ISE | 500 | Everything else |

#### Payload

| Field | Type | Description |
| :--- | :--- | :--- |
| `id`  | `UUID` | Id |
| `author` | `UserEntity` | Post's author |
| `message` | `String` | Post body |
| `timestamp` | `LocalDateTime` | Post created at |

----

### Wall

A user sees a list of the messages they've posted, in reverse chronological order.


```http
GET /wall/{handle}
```
#### Path parameter

| Parameter | Type | Required | Validation | Description |
| :--- | :--- | :--- | :--- | :--- |
| `handle`  | `String` | &#9745; | min 1; max 20  | Username |

#### Response

| Type | Code | Description |
| :--- | :--- | :--- |
| Ok | 200 | See payload below |
| Not Found | 404 | The user was not found |
| ISE | 500 | Everything else |

#### Payload (Array)

| Field | Type | Description |
| :--- | :--- | :--- |
| `id`  | `UUID` | Id |
| `author` | `UserEntity` | Post's author |
| `message` | `String` | Post body |
| `timestamp` | `LocalDateTime` | Post created at |

----

### Timeline

A sees a list of the messages posted by all the people they follow, in reverse chronological order.

```http
GET /timeline/{handle}
```
#### Path parameter

| Parameter | Type | Required | Validation | Description |
| :--- | :--- | :--- | :--- | :--- |
| `handle`  | `String` | &#9745; | min 1; max 20  | Username |

#### Response

| Type | Code | Description |
| :--- | :--- | :--- |
| Ok | 200 | See payload below |
| Not Found | 404 | The user was not found |
| ISE | 500 | Everything else |

#### Payload (Array)

| Field | Type | Description |
| :--- | :--- | :--- |
| `id`  | `UUID` | Id |
| `author` | `UserEntity` | Post's author |
| `message` | `String` | Post body |
| `timestamp` | `LocalDateTime` | Post created at |

----

### Follow

A user is able to follow another user. Following doesn't have to be reciprocal: Alice can follow Bob without Bob having to follow Alice.

```http
POST /follow
```
#### Body(JSON)

| Parameter | Type | Required | Validation | Description |
| :--- | :--- | :--- | :--- | :--- |
| `followerHandle`  | `String` | &#9745; | min 1; max 20  | Username |
| `followedHandle`  | `String` | &#9745; | min 1; max 20  | Username |

#### Response

| Type | Code | Description |
| :--- | :--- | :--- |
| Ok | 200 | No payload |
| Not Found | 404 | The user was not found |
| Conflict | 409 | The user is already being followed |
| ISE | 500 | Everything else |
----

### Unfollow

A user is able to unfollow another user. 

```http
POST /unfollow
```
#### Body(JSON)

| Parameter | Type | Required | Validation | Description |
| :--- | :--- | :--- | :--- | :--- |
| `followerHandle`  | `String` | &#9745; | min 1; max 20  | Username |
| `followedHandle`  | `String` | &#9745; | min 1; max 20  | Username |

#### Response

| Type | Code | Description |
| :--- | :--- | :--- |
| Ok | 200 | No payload |
| Not Found | 404 | The user was not found |
| Conflict | 409 | The user is not being followed |
| ISE | 500 | Everything else |

----
