package szulc.magdalena.fitpost.remote.model

class Results {

    //markers
    var name:String?=null
    var geometry:Geometry?=null
    var photos:Array<Photos>?=null
    var id:String?=null
    var place_id :String?=null
    var price_level:Int = 0
    var raiting:Double=0.0
    var reference:String?=null
    var scoope:String?=null
    var types:Array<String>?=null
    var vicinity:String?=null
    var operninng_hours:OpeningHours?=null


    //place details
    var address_components:Array<AdressComponent>?=null
    var adr_address:String?=null
    var formatted_address:String?=null
    var formatted_phone_number:String?=null
    var international_phone_number:String?=null
    var reviews:Array<Review>?=null
    var url:String?=null
    var utc_offset:Int=0
    var website:String?=null

}