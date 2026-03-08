⚙️ Configuration
Set your application properties in application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/papernote
spring.datasource.username=YOUR_PG_USER
spring.datasource.password=YOUR_PG_PASSWORD

# JWT secret key (use a strong, random string, at least 256 bits for HS256)
jwt.secret=your-very-long-secret-key



🧪 Postman Testing Guide
1️⃣ Register a new user
POST /api/auth/register
Body:
{
  "name": "Alice",
  "email": "alice@test.com",
  "password": "secret123"
}

2️⃣ Login and get JWT token
POST /api/auth/login
Body:
{
  "email": "alice@test.com",
  "password": "secret123"
}

Copy the token from the response
Add to all subsequent requests:

Authorization: Bearer <token>
3️⃣ Create a note
POST /api/notes
Body:
{
  "title": "My Note",
  "content": "Hello world"
}

4️⃣ Search notes
GET /api/notes/search?q=hello&page=0&size=10

5️⃣ Soft delete, restore, and permanent delete
DELETE /api/notes/1           → soft delete
GET    /api/notes/trash       → view trashed notes
PATCH  /api/notes/1/restore   → restore note
DELETE /api/notes/1/permanent → permanently delete

6️⃣ Share a note (public link)
POST /api/notes/1/share       → returns sharePublicId
GET  /api/share/{publicId}    → public access, no token needed
DELETE /api/notes/1/share     → deactivate link

7️⃣ Tags
POST   /api/notes/1/tags      Body: {"name":"work"}  → assign tag
DELETE /api/notes/1/tags/1                        → remove tag from note
GET    /api/tags                                  → list all tags
GET    /api/tags/1/notes                          → list notes for a tag
