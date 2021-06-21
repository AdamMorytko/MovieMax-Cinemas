# MovieMax-Cinemas
This application was created to manage your cinemas. The workflow is very simple -> you create a cinema, add auditoriums with seats schemas to it and then you can create screenings, which will be displayed for users. Users will be then able to reserve seats for the screenings either as a guest or they can also create accounts which will allow them to see their reservation history.
# Features
1. Database is automatically populated each time the application starts with screenings for the upcoming two weeks (cinemas, auditoriums and seats schemas are added before that as well).
2. List of all cinemas.
3. List of screenings per cinema.
4. Reserve seats as a guest.
5. Reserve seats as a user.
6. Email sender with reservation details after successfully reserving seats.
7. Simple and useful user dashboard with reservation history and a possibility to edit the profile (credentials, name).
8. Admin profile to manage the cinemas, screenings, movies, users.
9. CRUD for everything (cinemas, screenings, movies, users, reservations) in the admin dashboard.
# Live version
You can check out the application here:  
http://moviemaxcinemas.herokuapp.com  
Test user:  
login: user@test.com  
password: usertest  
Test admin:  
login: admin@test.com  
password: admintest
# How does it look like:
![Homepage](https://github.com/AdamMorytko/MovieMax-Cinemas/blob/master/src/main/resources/static/img/homePageImgs/moviemax_1.png)
# Used technologies and libraries:
- Java
- Html
- Css
- Javascript
- Thymeleaf
- Spring Boot
- Spring Security
- Spring Boot Mail
- Spring Boot Validation
- Hibernate
- MySQL (locally)
- PostgreSQL (heroku)
- Maven
- Lombok
- Google Guava
- Bootstrap
