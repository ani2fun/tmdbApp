import org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
import sbtcrossproject.CrossPlugin.autoImport.crossProject
import sbtcrossproject.CrossType

lazy val library = new Object {

  val version = new Object {
    val scala                      = "2.12.8"
    val akkaHttp                   = "10.1.7"
    val akka                       = "2.5.20"
    val circe                      = "0.10.1"
    val akkaHttpCirce              = "1.22.0"
    val scalaGuice                 = "4.2.2"
    val scalaTest                  = "3.0.6"
    val mockitoScala               = "1.1.2"
    val scalaJsDom                 = "0.9.6"
    val scalaJsReact               = "1.4.2"
    val scalaCss                   = "0.5.5"
    val scalaJsScripts             = "1.1.2"
    val commonsIO                  = "2.6"
    val logback                    = "1.2.3"
    val log4j                      = "2.11.2"
    val japgollyTestState          = "2.2.4"
    val japgollyTest               = "1.3.1"
    val japgollyTestUtil           = "1.18"
    val react                      = "16.7.0"
    val fontAwesome                = "5.7.1"
    val jquery                     = "3.3.1"
    val bootstrap                  = "4.3.0"
  }
  
  val akkaHttp                   = "com.typesafe.akka"            %% "akka-http"              % version.akkaHttp
  val akkaActor                  = "com.typesafe.akka"            %% "akka-actor"             % version.akka
  val akkaStream                 = "com.typesafe.akka"            %% "akka-stream"            % version.akka
  val akkaHttpCirce              = "de.heikoseeberger"            %% "akka-http-circe"        % version.akkaHttpCirce
  val akkaSlf4j                  = "com.typesafe.akka"            %% "akka-slf4j"             % version.akka
  val circeCore                  = "io.circe"                     %% "circe-core"             % version.circe
  val circeGeneric               = "io.circe"                     %% "circe-generic"          % version.circe
  val circeParser                = "io.circe"                     %% "circe-parser"           % version.circe
  val scalaGuice                 = "net.codingwell"               %% "scala-guice"            % version.scalaGuice
  val scalaJsScripts             = "com.vmunier"                  %% "scalajs-scripts"        % version.scalaJsScripts
  val logback                    = "ch.qos.logback"               % "logback-classic"         % version.logback

  // js dependencies
  private val npm = "org.webjars.npm"

  val fontAwesomeJar = "org.webjars" % "font-awesome" % version.fontAwesome


  val react          = npm % "react"     % version.react / "umd/react.development.js" minified "umd/react.production.min.js"
  val reactDom       = npm % "react-dom" % version.react / "umd/react-dom.development.js" minified "umd/react-dom.production.min.js" dependsOn react.jsDep.resourceName
  val reactDomServer = npm % "react-dom" % version.react / "umd/react-dom-server.browser.development.js" minified "umd/react-dom-server.browser.production.min.js" dependsOn reactDom.jsDep.resourceName

  val jquery        = npm % "jquery" % version.jquery
  val bootstrap     = npm % "bootstrap" % version.bootstrap

  val reactTestUtils = npm % "react-dom" % version.react % Test / "umd/react-dom-test-utils.development.js" minified "umd/react-dom-test-utils.production.min.js" dependsOn reactDom.jsDep.resourceName


  // Only for test purpose
  val akkaTestKit        = "com.typesafe.akka"        %% "akka-testkit"         % version.akka               % Test
  val akkaStreamTestKit  = "com.typesafe.akka"        %% "akka-stream-testkit"  % version.akka               % Test
  val akkaHttpTestKit    = "com.typesafe.akka"        %% "akka-http-testkit"    % version.akkaHttp           % Test
  val scalaTest          = "org.scalatest"            %% "scalatest"            % version.scalaTest          % Test
  val mockitoScala       = "org.mockito"              %% "mockito-scala"        % version.mockitoScala       % Test
  val commonsIO          = "commons-io"               % "commons-io"            % version.commonsIO          % Test
  val log4jCore          = "org.apache.logging.log4j" % "log4j-core"            % version.log4j              % Test
  val log4jApi           = "org.apache.logging.log4j" % "log4j-api"             % version.log4j              % Test
  val log4jApiImpl       = "org.apache.logging.log4j" % "log4j-slf4j-impl"      % version.log4j              % Test
  
}

lazy val root = (project in file("."))
  .aggregate(server)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen { s: State => "project server" :: s
}

// -----------------------------------------------------------------------------
// common settings
// -----------------------------------------------------------------------------

lazy val commonSettings = Seq(
  scalaVersion := library.version.scala,
  organization := "example.app",
  headerLicense := headerSettings,
  fork in run := true,
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8",
    "-encoding",
    "UTF-8",
    "-feature",
    "-Ypartial-unification",
    "-Xfatal-warnings"
  )
) ++ scalastyleSettings ++ scalafmtSettings

