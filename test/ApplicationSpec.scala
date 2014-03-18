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
class ApplicationSpec extends org.specs2.mutable.Specification 
	with UsersApiTests  with LocationsApiTests  with EventsApiTests  with TripsApiTests  with JournalsApiTests
	with UsersEdgeTests with LocationsEdgeTests with EventsEdgeTests with TripsEdgeTests with JournalsEdgeTests {
 
  
	// TODO - add test to verify server is running or nothing works 
	val serverLocation = TestCommon.serverLocation
	
	"Terra Traveler Test" should {
  	
		println("\n\n\n\n==================== Test Started " + Calendar.getInstance().getTime() + " =======================")


		// Default tests that verify web server works
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
		//                   Users API tests
		//       
		//
		"Users API test" should
		{
		    // No test for get all users
		    ttt_UsersApiTest_createUser
			ttt_UsersApiTest_getUserById
			ttt_UsersApiTest_createUserProfile
			ttt_UsersApiTest_getUserProfile
			ttt_UsersApiTest_associateUserAndEvent
							
			// At least one test must be directly inside of "should {}" or "in {} for test results 
			// to work properly. 		
			"End of users API test" in {"End" must beEqualTo("End")}		

				
		} // End of user API tests

		// =================================================================
		//                        Locations API tests
		//
		"Locations tests API" should {
		  
			ttt_placesApiTest_createPlace
			ttt_placesApiTest_getPlaceById
			ttt_placesApiTest_getPlacesByLatitudeLongitudeAndRadius
			
			"ttt_placesApiTest_createPlace3rdPartyReference - Github issue 9" in {pending}
			"ttt_placesApiTest_getPlace3rdPartyReferenceById - Github issue 9" in {pending}
			"ttt_placesApiTest_getPlace3rdPartyReferenceByTerraTravelerPlaceId - Github issue 9" in {pending}			
			//ttt_placesApiTest_createPlace3rdPartyReference
			//ttt_placesApiTest_getPlace3rdPartyReferenceById
			//ttt_placesApiTest_getPlace3rdPartyReferenceByTerraTravelerPlaceId
			
			"End of users API test" in {"End of users API test" must contain("End")}			
			
  		} // End of Places Test API

		
		// =================================================================
		//                        Events API tests	
		//
		"Events API tests" should {
		  
		    ttt_EventsApiTest_createEvent
		    ttt_EventsApiTest_getEventById
			ttt_EventsApiTest_getAllActivityTypesAndCategories
		    "Get Users (attendies) by Event ID" in {pending}		
			"Get User Contacts by User ID" in {pending}			
			"Get Events by User ID" in {pending}		
			"Get Events by location radius using location ID" in {pending}
			"Get Events by latitude, longitude, radius, activity, and category" in {pending}
	
						
			"End of Events API test" in {"End" must beEqualTo("End")}
		} // End of Events API tests
		

		// =================================================================
		//                      Trips API tests
		//
		"Trips API tests" should {
		  
			ttt_TripsApiTest_createNewTrip
			ttt_TripsApiTest_getTripsById
		  
			"End of trips API test" in {"End" must beEqualTo("End")}
		}
		

		"Edge tests" should	{
		  
			ttt_runAll_usersEdgeTests
			ttt_runAll_eventsEdgeTests
			ttt_runAll_locationsEdgeTests
			ttt_runAll_tripsEdgeTests
			ttt_runAll_journalsEdgeTests

			"End of Edge tests" in {"End" must beEqualTo("End")}
			
		} // End of Edge Test		
		
	
		"End of all Tests" in {"End" must beEqualTo("End")}
		
	
	} // End of Terra Traveler Test should

 
} // End of the main program ApplicationSpec



	
	

	
	


	 	

 	
 		

  	


