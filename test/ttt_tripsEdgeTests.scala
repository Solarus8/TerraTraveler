

trait TripsEdgeTests extends org.specs2.mutable.Specification {
  
	// =================================================================================
	//    Trips API tests
	//        Create New Trip
	//        Get trip by ID
	//
	// =================================================================================
  

  	// =================================================================================
    //                   ttt_runAll_tripsEdgeTests
	// 
	def ttt_runAll_tripsEdgeTests() {
	  
		"Events Edge tests" should {
		  
			"Edge Tests - Create new Trip" in {pending}
			"Edge Tests - Get Trip by ID" in {pending}
		  
		  
			"End of Events Edge Tests" in {"End" must beEqualTo("End")}
		}
	  
	  
	} // End of ttt_runAll_tripsEdgeTests
	

} // End trait TripsEdgeTests
