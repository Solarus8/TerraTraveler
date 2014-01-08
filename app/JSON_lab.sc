object JSON_lab {
	import play.api.libs.json.Json._
	import play.api.libs.json._
	import play.api.libs.json
	
	val myJson = Json.obj("json1" -> Json.obj("aDouble" -> "37.4928"))
                                                  //> myJson  : play.api.libs.json.JsObject = {"json1":{"aDouble":"37.4928"}}
	val myJson2 = Json.obj("json2" -> Json.obj("aDouble2" -> 37.4928))
                                                  //> myJson2  : play.api.libs.json.JsObject = {"json2":{"aDouble2":37.4928}}

	val dub: JsValue = myJson \ "json1"       //> dub  : play.api.libs.json.JsValue = {"aDouble":"37.4928"}
	
	val dub2: JsValue = myJson2 \ "json2"     //> dub2  : play.api.libs.json.JsValue = {"aDouble2":37.4928}
	
	(dub2 \ "aDouble2").validate[Double]      //> res0: play.api.libs.json.JsResult[Double] = JsSuccess(37.4928,)
	
		
}