Configuration

Set your application properties in application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/papernote
spring.datasource.username=YOUR_PG_USER
spring.datasource.password=YOUR_PG_PASSWORD

JWT secret key (use a strong, random string, at least 256 bits for HS256):
jwt.secret=your-very-long-secret-key




Postman Testing Guide

1. Register a New User
Endpoint: POST /api/auth/register
Body:
{
"name": "Alice",
"email": "alice@test.com
",
"password": "secret123"
}

2. Login and Get JWT Token
Endpoint: POST /api/auth/login
Body:
{
"email": "alice@test.com
",
"password": "secret123"
}

Copy the token from the response and add it to all subsequent requests:
Authorization: Bearer <token>

3. Create a Note
Endpoint: POST /api/notes
Body:
{
"title": "My Note",
"content": "Hello world"
}

4. Search Notes
Endpoint: GET /api/notes/search?q=hello&page=0&size=10

5. Soft Delete, Restore, and Permanent Delete
Soft delete a note: DELETE /api/notes/{id}
View trashed notes: GET /api/notes/trash
Restore a trashed note: PATCH /api/notes/{id}/restore
Permanently delete a note: DELETE /api/notes/{id}/permanent

6. Share a Note (Public Link)
Create share link: POST /api/notes/{id}/share → returns sharePublicId
Access shared note publicly: GET /api/share/{publicId}
Deactivate share link: DELETE /api/notes/{id}/share

7. Tags
Assign tag to note: POST /api/notes/{id}/tags Body: {"name":"work"}
Remove tag from note: DELETE /api/notes/{noteId}/tags/{tagId}
List all tags: GET /api/tags
List notes for a tag: GET /api/tags/{tagId}/notes
