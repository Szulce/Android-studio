package szulc.magdalena.fitpost.providers
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import szulc.magdalena.fitpost.MyDatabaseOpenHelper
import java.lang.IllegalArgumentException

class DataContentProvider : ContentProvider() {


    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private  lateinit var database: SQLiteDatabase


    companion object {
        val AUTHORITY = "com.example.socialmediaproject.provider.DataContentProvider"
        private val STATUS_TABLE= "status"
        val CONTENT_PATH = "status"
        val CONTENT_URI :Uri = Uri.parse("content://"+ AUTHORITY +"/"+ STATUS_TABLE)
        val CONTENT_TYPE = AUTHORITY +"/"+ STATUS_TABLE
        val STATUS = -1
    }


    private fun initializeUriMatching(){
        uriMatcher.addURI(
            AUTHORITY,
            CONTENT_PATH +"/#",1)
        uriMatcher.addURI(
            AUTHORITY,
            CONTENT_PATH,0)
    }

    override fun onCreate():Boolean{
        val handler = MyDatabaseOpenHelper(context!!)
        database = handler.getWritableDatabase()
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        dbInnit()
        var delCount:Int
        when(uriMatcher.match(uri))
        {
            STATUS ->delCount = database.delete(MyDatabaseOpenHelper.TABLE_STATUS,selection,selectionArgs)
            else -> throw IllegalArgumentException("unknown")
        }
        context!!.contentResolver.notifyChange(uri,null)
        return delCount
    }

    override fun getType(uri: Uri): String? {
        when(uriMatcher.match(uri)){
            STATUS -> return CONTENT_TYPE
            else -> throw  IllegalArgumentException("argument error")
        }
    }

    override fun insert(uri: Uri, p1: ContentValues?): Uri? {
       dbInnit()
        val id = database.insert(MyDatabaseOpenHelper.TABLE_STATUS,null,p1)
        if(id >0){
            val _uri = ContentUris.withAppendedId(CONTENT_URI,id)
            context!!.contentResolver.notifyChange(_uri,null)
            return _uri
        }
        throw  SQLException("Insert failed:$uri")


    }


    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
       dbInnit()
        val cursor:Cursor
        when(uriMatcher.match(uri)){
            STATUS -> cursor = database.query(MyDatabaseOpenHelper.TABLE_STATUS, MyDatabaseOpenHelper.COLUMNS,selection,null,null,null,"id DESC")
            else -> throw  IllegalArgumentException("illegal argument $uri")
        }
        cursor.setNotificationUri(context!!.contentResolver,uri)
        while (cursor.moveToNext()){
            println("Test:"+cursor.getString(cursor.getColumnIndex("id")))
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        dbInnit()
        return 0
    }

    private fun dbInnit() {
        val handler = MyDatabaseOpenHelper(context!!)
        database = handler.getWritableDatabase()

    }
}
