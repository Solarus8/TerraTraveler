import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import org.specs2.runner._
import org.junit.runner._

import java.util.Calendar
import play.api.test._
import play.api.test.Helpers._

// To run this test 
//     1. Start the postgresql server
//     2. Open two terminal windows and go to the Terra Traveler directory    
//        a. On one window type "play" and wait until you get the $ prompt the type 
//           $ start -Dhttp.port=9998
//        b. In the other terminal window type: play test
//

@RunWith(classOf[JUnitRunner])
//class ApplicationSpec extends Specification with JsonMatchers {
class ApplicationSpec extends org.specs2.mutable.Specification 
	with UsersApiTests with LocationsApiTests with EventsApiTests with TripsApiTests
	with UsersEdgeTests with LocationsEdgeTests with EventsEdgeTests with TripsEdgeTests {
 
  
	// TODO - add test to verify server is running or nothing works 
	val serverLocation = TestCommon.serverLocation
	
	"Terra Traveler Test" should {
  	
		println("\n\n\n\n==================== Test Started " + Calendar.getInstance().getTime() + " =======================")


		"Web server tests" should {
		
			"send 404 on a bad request" in new WithApplication{
				  route(FakeRequest(GET, "/boum")) must beNone
			}
			

			
			"render the index page" in new WithApplication{
				val home = route(FakeRequest(GET, "/")).get
				
				status(home) must equalTo(OK)
				contentType(home) must beSome.which(_ == "text/html")
				contentAsString(home) must contain ("Terra Traveler app is ready.")
			}
		}

	

		// =================================================================
		//    Users API tests
		//        Get User by ID
		//        Create User
		//        Create UserProfile
		//        Get UserProfile
		//        Get all Users (Should test be written since this may go away
		//                       but is very useful for test and debug
		// =================================================================
		
		"Users API test" should
		{
		  
		    ttt_UsersApiTest_createUser
			ttt_UsersApiTest_getUserById
			ttt_UsersApiTest_createUserProfile
			ttt_UsersApiTest_getUserProfile
			ttt_UsersApiTest_associateUserAndEvent
			
			// TODO - Get All Users test would take a long time - test not written yet
						
			// At least one test must be directly inside of "should {}" for test results to 
			// work properly. Tests in a function work but tests in another file or class don't print
			// the results correctly.				
			"End of users API test" in {"End" must beEqualTo("End")}		

				
		} // End of user API tests

		// =================================================================
		//    Places API tests
		//        Create Place
		//        Create Place 3rd Party Reference
		//        Get Place 3rd Party Reference by ID
	    //        Get Place 3rd Party Reference by TerraTraveler Place ID
	    //        Get Place by ID
		//        Get Users (attendies) by Event ID"
		//        Get all Activity Types and Categories
		//        Get User Contacts by User ID
		//        Associate User to Contact
		//        Create Place 3rd Party Reference
		// =================================================================
			
		"Locations tests API" should {
		  
		  
		  
			ttt_placesApiTest_createPlace
			ttt_placesApiTest_getPlaceById
//			ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius
	
						
			"Create Place 3rd Party Reference" in {pending}
			"Get Place 3rd Party Reference by ID" in {pending}	
			"Get Place 3rd Party Reference by TerraTraveler Place ID" in {pending}	
			"Get Users (attendies) by Event ID" in {pending}		
			"Get User Contacts by User ID" in {pending}	

			"Create Place 3rd Party Reference" in {pending}				
			
			
			"End of users API test" in {			
					"End of users API test" must contain("End")
			}
			
  		} // End of Places Test API

		
	
		
		
		// =================================================================
		//    Events API tests	
		//        Create Event
		//        Get Events by User ID
		//        Get Event by ID
		//
		//        What is the accuracy for radius of events at the equator, Mountain View,
		//            Fairbanks Alaska and Johannesburg South Affrica.
		// =================================================================
		
		"Events API tests" should {
		  
		    ttt_EventsApiTest_createEvent
		    ttt_EventsApiTest_getEventById
			ttt_EventsApiTest_getAllActivityTypesAndCategories
		
			"Get Events by User ID" in {pending}		
			"Get Events by location radius using location ID" in {pending}
			"Get Events by latitude, longitude, radius, activity, and category" in {pending}
	
						
			"End of Events API test" in {"End" must beEqualTo("End")}
		} // End of Events API tests
		

		// =================================================================
		//    Trips API tests
		//        Create New Trip
		//        Get trip by ID
		//
		// =================================================================
		"Trips API tests" should {
		  
			ttt_TripsApiTest_createNewTrip
			ttt_TripsApiTest_getTripsById
		  
			"End of trips API test" in {"End" must beEqualTo("End")}
		}
		

		"Edge tests" should	{
		
			"Edge Tests - Create New User /api/v1/users" should {
			  
				ttt_EdgeTests_CreateUser
			  			  				
				"End of Edge Tests for Create User" in {"End" must beEqualTo("End")}
				  				  

			
			} // End of Create User edge tests


			"Edge Tests - Get User By Id /api/v1/users/1:" should {
			  
				ttt_EdgeTests_getUserById
			  				
				"End of Edge Tests for Get User By Id" in {"End" must beEqualTo("End")}
									
			}
			
			"Edge Tests - Create User Profile" in {pending}
			"Edge Tests - Get User Profile" in {pending}			
			"Edge Tests - Create Event" in {pending}
			"Edge Tests - Get Events by User Id" in {pending}
			"Edge Tests - Get Event by Id" in {pending}
			"Edge Tests - Get Events by location radius using location ID" in {pending}
			"Edge Tests - Get Events by latitude, longitude, radius, activity, and category" in {pending}
			"Edge Tests - Create Place" in {pending}
			"Edge Tests - Create Place 3rd Party Reference" in {pending}
			"Edge Tests - Get Place 3rd Party Reference by ID" in {pending}
			"Edge Tests - Get Place 3rd Party Reference by TerraTraveler Place ID" in {pending}
			"Edge Tests - Get Place by ID" in {pending}
			"Edge Tests - Get Places by latitude, longitude, and radius" in {pending}
			"Edge Tests - Associate User and Event" in {pending}
			"Edge Tests - Get Users (attendies) by Event ID" in {pending}
			"Edge Tests - Get all Activity Types and Categories" in {pending}
			"Edge Tests - Get User Contacts by User ID" in {pending}
			"Edge Tests - Associate User to Contact" in {pending}
			"Edge Tests - Create new Trip" in {pending}
			"Edge Tests - Get Trip by ID" in {pending}
			"Edge Tests - Create new Journal" in {pending}
			"Edge Tests - Get Journal by ID" in {pending}
			
		} // End of Edge Test		
		
	
		"End of all Tests" in {"End" must beEqualTo("End")}
		
	
	} // End of Terra Traveler Test should

  //} // End of "Application" should 
  
}

	
	

	
	


	 	

 	
 		

  	


