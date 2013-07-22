name := "Rest Record Module "

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.9.2"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )

seq(com.github.siasia.WebPlugin.webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5"
  Seq(
    "net.liftweb"             %% "lift-webkit"     % liftVersion  % "compile",
    "net.liftweb"             %% "lift-record"     % liftVersion  % "compile",
    "net.liftmodules"         %% "restrecord"      % (liftVersion + "-1.3-SNAPSHOT") % "compile",
    "org.specs2"              %% "specs2"          % "1.12.1"           % "test",
    "org.eclipse.jetty"        % "jetty-webapp"    % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit"  % "javax.servlet"   % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"           % "logback-classic" % "1.0.6"
  )
}

