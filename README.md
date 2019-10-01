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
#### Body(JSON)

| Parameter | Type | Required | Validation | Description |
| :--- | :--- | :--- | :--- | :--- |
| `handle`  | `String` | &#9745; | min 1; max 20  | Username |
| `message` | `String` | &#9745; | min 1; max 140 | Message body|

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

----
