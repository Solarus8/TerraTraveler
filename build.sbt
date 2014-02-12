name := "terratraveler"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2" % "2.3.7" % "test",
  	jdbc,
  	anorm,
  	cache
)     

play.Project.playScalaSettings
