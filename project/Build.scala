import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "aqume_courseware"
    val appVersion      = "1.0-SNAPSHOT"

    // Add your project dependencies here,
    val appDependencies = Seq(
        "postgresql" % "postgresql" % "9.1-901-1.jdbc4" // Heroku
    )

    // Add your own project settings here | resolvers
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
        
    )

}
