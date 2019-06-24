// ScalaJs sbt related
addSbtPlugin("com.vmunier"        % "sbt-web-scalajs" % "1.0.8-0.6")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"     % "0.6.26")

// Fast development turnaround when using sbt ~reStart
addSbtPlugin("io.spray"           % "sbt-revolver"             % "0.9.1")
addSbtPlugin("com.eed3si9n"       % "sbt-assembly"             % "0.14.9")
addSbtPlugin("com.typesafe.sbt"   % "sbt-twirl"                % "1.3.15")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.0")

// Faster dependencies fetching
addSbtPlugin("io.get-coursier"    % "sbt-coursier" % "1.0.3")

addSbtPlugin("com.typesafe.sbt"   % "sbt-native-packager" % "1.3.15")

// Coding style
addSbtPlugin("org.scalastyle"    %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.wartremover"   % "sbt-wartremover"        % "2.3.7")

addSbtPlugin("de.heikoseeberger" % "sbt-header"             % "5.0.0")
addSbtPlugin("com.lucidchart"    % "sbt-scalafmt"           % "1.15")
addSbtPlugin("com.typesafe.sbt"  % "sbt-license-report"     % "1.2.0")

// Test coverage
addSbtPlugin("org.scoverage"     % "sbt-scoverage"          % "1.5.1")

// CI/CD
addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "1.1.13")

// Get build version
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

// Kamon integration
addSbtPlugin("io.kamon"          % "sbt-aspectj-runner" % "1.1.1")
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent"      % "0.1.4")


// Gatling
addSbtPlugin("io.gatling" % "gatling-sbt" % "3.0.0")

//Heroku
addSbtPlugin("com.heroku" % "sbt-heroku" % "2.1.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.3")