

trait LocationsEdgeTests extends org.specs2.mutable.Specification {

	// =================================================================
	//    Locations Edge tests
	//        Create Place
	//        Create Place 3rd Party Reference
	//        Get Place 3rd Party Reference by ID
    //        Get Place 3rd Party Reference by TerraTraveler Place ID
    //        Get Place by ID
	//        Get Users (attendies) by Event ID"
	//        Get all Activity Types and Categories
	// =================================================================
  
  
  
	// =================================================================================
	//                      ttt_runAll_locationsEdgeTests
	//
	def ttt_runAll_locationsEdgeTests() {
		"Locations Edge Tests" should {
	
	  		"Edge Tests - Create Place" in {pending}
			"Edge Tests - Create Place 3rd Party Reference" in {pending}
			"Edge Tests - Get Place 3rd Party Reference by ID" in {pending}
			"Edge Tests - Get Place 3rd Party Reference by TerraTraveler Place ID" in {pending}
			"Edge Tests - Get Place by ID" in {pending}
			"Edge Tests - Get Places by latitude, longitude, and radius" in {pending}
			
			"End of Locations Edge Tests" in {"End" must beEqualTo("End")}
		}
		
	} // End of ttt_runAll_locationsEdgeTests
  
} // End of trait LocationsEdgeTests
