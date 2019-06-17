# Example App : Skeleton
---

## Stack
This project uses for the server side:
* [Akka-http](https://doc.akka.io/docs/akka-http/current/) server for the REST service

This project use for the client side
* [Scala-js](https://www.scala-js.org/doc/)
* [Scalajs-react](https://github.com/japgolly/scalajs-react)

## Note

Start app locally
> sbt ~reStart

Local EndPoint to test -> 
GET -> ``http://localhost:9090/findmovie?movieName="avengers"``

movie search by name -> GET `https://api.themoviedb.org/3/search/movie?api_key=5fdcb6eafc5f7c6b238952774693c9a9&include_adult=false&page=1&language=en-US&query=the+avengers`
trending movies -> GET `https://api.themoviedb.org/3/trending/movie/day?api_key=5fdcb6eafc5f7c6b238952774693c9a9&page=1`



