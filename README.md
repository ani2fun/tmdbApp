# Example Movie App (playground scalajs-react-akka-http)
---

## Stack
This project uses for the server side:
* [Akka-http](https://doc.akka.io/docs/akka-http/current/) server for the REST service

This project use for the client side
* [Scala-js](https://www.scala-js.org/doc/)
* [Scalajs-react](https://github.com/japgolly/scalajs-react)

## Note

Start app locally
> `sbt "~reStart"`

Browser(Chrome)
Start app in your browser ``http://localhost:9090``
- Enter a movie name and click button search to search for movie.
- Click button `Get Trending Movies` to get trending movies list.(wip: look into the developers Networks tab for the http response for trending movies)


Backend API EndPoint to test locally -> 
GET -> ``http://localhost:9090/findmovie?moviename="avengers"``
GET -> ``http://localhost:9090/trending``

#### In case API key is not valid please generate new one at (https://www.themoviedb.org) and update application.conf for the same