// -----------------------------------------------------------------------------
// modules settings
// -----------------------------------------------------------------------------
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val server = (project in file("server"))
  .enablePlugins(
    AutomateHeaderPlugin,
    SbtWeb,
    SbtTwirl,
    JavaAppPackaging,
    BuildInfoPlugin
  )
  .settings(commonSettings)
  .settings(
    name := "tmdbdemoapp",
    parallelExecution in Test := true,
    devCommands in scalaJSPipeline ++= Seq("test", "testOnly", "doc"),
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    mainClass in compile := Some("example.app.Main"),
    mainClass in run := Some("example.app.Main"),
    dependencyOverrides += library.log4jApi,
    libraryDependencies ++= Seq(
      library.akkaHttp,
      library.akkaHttpCirce,
      library.akkaStream,
      library.akkaActor,
      library.akkaStream,
      library.akkaSlf4j,
      library.circeCore,
      library.circeGeneric,
      library.circeParser,
      library.scalaGuice,
      library.scalaJsScripts,
      library.akkaTestKit,
      library.akkaStreamTestKit,
      library.akkaHttpTestKit,
      library.scalaTest,
      library.commonsIO,
      library.logback,
      library.log4jCore,
      library.log4jApi,
      library.log4jApiImpl,
    ),
    libraryDependencies ++= Seq(
      library.fontAwesomeJar,
      library.jquery,
      library.bootstrap,
    ),
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
    headerLicense := headerSettings
  ).dependsOn(sharedJvm % "compile->compile")

lazy val client = (project in file("client"))
  .enablePlugins(AutomateHeaderPlugin, ScalaJSPlugin, ScalaJSWeb)
  .settings(commonSettings)
  .settings(
    coverageEnabled := false,
    scalacOptions += "-feature",
    scalaJSUseMainModuleInitializer := true,
    jsEnv in Test := new JSDOMNodeJSEnv,
    libraryDependencies ++= Seq(
      "org.scala-js"                      %%% "scalajs-dom"                 % library.version.scalaJsDom,
      "com.github.japgolly.scalajs-react" %%% "core"                        % library.version.scalaJsReact,
      "com.github.japgolly.scalajs-react" %%% "extra"                       % library.version.scalaJsReact,
      "com.github.japgolly.scalacss"      %%% "core"                        % library.version.scalaCss,
      "com.github.japgolly.scalacss"      %%% "ext-react"                   % library.version.scalaCss,
      "io.circe"                          %%% "circe-core"                  % library.version.circe,
      "io.circe"                          %%% "circe-generic"               % library.version.circe,
      "io.circe"                          %%% "circe-parser"                % library.version.circe
    ),
    skip in packageJSDependencies := false,
    crossTarget in (Compile, fullOptJS) := file("js"),
    crossTarget in (Compile, fastOptJS) := file("js"),
    crossTarget in (Compile, packageJSDependencies) := file("js"),
    crossTarget in (Compile, packageMinifiedJSDependencies) := file("js"),
    artifactPath in (Compile, fastOptJS) := ((crossTarget in (Compile, fastOptJS)).value /
    ((moduleName in fastOptJS).value + "-opt.js")),
    jsDependencies ++= Seq(
      library.react,
      library.reactDom,
      library.reactDomServer,
      library.reactTestUtils
    )
  ).dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.circe"             %%% "circe-core"           % library.version.circe,
      "io.circe"             %%% "circe-generic"        % library.version.circe,
      "io.circe"             %%% "circe-generic-extras" % library.version.circe,
      "io.circe"             %%% "circe-parser"         % library.version.circe,
      "org.scalatest"        %%% "scalatest"            % library.version.scalaTest % Test
    ),
    autoAPIMappings := true
  )
  .jvmSettings(
  libraryDependencies ++= Seq(
    library.mockitoScala
  ))
  .jsSettings(
    skip in packageJSDependencies := false,
    jsEnv in Test := new JSDOMNodeJSEnv
  )

// -----------------------------------------------------------------------------
// scalastyle settings
// -----------------------------------------------------------------------------

lazy val scalastyleSettings = Seq(
  scalastyleFailOnWarning := true
)

// -----------------------------------------------------------------------------
// header settings
// -----------------------------------------------------------------------------

lazy val headerSettings = Some(
  HeaderLicense.Custom(
    """|Copyright Company
       |""".stripMargin
  )
)

// -----------------------------------------------------------------------------
// scalafmt settings
// -----------------------------------------------------------------------------

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtOnCompile.in(Sbt) := true,
    scalafmtVersion := "1.4.0"
  )

// -----------------------------------------------------------------------------
// aliases
// -----------------------------------------------------------------------------

// SBT aliases to run multiple commands in a single call
//   Optionally add it:scalastyle if the project has integration tests
addCommandAlias(
  "styleCheck",
  "; scalafmt::test ; server/scalastyle ; server/test:scalastyle"
)

addCommandAlias(
  "scalafmt",
  "; server/scalafmt ; client/scalafmt"
)

addCommandAlias(
  "clean",
  "; server/clean ; client/clean"
)

addCommandAlias(
  "test",
  "; server/test ; sharedJVM/test; sharedJS/test" // client/test
)
