package szulc.magdalena.fitpost.remote.model

import com.sys1yagi.mastodon4j.api.entity.Results

class MyPlaces {
 var html_attributions: Array<String>?=null
    var status:String?=null
    var next_page_token:String?=null
    var results:Array<szulc.magdalena.fitpost.remote.model.Results>?=null
}